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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Optional;
import java.util.UUID;

import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.config.DefaultConfigMapSupplier;
import org.infrastructurebuilder.util.core.ExecutionResponse;
import org.infrastructurebuilder.util.core.PathSupplier;
import org.infrastructurebuilder.util.core.TestingPathSupplier;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import liquibase.exception.DatabaseException;

public class LocalLiquibaseExecutorTest {

  private final static Logger log = LoggerFactory.getLogger(LocalLiquibaseExecutorTest.class);
  private static TestingPathSupplier wps;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    wps = new TestingPathSupplier();
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    wps.finalize();
  }

  private LocalLiquibaseExecutor l, lbbConfigured;
  private PathSupplier rollback;
  private DataSourceSupplier dsp;
  private String cl;
  private String ctxs;
  private String labelsx;
  private String tagsx;
  private ConfigMapSupplier paramsx;
  private Optional<String> optDefaultSchema;
  private String lschemaX;
  private String dbCLTable;
  private String dbLockTbl;
  private boolean isDropFirst;
  private boolean isClearChecksums;
  private boolean isShouldRun;
  private Optional<Boolean> isTestRollbackOnUpdate = Optional.empty();
  private String tablespace;

  @Before
  public void setUp() throws Exception {
    String jdbc = "jdbc:sqlite:" + wps.getTestClasses().resolve("test.trace.db").toAbsolutePath().toString();
    rollback = () -> wps.get().resolve(UUID.randomUUID().toString());
    SQLiteConfig cfg = new SQLiteConfig();
    cfg.setBusyTimeout(300);
    cfg.setReadOnly(true);
    SQLiteDataSource lds = new SQLiteDataSource(cfg);
    lds.setUrl(jdbc);
    dsp = () -> lds;
    paramsx = new DefaultConfigMapSupplier();

    l = new LocalLiquibaseExecutor(() -> log, rollback, dsp, "UTF-8");
    lbbConfigured = l.configure(paramsx);
    //    , cl, ctxs, labelsx, tagsx, paramsx, optDefaultSchema, lschemaX,
    //        dbCLTable, dbLockTbl, tablespace, isDropFirst, isClearChecksums, isShouldRun, isTestRollbackOnUpdate);

  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetDatabaseProductName() throws DatabaseException {
    assertEquals("SQLite", lbbConfigured.getDatabaseProductName());
  }

  @Test
  @Ignore
  public void testExecute() {
    ExecutionResponse<String, Integer> er = lbbConfigured.execute();
    assertNotNull(er);
  }

  @Test
  public void testGetParameters() {
    assertNotNull(lbbConfigured.getParameters());
  }

}
