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
package org.infrastructurebuilder.data.schema.liquibase;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static org.infrastructurebuilder.data.IBDataException.cet;
import static org.infrastructurebuilder.data.IBMetadataUtils.stringifyDocument;

import java.io.StringReader;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.util.artifacts.Checksum;
import org.infrastructurebuilder.util.artifacts.ChecksumBuilder;
import org.infrastructurebuilder.util.artifacts.ChecksumEnabled;
import org.w3c.dom.Document;

public class IBDataLiquibaseChangelog extends AbstractIBDataLB {
  public static final String DATABASE_CHANGE_LOG = "databaseChangeLog";
  private static final long serialVersionUID = 8585631038830491760L;
//  public final static Function<Object, IBDataLiquibaseChangelog> translateToChangeLog = (document) -> cet
//      .withReturningTranslation(() -> {
//        if (document == null)
//          return new IBDataLiquibaseChangelog();
//        if (document instanceof IBDataLiquibaseChangelog)
//          return (IBDataLiquibaseChangelog) document;
//        if (document instanceof Document) {
//          Document d = (Document) document;
//          if (d.hasAttributes() || d.hasChildNodes())
//            return new IBDataLiquibaseChangelog(Xpp3DomBuilder.build(new StringReader(stringifyDocument.apply(d))));
//          else
//            return new IBDataLiquibaseChangelog();
//        } else
//          return new IBDataLiquibaseChangelog(Xpp3DomBuilder.build(new StringReader(document.toString()), true));
//      });

  public IBDataLiquibaseChangelog() {
    super(DATABASE_CHANGE_LOG);
    setAttribute("xmlns", "http://www.liquibase.org/xml/ns/dbchangelog");
    setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
    setAttribute("xmlns:ext", "http://www.liquibase.org/xml/ns/dbchangelog-ext");
    setAttribute("xmlns:pro", "http://www.liquibase.org/xml/ns/pro");
    setAttribute("xsi:schemaLocation",
        "http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.8.xsd"
            + "\n\t" + "http://www.liquibase.org/xml/ns/dbchangelog-ext http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-ext.xsd"
            + "\n\t" + "http://www.liquibase.org/xml/ns/pro http://www.liquibase.org/xml/ns/pro/liquibase-pro-3.8.xsd ");

  }

  public IBDataLiquibaseChangelog(Xpp3Dom src) {
    super(src);
    if (!src.getName().equals(DATABASE_CHANGE_LOG))
      throw new IBDataException("Name must be " + DATABASE_CHANGE_LOG);
  }

////  public IBDataLiquibaseChangelog(Object o) {
////    super(translateToChangeLog.apply(o));
////  }
////
//  public Supplier<Document> asDocumentSupplier() {
//    return () -> null;
//  }

  public IBDataLiquibaseChangelog addPreCondition(Xpp3Dom preCondition) {
    Xpp3Dom x = getOrCreateChild("preConditions");
    x.addChild(preCondition);
    return this;
  }

  @Override
  public Xpp3Dom get() {
    return this;
  }

  public IBDataLiquibaseChangelog addProperty(String name, String value, String context, String dbms, Boolean global) {
    IBLBInternal x = new IBLBInternal("property");
    x.setAttribute("name", requireNonNull(name, "property name"));
    x.setAttribute("value", requireNonNull(value, "property value"));
    x.setAttribute("context", context);
    x.setAttribute("dbms", dbms);
    x.setAttribute("global", global);
    addChild(x.get());
    return this;
  }
  public IBDataLiquibaseChangelog addInclude(String filename, Boolean relativeToChangelogFile, List<String> contexts) {
    IBLBInternal x = new IBLBInternal("include");
    x.setRequiredAttribute("file", requireNonNull(filename, "property name"));
    x.setAttribute("relativeToChangelogFile", relativeToChangelogFile);
    x.setAttribute("context", contexts);
    addChild(x.get());
    return this;
  }

  public IBDataLiquibaseChangelog addChangeSet(IBDataLiquibaseChangeSet changeSet) {
    addChild(changeSet);
    return this;
  }

  private Xpp3Dom getOrCreateChild(String child) {
    return ofNullable(getChild(child)).orElseGet(() -> {
      Xpp3Dom xpp3Dom = new Xpp3Dom(child);
      addChild(xpp3Dom);
      return xpp3Dom;
    });
  }
}
