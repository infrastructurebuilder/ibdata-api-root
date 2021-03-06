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
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import javax.sql.DataSource;

import org.infrastructurebuilder.util.BasicCredentials;
import org.infrastructurebuilder.util.CredentialsFactory;
import org.infrastructurebuilder.util.URLAndCreds;
import org.infrastructurebuilder.util.artifacts.GAV;
import org.infrastructurebuilder.util.files.IBResource;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IBDataDatabaseDriverSupplierTest {

  private final static Logger log = LoggerFactory.getLogger(IBDataDatabaseDriverSupplierTest.class);
  private static final String HINT = "hint";
  private IBDataDatabaseDriverSupplier d;

  @Before
  public void setUp() throws Exception {
    d = new IBDataDatabaseDriverSupplier() {

      @Override
      public List<GAV> getRequiredArtifacts() {
        return Collections.emptyList();
      }

      @Override
      public Optional<String> getDatabaseDriverClassName(URLAndCreds jdbcUrl) {
        return empty();
      }

      @Override
      public String getHint() {
        return HINT;
      }

      @Override
      public Optional<IBDatabaseDialect> getDialect(URLAndCreds jdbcUrl) {
        return empty();
      }

      @Override
      public boolean respondsTo(URLAndCreds jdbcURL) {
        return false;
      }

      @Override
      public Optional<Supplier<DataSource>> getDataSourceSupplier(URLAndCreds in) {
        return empty();
      }

      @Override
      public Logger getLog() {
        return log;
      }

      @Override
      public CredentialsFactory getCredentialsFactory() {
        return new CredentialsFactory() {

          @Override
          public Optional<BasicCredentials> getCredentialsFor(String query) {
            return empty();
          }
        };
      }

      @Override
      public Optional<Map<String, IBResource>> schemaFrom(URLAndCreds in, String query, String nameSpace, String name,
          Optional<String> desc) {
        // TODO Auto-generated method stub
        return empty();
      }

    };
  }

  @Test
  public void testGetJooqName() {
    assertEquals(HINT, d.getJooqName());
  }

}
