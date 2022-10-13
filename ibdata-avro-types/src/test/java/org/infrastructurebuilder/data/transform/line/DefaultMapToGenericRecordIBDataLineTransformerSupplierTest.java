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
package org.infrastructurebuilder.data.transform.line;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.file.SeekableByteArrayInput;
import org.apache.avro.file.SeekableInput;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.IndexedRecord;
import org.apache.avro.generic.MapProxyGenericData;
import org.infrastructurebuilder.data.Formatters;
import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.data.transform.line.DefaultMapToGenericRecordIBDataLineTransformerSupplier.DefaultMapSSToGenericRecordIBDataLineTransformer;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.config.DefaultConfigMapSupplier;
import org.infrastructurebuilder.util.config.TestingPathSupplier;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultMapToGenericRecordIBDataLineTransformerSupplierTest {
  public final static Logger log = LoggerFactory
      .getLogger(DefaultMapToGenericRecordIBDataLineTransformerSupplierTest.class);

  public static final String BA = "ba";
  private final static TestingPathSupplier wps = new TestingPathSupplier();

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    wps.finalize();
  }

  private Path wp;
  private DefaultMapToGenericRecordIBDataLineTransformerSupplier s;
  private ConfigMapSupplier cms;
  private ConfigMap cm;
  private Path schemaFile;

  @Before
  public void setUp() throws Exception {
    schemaFile = wps.getTestClasses().resolve(BA + ".avsc").toAbsolutePath();
    cm = new ConfigMap();
    cm.put(DefaultMapToGenericRecordIBDataLineTransformerSupplier.SCHEMA_PARAM, schemaFile.toString());
    wp = wps.get();
    cms = new DefaultConfigMapSupplier().addConfiguration(cm);
    s = new DefaultMapToGenericRecordIBDataLineTransformerSupplier(wps, () -> log);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetHint() {
    assertEquals(DefaultMapToGenericRecordIBDataLineTransformerSupplier.NAME, s.getHint());
  }

  @Test
  public void testConfigureConfigMapSupplier() throws IOException {
    AbstractIBDataRecordTransformerSupplier<Map<String, Object>, IndexedRecord> v = s.configure(cms);
    assertFalse(v == s);

    DefaultMapSSToGenericRecordIBDataLineTransformer q = (DefaultMapSSToGenericRecordIBDataLineTransformer) v.get();

    assertEquals(DefaultMapToGenericRecordIBDataLineTransformerSupplier.NAME, q.getHint());
    assertTrue(q.accepts().get().contains(Map.class));
    assertEquals(IndexedRecord.class, q.produces().get());
    assertEquals(IndexedRecord.class, q.getOutboundClass());
    assertEquals(Map.class, q.getInboundClass());
    Formatters f = q.getFormatters();
    GenericData gd = new MapProxyGenericData(f);

    assertTrue(f.isBlankFieldNullInUnion());
    assertEquals(BA.toUpperCase(), q.getSchema().getName());
    assertEquals(Locale.getDefault(), q.getLocale());
    assertNotNull(f.getTimeFormatter());
    assertNotNull(f.getDateFormatter());
    assertNotNull(f.getTimestampFormatter());

    Map<String, Object> m = new HashMap<>();
    m.put("index", 7);
    m.put("last_name", "alvis");
    m.put("first_name", "mkel");
    m.put("country", "USA");
    m.put("date_of_birth", "10-15-07");
    m.put("id", "3598");
    m.put("gender", "F");
    m.put("age", 34);

    GenericRecord datum = (GenericRecord) q.apply(m);

    Integer d = 13801;
    Schema schema = q.getSchema();
    DataFileWriter<GenericRecord> w = new DataFileWriter<GenericRecord>(
        new GenericDatumWriter<GenericRecord>(schema, gd));
    ByteArrayOutputStream outs = new ByteArrayOutputStream(5000);
    w.create(schema, outs);
    w.append(datum);
    w.close();

    SeekableInput sin = new SeekableByteArrayInput(outs.toByteArray());

    DataFileReader<GenericRecord> r = new DataFileReader<>(sin, new GenericDatumReader<>(schema));

    GenericRecord newDatum = r.next(); // We wrote one record
    r.close();
    LocalDate d2 = LocalDate.ofEpochDay(new Integer(d).longValue());
    assertNotNull(d2);
    assertEquals(d, newDatum.get("date_of_birth"));
  }

  @Test(expected = IBDataException.class)
  public void testConfigureConfigMapSupplierNoSchema() {
    AbstractIBDataRecordTransformerSupplier<Map<String, Object>, IndexedRecord> v = s
        .configure(new DefaultConfigMapSupplier());
    assertFalse(v == s);
    v.get();
  }

}
