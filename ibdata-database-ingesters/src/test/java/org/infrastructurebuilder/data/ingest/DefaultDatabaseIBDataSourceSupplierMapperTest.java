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
package org.infrastructurebuilder.data.ingest;

import static org.infrastructurebuilder.data.DefaultAvroGenericRecordStreamSupplier.genericStreamFromInputStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.avro.LogicalType;
import org.apache.avro.Schema.Field;
import org.apache.avro.data.TimeConversions.DateConversion;
import org.apache.avro.generic.GenericRecord;
import org.codehaus.plexus.configuration.xml.XmlPlexusConfiguration;
import org.infrastructurebuilder.util.constants.IBConstants;
import org.infrastructurebuilder.data.IBDataConstants;
import org.infrastructurebuilder.data.IBDataSource;
import org.infrastructurebuilder.data.IBDataSourceSupplier;
import org.infrastructurebuilder.data.util.files.DefaultTypeToExtensionMapper;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.TestingPathSupplier;
import org.infrastructurebuilder.util.files.IBChecksumPathType;
import org.infrastructurebuilder.util.files.TypeToExtensionMapper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultDatabaseIBDataSourceSupplierMapperTest {
  public final static Logger log = LoggerFactory.getLogger(DefaultDatabaseIBDataSourceSupplierMapperTest.class);

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

  private DefaultDatabaseIBDataSourceSupplierMapper d;

  private String theUrl;

  private DefaultIBDataStreamIdentifierConfigBean b;

  private IBDataSourceSupplier s;

  private IBDataSource ds;

  @Before
  public void setUp() throws Exception {
    c = new ConfigMap();
    c.put(DefaultDatabaseIBDataSourceSupplierMapper.DIALECT, "H2");
    theUrl = "jdbc:h2:" + wps.getTestClasses().resolve("test").toAbsolutePath().toString();
    c.put("url", theUrl);
    c.put("query", "SELECT * FROM TEST ORDER BY ID;");
    c.put(IBDataConstants.DATE_FORMATTER, "yyyy-MM-dd");
    t2e = new DefaultTypeToExtensionMapper();
    d = new DefaultDatabaseIBDataSourceSupplierMapper(() -> log, t2e, wps);
    b = new DefaultIBDataStreamIdentifierConfigBean();
    b.setDescription("desc");
    b.setId(UUID.randomUUID().toString());
    b.setMetadata(new XmlPlexusConfiguration("metadata"));
    b.setMimeType(IBConstants.TEXT_CSV); // obviously wrong
    b.setName("name");
    b.setPath(null);
    b.setSha512(null);
    b.setUrl(theUrl);
  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void test() throws MalformedURLException {
    assertTrue(d.respondsTo(b));
    s = d.getSupplierFor(b.getTemporaryId(), b);
    ds = s.get().withAdditionalConfig(c);
    List<IBChecksumPathType> p = ds.get();
    assertTrue(p.size() > 0);
    Optional<Stream<GenericRecord>> x = genericStreamFromInputStream.apply(p.get(0).get());
    assertTrue(x.isPresent());
    Stream<GenericRecord> theStream = x.get();
    assertEquals(1, theStream.count());
    GenericRecord theRecord = genericStreamFromInputStream.apply(p.get(0).get()).get().collect(Collectors.toList())
        .get(0);
    assertEquals(1, theRecord.get("ID"));
    Field f = theRecord.getSchema().getField("BIRTHDAY");
    LogicalType t = f.schema().getLogicalType();
    Object k = theRecord.get("BIRTHDAY");
    LocalDate conv = new DateConversion().fromInt((Integer) k, f.schema(), t);
    // 2019-10-22
    Date d = new Date(new Integer((int) k).longValue());
    assertEquals("2019-10-22", conv.toString());

  }

}
