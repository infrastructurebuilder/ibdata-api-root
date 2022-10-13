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
import static java.util.Optional.ofNullable;
import static org.infrastructurebuilder.exceptions.IBException.cet;
import static org.infrastructurebuilder.util.core.IBUtils.nullIfBlank;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.inject.Named;
import javax.sql.DataSource;

import org.infrastructurebuilder.data.DataSourceSupplier;
import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.core.ExecutionEnabled;
import org.infrastructurebuilder.util.core.ExecutionResponse;
import org.infrastructurebuilder.util.core.LoggerSupplier;
import org.infrastructurebuilder.util.core.PathSupplier;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.configuration.ConfigurationProperty;
import liquibase.configuration.GlobalConfiguration;
import liquibase.configuration.LiquibaseConfiguration;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.database.OfflineConnection;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;

@Named(LocalLiquibaseExecutor.NAME)
public class LocalLiquibaseExecutor
    implements ExecutionEnabled<LocalLiquibaseExecutor, ConfigMapSupplier, String, Integer> {


  private static final String TEST_ROLLBACK_ON_UPDATE = "testRollbackOnUpdate";

  private static final String SHOULD_RUN = "shouldRun";

  private static final String CLEAR_CHECKSUMS = "clearChecksums";

  private static final String DROP_FIRST = "dropFirst";

  private static final String LB_CHANGE_LOG_LOCK_TABLE = "liquibaseDatabaseChangeLogLockTable";

  private static final String LB_TABLESPACE = "liquibaseTablespace";

  private static final String LB_CHANGE_LOG_TABLE = "liquibaseDatabaseChangeLogTable";

  private static final String LIQUIBASE_SCHEMA = "liquibaseSchema";

  private static final String DEFAULT_SCHEMA = "defaultSchema";

  private static final String TAGS = "tags";

  private static final String LABELS = "labels";

  private static final String CONTEXTS = "contexts";

  private static final String CHANGELOG = "changelog";

  public static final String NAME = "local-liquibase";

  protected final DataSourceSupplier dataSourceSupplier;
  protected final String changeLog;
  protected final Contexts contexts;
  protected final LabelExpression labels;
  protected final String tag;
  protected final ConfigMapSupplier parametersSupplier;
  protected final Optional<String> defaultSchema;
  protected final String liquibaseSchema;
  protected final String databaseChangeLogTable;
  protected final String databaseChangeLogLockTable;
  protected final String liquibaseTablespace;
  protected final boolean dropFirst;
  protected final boolean clearCheckSums;
  protected final boolean shouldRun;
  protected final PathSupplier rollbackFileSupplier;

  private final List<String> errors = new ArrayList<>();
  private final boolean ignoreClasspathPrefix = true;
  private final String encoding;

  protected final boolean testRollbackOnUpdate;

  private Logger log;

  public LocalLiquibaseExecutor(LoggerSupplier ls, PathSupplier rollbackPathSupplier, DataSourceSupplier dsp,
      String encoding) {
    this(ls, rollbackPathSupplier, dsp, encoding, null, null, null, null, null, Optional.empty(), null, null, null,
        null, false, false, false, false);
  }

  /**
   * Executed automatically when the bean is initialized.
   * @param ls LoggerSupplier
   * @param rollback Rollback file
   * @param dsp
   * @param cl
   * @param ctxs
   * @param labelsx
   * @param tagsx
   * @param paramsx
   * @param optDefaultSchema
   * @param lschemaX
   * @param dbCLTable
   * @param dbLockTbl
   * @param tablespace
   * @param isDropFirst
   * @param isClearChecksums
   * @param isShouldRun
   * @param isTestRollbackOnUpdate
   */
  private LocalLiquibaseExecutor(LoggerSupplier ls, PathSupplier rollback, DataSourceSupplier dsp, String encoding,
      String cl, Contexts ctxs, LabelExpression labelsx, String tagsx, ConfigMapSupplier paramsx,
      Optional<String> optDefaultSchema, String lschemaX, String dbCLTable, String dbLockTbl, String tablespace,
      boolean isDropFirst, boolean isClearChecksums, boolean isShouldRun, boolean isTestRollbackOnUpdate) {
    this.log = requireNonNull(ls).get();
    this.dataSourceSupplier = requireNonNull(dsp);
    this.encoding = requireNonNull(encoding);
    this.changeLog = cl;
    this.contexts = ctxs;
    this.labels = labelsx;
    this.tag = tagsx;
    this.parametersSupplier = paramsx;
    this.defaultSchema = optDefaultSchema;
    this.liquibaseSchema = lschemaX;
    this.databaseChangeLogTable = dbCLTable;
    this.databaseChangeLogLockTable = dbLockTbl;
    this.liquibaseTablespace = tablespace;
    this.dropFirst = isDropFirst;
    this.clearCheckSums = isClearChecksums;
    this.shouldRun = isShouldRun;
    this.testRollbackOnUpdate = isTestRollbackOnUpdate;
    this.rollbackFileSupplier = rollback;

  }

  @Override
  public LocalLiquibaseExecutor configure(ConfigMapSupplier cms) {
    ConfigMap cfg = requireNonNull(cms, "ConfigMapSupplier for LocalLiquibaseExecutor").get();
    return new LocalLiquibaseExecutor(
        // Must already exist in the previous bean
        () -> this.log, this.rollbackFileSupplier, this.dataSourceSupplier, this.encoding
        // prev? changelog
        , cfg.getString(CHANGELOG)
        // Ctxs
        , new Contexts(cfg.getString(CONTEXTS))
        // labels
        , new LabelExpression(cfg.getString(LABELS))
        // tags
        , cfg.getString(TAGS)
        // TODO Get a copy of the parameters supplied here!
        , cms
        // optDefaultSchema
        , ofNullable(cfg.getString(DEFAULT_SCHEMA))
        // lb schema
        , cfg.getString(LIQUIBASE_SCHEMA)
        // table and locktable
        , cfg.getString(LB_CHANGE_LOG_TABLE), cfg.getString(LB_CHANGE_LOG_LOCK_TABLE)
        // lb table space
        , cfg.getString(LB_TABLESPACE)
        // flags
        , cfg.getParsedBoolean(DROP_FIRST, false)
        // Clear checksums
        , cfg.getParsedBoolean(CLEAR_CHECKSUMS, false)
        // more flags
        , cfg.getParsedBoolean(SHOULD_RUN, true)
        // Test rollback on update
        , cfg.getParsedBoolean(TEST_ROLLBACK_ON_UPDATE, false));
  }

  public ConfigMap getParameters() {
    return parametersSupplier.get();
  }

  public String getDatabaseProductName() throws DatabaseException {
    Connection connection = null;
    Database database = null;
    String name = "unknown";
    try {
      DataSource dss = this.dataSourceSupplier.get();
      connection = dss.getConnection();
      database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
      name = database.getDatabaseProductName();
    } catch (SQLException e) {
      throw new DatabaseException(e);
    } finally {
      if (database != null) {
        database.close();
      } else if (connection != null) {
        try {
          if (!connection.getAutoCommit()) {
            connection.rollback();
          }
          connection.close();
        } catch (SQLException e) {
          log.log(Level.WARNING,"problem closing connection", e);
        }
      }
    }
    return name;
  }

  public ExecutionResponse<String, Integer> execute() {
    ConfigurationProperty shouldRunProperty = LiquibaseConfiguration.getInstance()
        .getProperty(GlobalConfiguration.class, GlobalConfiguration.SHOULD_RUN);

    if (!shouldRunProperty.getValue(Boolean.class)) {
      return new LocalLiquibaseBeanExectionResponse(-1, Arrays.asList("Liquibase did not run because "
          + LiquibaseConfiguration.getInstance().describeValueLookupLogic(shouldRunProperty) + " was set to false"));
    }
    if (!shouldRun) {
      return new LocalLiquibaseBeanExectionResponse(-1, Arrays.asList(
          "Liquibase did not run because 'shouldRun' property was set " + "to false on Liquibase Component." + NAME));
    }

    Liquibase liquibase = null;
    try {
      liquibase = createLiquibase(this.dataSourceSupplier.get().getConnection());
      generateRollbackFile(liquibase);
      performUpdate(liquibase);
    } catch (SQLException | LiquibaseException e) {
      throw new IBDataException(e);
    } finally {
      Database database = null;
      if (liquibase != null) {
        database = liquibase.getDatabase();
      }
      if (database != null) {
        try {
          database.close();
        } catch (DatabaseException e) {
          // Error but do nothing?
          e.printStackTrace();
        }
      }
    }

    return new LocalLiquibaseBeanExectionResponse(0, errors);
  }

  private void generateRollbackFile(Liquibase liquibase) throws LiquibaseException {
    Path rollbackFile = this.rollbackFileSupplier.get();

    try (
        OutputStream fileOutputStream = Files.newOutputStream(rollbackFile, StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        Writer output = new OutputStreamWriter(fileOutputStream, this.encoding)

    ) {

      if (tag != null) {
        liquibase.futureRollbackSQL(tag, this.contexts, this.labels, output);
      } else {
        liquibase.futureRollbackSQL(this.contexts, this.labels, output);
      }
    } catch (IOException e) {
      throw new LiquibaseException("Unable to generate rollback file.", e);
    }
  }

  protected void performUpdate(Liquibase liquibase) throws LiquibaseException {
    if (this.clearCheckSums) {
      liquibase.clearCheckSums();
    }

    if (this.testRollbackOnUpdate) {
      if (tag != null) {
        liquibase.updateTestingRollback(this.tag, this.contexts, this.labels);
      } else {
        liquibase.updateTestingRollback(this.contexts, this.labels);
      }
    } else {
      if (tag != null) {
        liquibase.update(this.tag, this.contexts, this.labels);
      } else {
        liquibase.update(this.contexts, this.labels);
      }
    }
  }

  protected Liquibase createLiquibase(Connection c) throws LiquibaseException {
    ResourceAccessor resourceAccessor = createResourceOpener();
    Liquibase liquibase = new Liquibase(this.changeLog, resourceAccessor, createDatabase(c, resourceAccessor));
    liquibase.setIgnoreClasspathPrefix(this.ignoreClasspathPrefix);
    ConfigMap parameters = getParameters();
    if (parameters != null) {
      for (String entry : parameters.keySet()) {
        liquibase.setChangeLogParameter(entry, parameters.get(entry));
      }
    }
    if (this.dropFirst) {
      liquibase.dropAll();
    }
    return liquibase;
  }

  public final static Function<String, Optional<String>> optionalIfBlank = (s) -> ofNullable(nullIfBlank.apply(s));

  /**
   * Subclasses may override this method add change some database settings such as
   * default schema before returning the database object.
   *
   * @param c
   * @return a Database implementation retrieved from the {@link DatabaseFactory}.
   * @throws DatabaseException
   */
  protected Database createDatabase(Connection c, ResourceAccessor resourceAccessor) throws DatabaseException {

    DatabaseConnection liquibaseConnection;
    if (c == null) {
      String str = "Null connection returned by liquibase datasource. Using offline unknown database";
      log.log(Level.WARNING,str);
      this.errors.add(str);
      liquibaseConnection = new OfflineConnection("offline:unknown", resourceAccessor);

    } else {
      liquibaseConnection = new JdbcConnection(c);
    }

    Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(liquibaseConnection);
    this.defaultSchema.ifPresent(s -> {
      if (database.supportsSchemas()) {
        cet.withTranslation(() -> database.setDefaultSchemaName(s));
      } else if (database.supportsCatalogs()) {
        cet.withTranslation(() -> database.setDefaultCatalogName(s));
      }

    });
    optionalIfBlank.apply(this.liquibaseSchema).ifPresent(s -> {
      if (database.supportsSchemas()) {
        database.setLiquibaseSchemaName(s);
      } else if (database.supportsCatalogs()) {
        database.setLiquibaseCatalogName(s);
      }
    });
    optionalIfBlank.apply(this.liquibaseTablespace).ifPresent(s -> {
      if (database.supportsTablespaces()) {
        database.setLiquibaseTablespaceName(s);
      }
    });
    optionalIfBlank.apply(this.databaseChangeLogTable).ifPresent(s -> {
      database.setDatabaseChangeLogTableName(s);
    });
    optionalIfBlank.apply(this.databaseChangeLogLockTable).ifPresent(s -> {
      database.setDatabaseChangeLogLockTableName(s);
    });
    return database;
  }

  /**
   * Create a new resourceOpener.
   */
  protected ResourceAccessor createResourceOpener() {
    return new ClassLoaderResourceAccessor(getClass().getClassLoader());
  }

  private final class LocalLiquibaseBeanExectionResponse implements ExecutionResponse<String, Integer> {

    private List<String> errors;
    private int response;

    public LocalLiquibaseBeanExectionResponse(int r, List<String> e) {
      this.errors = e;
      this.response = r;
    }

    @Override
    public List<String> getErrors() {
      return errors;
    }

    @Override
    public Integer getResponseValue() {
      return response;
    }

  }

}
