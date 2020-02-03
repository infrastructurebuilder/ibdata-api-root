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

public interface IBDataConstants {
  public static final String APPLICATION_IBDATA_ARCHIVE = "application/ibdata-archive";
  public final static String UNCONFIGURABLE = "<!-- UNCONFIGURABLE -->";
  public static final String PASS_THRU = UNCONFIGURABLE + "passthru";
  public final static String IBDATA_WORKING_PATH_SUPPLIER = "ibdata-working-path-supplier";
  public final static String IBDATA_LOCAL_GAV_SUPPLIER = "ibdata-local-gav-supplier";
  public final static String IBDATA_DOWNLOAD_CACHE_DIR_SUPPLIER = "ibdata-download-cache-dir-supplier";
//  public final static String CACHE_DIRECTORY_CONFIG_ITEM = UNCONFIGURABLE + ".cachePath";
  public final static String TRANSFORMERSLIST = UNCONFIGURABLE + ".transformers";
  public final static String RECORD_SPLITTER = ",";
  public final static String MAP_SPLITTER = "|";
  public final static String WORKING_PATH_CONFIG_ITEM = UNCONFIGURABLE + ".workingPath";

  public static final String IBDATA_ENTITY = "IBData";
  public static final String IBDATA = "IBDATA-INF";
  public static final String IBDATA_DIR = "/" + IBDATA + "/";
  public static final String IBDATASET_XML = "ibdataset.xml";
  public static final String IBDATA_IBDATASET_XML = IBDATA_DIR + IBDATASET_XML;
  public final static String INGESTION_TARGET = "IBDATA_INGESTION_TARGET_@3123";
  public final static String TRANSFORMATION_TARGET = "IBDATA_TRANSFORMATION_TARGET_@3123";
  public final static String MARKER_FILE = ".ibdatamarker.xml";;
  public static final String ANY_TYPE = "*ANY*";
  public static final String IBDATA_WORKING_DIRECTORY = "ibdata.working.directory";

  public final static String TIMESTAMP_FORMATTER = "timestamp.formatter";
  public final static String TIME_FORMATTER = "time.formatter";
  public final static String DATE_FORMATTER = "date.formatter";

  public final static String LOCALE_LANGUAGE_PARAM = "locale.language";
  public final static String LOCALE_REGION_PARAM = "locale.region";
  public static final String H_2 = "H2";
  public static final String LIQUIBASE_DATABASE_CORE_MY_SQL_DATABASE = "liquibase.database.core.MySQLDatabase";
  public static final String LIQUIBASE_DATABASE_CORE_SQ_LITE_DATABASE = "liquibase.database.core.SQLiteDatabase";
  public static final String LIQUIBASE_DATABASE_CORE_HSQL_DATABASE = "liquibase.database.core.HsqlDatabase";
  public static final String LIQUIBASE_DATABASE_CORE_H2_DATABASE = "liquibase.database.core.H2Database";
  public static final String LIQUIBASE_DATABASE_CORE_MSSQL_DATABASE = "liquibase.database.core.MSSQLDatabase";
  public static final String LIQUIBASE_DATABASE_CORE_MARIA_DB_DATABASE = "liquibase.database.core.MariaDBDatabase";



  public static final String QUERY = "query";
  public static final String DIALECT = "dialect";
  public static final String SCHEMA = "schema"; // "Optional" (sort of )
  public static final String SCHEMA_QUERY = "schemaQuery";
  public static final String CREDS_QUERY = "credsQuery";

  public static final String METADATA = "metadata";
  public static final String INLINE = "inline";
  public static final String ORIGINAL_ASSET = "originalAsset";

  public static final String JDBC_TYPE_NAME = "jdbcTypeName";

  public static final String URLS = "URLS";
}
