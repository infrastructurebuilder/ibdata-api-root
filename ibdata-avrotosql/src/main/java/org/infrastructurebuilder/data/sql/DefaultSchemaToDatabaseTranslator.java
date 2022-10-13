/**
 * Copyright © 2019 admin (admin@infrastructurebuilder.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.infrastructurebuilder.data.sql;

import static java.util.Objects.requireNonNull;
import static org.apache.avro.Schema.Type.RECORD;
import static org.infrastructurebuilder.data.IBDataException.cet;
import static org.infrastructurebuilder.util.core.IBUtils.writeString;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import org.apache.avro.Schema;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.util.config.IncrementingDatedStringSupplier;
import org.infrastructurebuilder.util.core.IdentifierSupplier;
import org.infrastructurebuilder.util.core.PathSupplier;

public class DefaultSchemaToDatabaseTranslator implements SchemaToDatabaseTranslator {
  public static final String NAME = "name";
  public static final String CREATE_TABLE = "createTable";

  private final Path workingPath;
  private final IdentifierSupplier idSupplier = new IncrementingDatedStringSupplier();
  private final Path targetFile;

  public DefaultSchemaToDatabaseTranslator(PathSupplier wps) {
    this.workingPath = wps.get();
    this.targetFile = workingPath.resolve("LB-" + UUID.randomUUID().toString() + ".xml");
  }

  @Override
  public synchronized Path apply(List<Schema> u) {
    final ChangeLog changeLog = new ChangeLog(idSupplier);
    requireNonNull(u).stream().forEach(schema -> changeLog.addSchema(schema));
    return cet.withReturningTranslation(() -> writeString(this.targetFile, changeLog.get()));
  }

}

abstract class LocalChangeSet {

  private final Xpp3Dom dom;
  private final IdentifierSupplier idSupplier;

  public LocalChangeSet(Xpp3Dom u, IdentifierSupplier idSupplier) {
    this.dom = requireNonNull(u);
    this.idSupplier = requireNonNull(idSupplier);

  }

  public LocalChangeSet addChildField(Xpp3Dom ct) {
    this.dom.addChild(ct);
    return this;
  }

  public LocalChangeSet setAttribute(String name, String value) {
    this.dom.setAttribute(name, value);
    return this;
  }

  public Xpp3Dom asXML() {
    return this.dom;
  }

  public IdentifierSupplier getIdSupplier() {
    return idSupplier;
  }

  public String getId() {
    return idSupplier.get();
  }
}

class ChangeLog extends LocalChangeSet implements Supplier<String> {
  private static final String LB_CHANGELOG_NS = "http://www.liquibase.org/xml/ns/dbchangelog";

  public ChangeLog(IdentifierSupplier idSupplier) {
    super(new Xpp3Dom("databaseChangeLog"), idSupplier);
    this
        //
        .setAttribute("xmlns", LB_CHANGELOG_NS)
        //
        .setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance")
        //
        .setAttribute("xmlns:ext", "http://www.liquibase.org/xml/ns/dbchangelog-ext").setAttribute("xsi:schemaLocation",
            "http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.1.xsd "
                + "http://www.liquibase.org/xml/ns/dbchangelog-ext http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-ext.xsd");

  }

  public ChangeLog addSchema(Schema u) {
    if (requireNonNull(u, "DefaultSchemaToDatabaseTranslator schema").getType() != RECORD)
      throw new IBDataException("The converted schema named " + u.getFullName() + " must be of type " + RECORD);
    this.addChildField(new CreateTableChangeSet(u, getIdSupplier()).asXML());
    return this;
  }

  @Override
  public String get() {
    return asXML().toString();
  }
}

class CreateTableChangeSet extends LocalChangeSet {
  public static final String AUTHOR = "author";
  public static final String CHANGE_SET = "changeSet";
  public static final String IBDATA_FROM_AVRO = "ibdata-from-avro";
  public static final String ID = "id";

  public CreateTableChangeSet(Schema u, IdentifierSupplier idSupplier) {
    super(new Xpp3Dom(CHANGE_SET), idSupplier);
    this.setAttribute(AUTHOR, IBDATA_FROM_AVRO).setAttribute(ID, getId());
    u.getFields().stream().map(IBDataAvroToLiquibaseUtils::addField).forEach(this::addChildField);
    // Now the hard parts

    // Indexes

    // Constraints

    // Subrecord tables
  }

}
