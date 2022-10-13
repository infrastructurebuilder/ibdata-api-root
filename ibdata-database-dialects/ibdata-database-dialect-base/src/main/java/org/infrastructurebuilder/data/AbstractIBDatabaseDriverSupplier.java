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

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.infrastructurebuilder.util.core.DefaultGAV;
import org.infrastructurebuilder.util.core.GAV;
import org.infrastructurebuilder.util.core.LoggerSupplier;
import org.infrastructurebuilder.util.credentials.basic.BasicCredentials;
import org.jooq.SQLDialect;
import org.jooq.SQLDialect.ThirdParty;
import org.slf4j.Logger;

import liquibase.database.Database;

abstract public class AbstractIBDatabaseDriverSupplier
    //extends    AbstractConfigurableSupplier<IBDataDatabaseDriverSupplier, ClassLoader>
    implements IBDataDatabaseDriverSupplier {

  private final String hint;
  private final String databaseClass;
  private final List<DefaultGAV> gavs;
  private final Optional<Database> database;
  private final Logger log;

  protected AbstractIBDatabaseDriverSupplier(LoggerSupplier l, String hint, String liquibaseDatabaseClass,
      String... list) {
    this.log = requireNonNull(l, "LoggerSupplier").get();
    this.hint = requireNonNull(hint, "Driver hint");
    this.databaseClass = requireNonNull(liquibaseDatabaseClass, "Liquibase Database Classname");
    Database db;
    try {
      db = (Database) Class.forName(databaseClass).newInstance();
    } catch (Exception e) {
      db = null;
    }
    this.database = ofNullable(db);
    this.gavs = ofNullable(list).map(Arrays::asList).orElse(Collections.emptyList())
        // Map list to list of GAVs
        .stream().map(DefaultGAV::new).collect(toList());
  }

  @Override
  public Logger getLog() {
    return this.log;
  }

  @Override
  public Optional<IBDatabaseDialect> getDialect(String jdbcUrl) {
    return getDatabaseDriverClassName(jdbcUrl).flatMap(driver -> IBDataDatabaseUtils.bySQLDialectName(getJooqName())
        .map(j -> new DefaultIBDatabaseDialect(j, this.databaseClass)));
  }

  protected final Optional<Database> getDatabase() {
    return this.database;
  }

  public List<GAV> getRequiredArtifacts() {
    return unmodifiableList(this.gavs);
  }

//  @Override
//  public Optional<Supplier<Connection>> getDataSourceSupplier(String jdbcURL, Optional<BasicCredentials> creds) {
//    return getDatabaseDriverClassName(jdbcURL).map(driverClass -> {
//      cet.withTranslation(() -> Class.forName(driverClass));
//      return () -> creds.map(cr -> {
//        return cet.withReturningTranslation(
//            () -> DriverManager.getConnection(jdbcURL, cr.getKeyId(), cr.getSecret().orElse(null)));
//      }).orElse(cet.withReturningTranslation(() -> DriverManager.getConnection(jdbcURL)));
//    });
//  }

  @Override
  public String getHint() {
    return hint;
  }

  @Override
  public Optional<String> getDatabaseDriverClassName(String jdbcUrl) {
    return ofNullable(jdbcUrl).flatMap(url -> getDatabase().map(db -> db.getDefaultDriver(url)));
  }

  @Override
  public boolean respondsTo(String jdbcURL) {
    return getDatabaseDriverClassName(jdbcURL).isPresent();
  }

  @Override
  public Optional<Supplier<DataSource>> getDataSourceSupplier(String jdbcURL, Optional<BasicCredentials> creds) {
    return getDatabaseDriverClassName(jdbcURL).map(driverClass -> {
      //      cet.withTranslation(() -> Class.forName(driverClass));
      final BasicDataSource d = new BasicDataSource();
      d.setDriverClassName(driverClass);
      d.setUrl(jdbcURL);
      creds.ifPresent(cr -> {
        d.setUsername(cr.getKeyId());
        cr.getSecret().ifPresent(secret -> d.setPassword(secret));
      });
      return () -> d;
      //        return cet.withReturningTranslation(
      //            () -> DriverManager.getConnection(jdbcURL, cr.getKeyId(), cr.getSecret().orElse(null)));
    });
  }

  private final class DefaultIBDatabaseDialect implements IBDatabaseDialect {

    private final SQLDialect jd;
    private final String liquibase;
    private final Optional<ThirdParty> thirdParty;

    public DefaultIBDatabaseDialect(SQLDialect d, String liquibaseDatabaseClass) {
      this.jd = requireNonNull(d);
      this.thirdParty = ofNullable(jd.thirdParty());
      this.liquibase = requireNonNull(liquibaseDatabaseClass);
    }

    @Override
    public String get() {
      return this.jd.getName();
    }

    @Override
    public Optional<String> hibernateDialectClass() {
      return this.thirdParty.map(ThirdParty::hibernateDialect);
    }

    @Override
    public String liquibaseDatabaseClass() {
      return this.liquibase;
    }

    @Override
    public Optional<String> springDbName() {
      return this.thirdParty.map(ThirdParty::springDbName);
    }

  }

}