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

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.infrastructurebuilder.data.IBDataConstants.H_2;
import static org.infrastructurebuilder.data.IBDataConstants.LIQUIBASE_DATABASE_CORE_H2_DATABASE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import javax.sql.DataSource;

import org.infrastructurebuilder.util.BasicCredentials;
import org.infrastructurebuilder.util.core.GAV;
import org.jooq.SQLDialect;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultIBDatabaseDialectMapperTest {
  private static final Logger log = LoggerFactory.getLogger(DefaultIBDatabaseDialectMapperTest.class);

  private static final String ORG_HIBERNATE_DIALECT_H2_DIALECT = "org.hibernate.dialect.H2Dialect";
  private DefaultIBDatabaseDialectMapper d;

  @Before
  public void setUp() throws Exception {

    Map<String, IBDataDatabaseDriverSupplier> z = new HashMap<>();
    IBDataDatabaseDriverSupplier i = new IBDataDatabaseDriverSupplier() {

      @Override
      public Logger getLog() {
        return log;
      }

      @Override
      public List<GAV> getRequiredArtifacts() {
        return Collections.emptyList();
      }

      @Override
      public String getHint() {
        return "H2";
      }

      @Override
      public Optional<IBDatabaseDialect> getDialect(String jdbcUrl) {
        return of(new IBDatabaseDialect() {

          @Override
          public String get() {
            return "H2";
          }

          @Override
          public Optional<String> springDbName() {
            return of(get());
          }

          @Override
          public String liquibaseDatabaseClass() {
            return LIQUIBASE_DATABASE_CORE_H2_DATABASE;
          }

          @Override
          public Optional<String> hibernateDialectClass() {
            return of(ORG_HIBERNATE_DIALECT_H2_DIALECT);
          }
        });
      }

      @Override
      public Optional<String> getDatabaseDriverClassName(String jdbcUrl) {
        return of("ABCDEFG");
      }

      @Override
      public boolean respondsTo(String jdbcURL) {
        return false;
      }

      @Override
      public Optional<Supplier<DataSource>> getDataSourceSupplier(String jdbcURL, Optional<BasicCredentials> creds) {
        return empty();
      }
    };
    i.getLog().debug("Test Setup");
    z.put(SQLDialect.H2.name(), i);
    d = new DefaultIBDatabaseDialectMapper(z);
  }

  @Test
  public void testFrom() {
    Optional<IBDatabaseDialect> q = d.getSupplier(SQLDialect.H2.name()).get().getDialect("jdbc:h2:");
    assertNotNull(q);
    assertTrue(q.isPresent());
    IBDatabaseDialect v = q.get();
    assertEquals(H_2, v.get());
    assertEquals(LIQUIBASE_DATABASE_CORE_H2_DATABASE, v.liquibaseDatabaseClass());
    assertEquals(H_2, v.springDbName().get());
    assertEquals(ORG_HIBERNATE_DIALECT_H2_DIALECT, v.hibernateDialectClass().get());
  }

}
