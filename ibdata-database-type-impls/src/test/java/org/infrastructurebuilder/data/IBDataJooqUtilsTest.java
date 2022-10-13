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

import static org.infrastructurebuilder.data.IBDataJooqUtils.getFieldFromType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Type;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.MapProxyGenericData;
import org.codehaus.plexus.configuration.xml.XmlPlexusConfiguration;
import org.infrastructurebuilder.util.constants.IBConstants;
import org.infrastructurebuilder.data.ingest.DefaultIBDataStreamIdentifierConfigBean;
import org.infrastructurebuilder.data.util.files.DefaultTypeToExtensionMapper;
import org.infrastructurebuilder.util.BasicCredentials;
import org.infrastructurebuilder.util.DefaultBasicCredentials;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.TestingPathSupplier;
import org.infrastructurebuilder.util.files.IBChecksumPathType;
import org.infrastructurebuilder.util.files.TypeToExtensionMapper;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultDataType;
import org.jooq.types.YearToMonth;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IBDataJooqUtilsTest {

  private static final String SELECT_STRING = "SELECT * FROM TEST ORDER BY ID;";

  public final static Logger log = LoggerFactory.getLogger(IBDataJooqUtilsTest.class);

  private static TestingPathSupplier wps;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    wps = new TestingPathSupplier();
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    wps.finalize();
  }

  private ConfigMap c;

  private TypeToExtensionMapper t2e;

  private String theUrl;

  private DefaultIBDataStreamIdentifierConfigBean b;

  private IBDataSourceSupplier s;

  private IBDataSource ds;

  private Connection conn;

  private IBChecksumPathType read;

  private GenericData jrmpGD = new MapProxyGenericData(new Formatters());

  private Path targetPath;

  private Schema schema;

  private Result<Record> result;

  private Record firstRecord;

  private JooqRecordWriter w;

  public static Logger getLog() {
    return log;
  }

  @Before
  public void setUp() throws Exception {
    targetPath = wps.get();
    c = new ConfigMap();
    c.put("dialect", "H2");
    theUrl = "jdbc:h2:" + wps.getTestClasses().resolve("test").toAbsolutePath().toString();
    c.put("url", theUrl);
    c.put("query", SELECT_STRING);
//    c.put(IBDataSource.TARGET_PATH, wps.get());
    c.put(IBDataConstants.DATE_FORMATTER, "yyyy-MM-dd");
    t2e = new DefaultTypeToExtensionMapper();
    b = new DefaultIBDataStreamIdentifierConfigBean();
    b.setDescription("desc");
    b.setId(UUID.randomUUID().toString());
    b.setMetadata(new XmlPlexusConfiguration("metadata"));
    b.setMimeType(IBConstants.TEXT_CSV); // obviously wrong
    b.setName("name");
    b.setPath(null);
    b.setSha512(null);
    b.setUrl(theUrl);
    if (conn == null) {
      String url = theUrl;
      BasicCredentials bc = new DefaultBasicCredentials("SA", Optional.empty());
      conn = IBDataException.cet
          .withReturningTranslation(() -> DriverManager.getConnection(url, bc.getKeyId(), bc.getSecret().orElse(null)));

    }
    SQLDialect dialect = SQLDialect.H2;
    String recordName = "test";
    Optional<String> sString;
    DSLContext create = DSL.using(conn, dialect);
    final Result<Record> firstResult = create.fetch(SELECT_STRING);
    String namespace = "org.test";
    schema = IBDataJooqUtils.schemaFromRecordResults(getLog(), namespace, recordName, "", firstResult);
    result = create.fetch(SELECT_STRING); // Read again if we had to create the schema
    getLog().info("Reading data from dataset");
    firstRecord = result.get(0); // First record
    w = new JooqRecordWriter(() -> getLog(), () -> targetPath, schema, jrmpGD);
    read = w.writeRecords(result);
  }

  @After
  public void tearDown() throws Exception {
    if (conn != null)
      conn.close();
  }

  @Test
  public void testGetFieldFromType() {
    Field<?>[] fList = firstRecord.fields();
    Field<?> f = fList[0];
    org.apache.avro.Schema.Field k = getFieldFromType("ID", f, f.getDataType(), false);
    assertNotNull(k);
    assertEquals(k.schema().getType(), Type.INT);

    k = getFieldFromType("NAME", fList[1], fList[1].getDataType(), false);
    assertEquals(k.schema().getType(), Type.STRING);
    k = getFieldFromType("BIRTHDAY", fList[2], fList[2].getDataType(), false);
    assertEquals(k.schema().getType(), Type.INT);
    k = getFieldFromType("AGE", fList[3], fList[3].getDataType(), false);
    assertEquals(k.schema().getType(), Type.INT);
    k = getFieldFromType("YES", fList[4], fList[4].getDataType(), false);
    assertEquals(k.schema().getType(), Type.BOOLEAN);

    assertNotNull(getFieldFromType("NAME", fList[1], fList[1].getDataType(), true));
    assertNotNull(getFieldFromType("YES", fList[4], fList[4].getDataType(), true));
  }

  @Test
  public void testLongType() {
    Field<Long> f = DSL.field("A", Long.class);
    org.apache.avro.Schema.Field k = getFieldFromType("A", f, f.getDataType(), true);
    assertNotNull(k);
    assertNotNull(getFieldFromType("A", f, f.getDataType(), false));
  }
  @Test
  public void testBigIntegerType() {
    Field<BigInteger> f = DSL.field("A", BigInteger.class);
    org.apache.avro.Schema.Field k = getFieldFromType("A", f, f.getDataType(), true);
    assertNotNull(k);
    assertNotNull(getFieldFromType("A", f, f.getDataType(), false));
  }

  @Test
  public void testArrayType() {
    Field<String[][]> f = DSL.array(DSL.array(DSL.field("A", String.class)));
    org.apache.avro.Schema.Field k = getFieldFromType("A", f, f.getDataType(), true);
    assertNotNull(k);
    assertNotNull(getFieldFromType("A", f, f.getDataType(), false));
  }

  @Test
  public void testDateType() {
    Field<java.sql.Date> f = DSL.date(new Date());
    org.apache.avro.Schema.Field k = getFieldFromType("A", f, f.getDataType(), true);
    assertNotNull(k);
    assertNotNull(getFieldFromType("A", f, f.getDataType(), false));
  }
  @Test
  public void testDateTime() {
    Field<LocalDateTime> f = DSL.localDateTime(LocalDateTime.now());
    org.apache.avro.Schema.Field k = getFieldFromType("A", f, f.getDataType(), true);
    assertNotNull(k);
    assertNotNull(getFieldFromType("A", f, f.getDataType(), false));
  }
  @Test
  public void testInterval() {
    DataType<YearToMonth> dt = DefaultDataType.getDataType(SQLDialect.H2, YearToMonth.class);
     Field<YearToMonth> f = DSL.field("A",  dt);
    org.apache.avro.Schema.Field k = getFieldFromType("A", f, f.getDataType(), true);
    assertNotNull(k);
    assertNotNull(getFieldFromType("A", f, f.getDataType(), false));
  }
  @Test
  public void testTime() {
     Field<Time> f = DSL.currentTime();
    org.apache.avro.Schema.Field k = getFieldFromType("A", f, f.getDataType(), true);
    assertNotNull(k);
    assertNotNull(getFieldFromType("A", f, f.getDataType(), false));
  }


}
