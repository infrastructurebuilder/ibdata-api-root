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
package org.infrastructurebuilder.data.ingest;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.data.IBDataSchemaSource;
import org.infrastructurebuilder.data.IBDataSchemaSupplier;
import org.infrastructurebuilder.data.model.PersistedIBSchema;
import org.infrastructurebuilder.data.model.io.xpp3.PersistedIBSchemaXpp3Reader;
import org.infrastructurebuilder.util.LoggerSupplier;
import org.infrastructurebuilder.util.config.AbstractCMSConfigurableSupplier;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;

@Named
public class DefaultIBDataSchemaHarvester extends AbstractIBDataSchemaHarvester {

  private static final String INLINE_SCHEMAS = "inline";

  @Inject
  public DefaultIBDataSchemaHarvester(LoggerSupplier l) {
    this(null, l);
  }

  private DefaultIBDataSchemaHarvester(ConfigMapSupplier cms, LoggerSupplier l) {
    super(cms, l);
  }

  @Override
  public AbstractCMSConfigurableSupplier<IBDataSchemaSupplier> getConfiguredSupplier(ConfigMapSupplier cms) {
    return new DefaultIBDataSchemaHarvester(cms, () -> getLog());
  }

  @Override
  protected IBDataSchemaSupplier getInstance() {
    Xpp3Dom d = getConfig().get().get(INLINE_SCHEMAS);
    return new DefaultIBDataSchemaSupplier("default", d);
  }

  public final class DefaultIBDataSchemaSupplier extends AbstractIBDataSchemaSupplier {

    private final Xpp3Dom schemas;

    public DefaultIBDataSchemaSupplier(String id, Xpp3Dom schemas) {
      super(id);
      this.schemas = Objects.requireNonNull(schemas);
    }

    @Override
    public SortedSet<IBDataSchemaSource> get() {
      List<Xpp3Dom> children = Arrays.asList(schemas.getChildren());
      if (children.stream().filter(c -> !"schema".equals(c.getName())).findFirst().isPresent()) {
        throw new IBDataException("Inline schemas must be of type <schema/>");
      }
      PersistedIBSchemaXpp3Reader dr = new PersistedIBSchemaXpp3Reader();

      SortedSet<PersistedIBSchema> a = children.stream().map(Xpp3Dom::toString).map(StringReader::new).map(input -> {
        return IBDataException.cet.withReturningTranslation(() -> dr.read(input));
      })
//
////          .map(DefaultIBDataSchema::new).map(DefaultIBDataSchemaSource::new)

          .collect(Collectors.toCollection(TreeSet::new));

      // TODO Write each PersistedIBSchema to disk individually
      return Collections.emptySortedSet(); // FIXME!!!!  Placeholder for unwritten schema
    }

  }

}
