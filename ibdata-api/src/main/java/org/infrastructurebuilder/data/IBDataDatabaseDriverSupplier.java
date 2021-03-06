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
package org.infrastructurebuilder.data;

import static java.util.Collections.emptyMap;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import javax.sql.DataSource;

import org.infrastructurebuilder.util.CredentialsFactory;
import org.infrastructurebuilder.util.LoggerEnabled;
import org.infrastructurebuilder.util.URLAndCreds;
import org.infrastructurebuilder.util.artifacts.GAV;
import org.infrastructurebuilder.util.files.IBResource;

/**
 * Supplies the class name for the driver to make a Connection using a given
 * JDBC URL
 *
 * @author mykel.alvis
 *
 */
public interface IBDataDatabaseDriverSupplier extends LoggerEnabled {

  /**
   * Return a list of coordinates that must be in the classpath in order to load
   * the driver
   *
   * @return List of GAV items in order of required insertion into the (new)
   *         classpath that the driver will be created from
   */
  List<GAV> getRequiredArtifacts();

  /**
   * Returns the Jooq Dialect name for a JDBC url if possible
   *
   * @param jdbcUrl
   * @return Jooq Dialect name or empty()
   */
  Optional<String> getDatabaseDriverClassName(URLAndCreds jdbcUrl);

  /**
   * The mapped hint for type of component
   *
   * @return non-null string hint from the component
   */
  String getHint();

  /**
   * The name of the Jooq SQLDialect enum. Defaults to be the same as the
   * getHint() call
   *
   * @return
   */
  default String getJooqName() {
    return getHint();
  }

  /**
   * Get an IBDatabaseDialect from a JDBC url if possible
   *
   * @param jdbcUrl
   * @return IBDatabaseDialect or empty() if not possible
   */
  Optional<IBDatabaseDialect> getDialect(URLAndCreds jdbcUrl);

  boolean respondsTo(URLAndCreds jdbcURL);

  CredentialsFactory getCredentialsFactory();

  Optional<Supplier<DataSource>> getDataSourceSupplier(URLAndCreds in);

  default Map<String, Object> getDbUnitConfigurationUpdates() {
    return emptyMap();
  }

  Optional<Map<String, IBResource>> schemaFrom(URLAndCreds in, String query, String nameSpace, String name,
      Optional<String> desc);


  // /**
//   * Generate a schema by running the query against the target datastore
//   *
//   * @param jdbcURL URL of the target database
//   * @param query   query to run against the database
//   * @return {@link IBSchema} instance, if possible
//   */
//
//  Optional<IBSchema> schemaFrom(URLAndCreds in, String query, String nameSpace, String name, Optional<String> desc);
}
