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
//package org.infrastructurebuilder.data;
//
//import static org.infrastructurebuilder.data.IBDataConstants.H_2;
//import static org.infrastructurebuilder.data.IBDataConstants.LIQUIBASE_DATABASE_CORE_H2_DATABASE;
//import static org.infrastructurebuilder.data.IBDataConstants.LIQUIBASE_DATABASE_CORE_HSQL_DATABASE;
//import static org.infrastructurebuilder.data.IBDataConstants.LIQUIBASE_DATABASE_CORE_MARIA_DB_DATABASE;
//import static org.infrastructurebuilder.data.IBDataConstants.LIQUIBASE_DATABASE_CORE_MSSQL_DATABASE;
//import static org.infrastructurebuilder.data.IBDataConstants.LIQUIBASE_DATABASE_CORE_MY_SQL_DATABASE;
//import static org.infrastructurebuilder.data.IBDataConstants.LIQUIBASE_DATABASE_CORE_SQ_LITE_DATABASE;
//
//import java.util.Objects;
//import java.util.Optional;
//
//public enum LBDDatabase {
//  H2(H_2, LIQUIBASE_DATABASE_CORE_H2_DATABASE) // H2
//  , HSQLDB("HSQLDB", LIQUIBASE_DATABASE_CORE_HSQL_DATABASE) //SQLite
//  , SQLLITE("SQLITE", LIQUIBASE_DATABASE_CORE_SQ_LITE_DATABASE) //SQLite
//  , MYSQL("MYSQL", LIQUIBASE_DATABASE_CORE_MY_SQL_DATABASE) // MySql
//  , MARIADB("MARIADB", LIQUIBASE_DATABASE_CORE_MARIA_DB_DATABASE) //
//  , SQLSERVER("SQLSERVER", LIQUIBASE_DATABASE_CORE_MSSQL_DATABASE) // SQL Server
//  , SQLSERVER2008("SQLSERVER2008", LIQUIBASE_DATABASE_CORE_MSSQL_DATABASE) // SQL Server
//  , SQLSERVER2014("SQLSERVER2014", LIQUIBASE_DATABASE_CORE_MSSQL_DATABASE) // SQL Server
//  , SQLSERVER2016("SQLSERVER2016", LIQUIBASE_DATABASE_CORE_MSSQL_DATABASE) // SQL Server
//  , SQLSERVER2017("SQLSERVER2017", LIQUIBASE_DATABASE_CORE_MSSQL_DATABASE) // SQL Server
//  //
//  ;
//  private final Optional<String> sqlDialect;
//  private final String databaseClass;
//
//  private LBDDatabase(final String sqlDialect, final String lbDatabaseClass) {
//    this.sqlDialect = Optional.ofNullable(sqlDialect);
//    this.databaseClass = Objects.requireNonNull(lbDatabaseClass);
//  }
//
//  public Optional<String> sqlDialect() {
//    return this.sqlDialect;
//  }
//
//  public String getDatabaseClass() {
//    return this.databaseClass;
//  }
//
//  @Override
//  public String toString() {
//    return ("LBDDatabase [sqlDialect = " + this.sqlDialect.toString() + ", databaseClass = " + this.databaseClass);
//  }
//}
