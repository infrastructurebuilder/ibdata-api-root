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
package org.infrastructurebuilder.data.mariadb;

import static org.jooq.SQLDialect.MARIADB;

import javax.inject.Inject;
import javax.inject.Named;

import org.infrastructurebuilder.data.AbstractIBDatabaseDriverSupplier;
import org.infrastructurebuilder.util.LoggerSupplier;

import liquibase.database.core.MariaDBDatabase;

@Named(MariadbDatabaseDriverSupplier.NAME)
public class MariadbDatabaseDriverSupplier extends AbstractIBDatabaseDriverSupplier {
  static final String NAME = "MARIADB";

  @Inject
  public MariadbDatabaseDriverSupplier(LoggerSupplier l) {
    super(l, MARIADB.name(), MariaDBDatabase.class.getCanonicalName(), "org.mariadb.jdbc:mariadb-java-client:");
  }

}