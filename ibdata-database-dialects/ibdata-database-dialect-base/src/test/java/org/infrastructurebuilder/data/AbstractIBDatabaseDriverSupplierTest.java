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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Supplier;

import javax.sql.DataSource;

import org.infrastructurebuilder.util.core.LoggerSupplier;
import org.infrastructurebuilder.util.core.TestingPathSupplier;
import org.infrastructurebuilder.util.credentials.basic.BasicCredentials;
import org.infrastructurebuilder.util.credentials.basic.DefaultBasicCredentials;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import liquibase.database.core.MySQLDatabase;

public class AbstractIBDatabaseDriverSupplierTest {

  private static final String JDBC_MYSQL = "jdbc:mysql:";
  private static final String MYSQL = "MYSQL";
  public final static Logger log = LoggerFactory.getLogger(AbstractIBDatabaseDriverSupplierTest.class);
  public final static TestingPathSupplier wps = new TestingPathSupplier();

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    wps.finalize();
  }

  private AbstractIBDatabaseDriverSupplier ab;
  private AbstractIBDatabaseDriverSupplier conf;
  private IBDataDatabaseDriverSupplier s;
  private String lqClass;

  @Before
  public void setUp() throws Exception {
    lqClass = MySQLDatabase.class.getCanonicalName();
    ab = new FakeAbstractIBDatabaseDriverSupplier(() -> log, MYSQL, lqClass, "X:Y:1.0.0");
    conf = (AbstractIBDatabaseDriverSupplier) ab;
    s = conf;
  }

  @Test
  public void testGetDialect() {
    assertFalse(conf.getDialect("ABC:def").isPresent());
  }

  @Test
  public void testGetRequiredArtifacts() {
    assertEquals(1, conf.getRequiredArtifacts().size());
  }

  @Test
  public void testGetHint() {
    s.getLog().debug("Testing getHint()");
    assertEquals(MYSQL, conf.getHint());
  }

  @Test
  public void testNoSuchDriver() {
    FakeAbstractIBDatabaseDriverSupplier abc = new FakeAbstractIBDatabaseDriverSupplier(() -> log, MYSQL,
        "No.Such.Driver.Classfile", "X:Y:1.0.0");
    assertFalse(abc.getDatabase().isPresent());
  }

  @Test
  public void testGet() {
    assertEquals("MySQL" /* Dunno why */, s.getDialect(JDBC_MYSQL).get().get());
  }

  @Test
  public void testGetDatabaseDriverClassName() {
    assertTrue(conf.respondsTo(JDBC_MYSQL));
    assertEquals("com.mysql.cj.jdbc.Driver", conf.getDatabaseDriverClassName(JDBC_MYSQL).get());
  }

  @Test
  public void testConnection() {

  }

  @Test(expected = SQLException.class)
  public void testFailingConnection1() throws SQLException {
    Optional<Supplier<DataSource>> k = conf.getDataSourceSupplier(JDBC_MYSQL, Optional.empty());
    Supplier<DataSource> l = k.get();
    Connection m = l.get().getConnection();
  }

  @Test(expected = SQLException.class)
  public void testFailingConnection2a() throws SQLException {
    BasicCredentials creds = new DefaultBasicCredentials("A", Optional.of("B"));
    Optional<Supplier<DataSource>> k = conf.getDataSourceSupplier(JDBC_MYSQL, Optional.of(creds));
    Supplier<DataSource> l = k.get();
    Connection m = l.get().getConnection();
  }

  @Test(expected = SQLException.class)
  public void testFailingConnection2b() throws SQLException {
    BasicCredentials creds = new DefaultBasicCredentials("A", Optional.empty());
    Optional<Supplier<DataSource>> k = conf.getDataSourceSupplier(JDBC_MYSQL, Optional.of(creds));
    Supplier<DataSource> l = k.get();
    Connection m = l.get().getConnection();
  }

  @Test
  public void testDialect1() {
    IBDatabaseDialect d = s.getDialect(JDBC_MYSQL).get();
    assertTrue(d.hibernateDialectClass().isPresent());
    assertNotNull(d.liquibaseDatabaseClass());
    assertTrue(d.springDbName().isPresent());
  }

  public static final class FakeAbstractIBDatabaseDriverSupplier extends AbstractIBDatabaseDriverSupplier {

    protected FakeAbstractIBDatabaseDriverSupplier(LoggerSupplier l, String hint, String liquibaseDatabaseClass,
        String... list) {
      super(l, hint, liquibaseDatabaseClass, list);
    }

  }

}
