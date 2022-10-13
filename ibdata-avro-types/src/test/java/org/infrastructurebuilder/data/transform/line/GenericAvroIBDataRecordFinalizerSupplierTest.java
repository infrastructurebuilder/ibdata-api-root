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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData.Record;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.apache.avro.generic.GenericRecord;
import org.infrastructurebuilder.data.DefaultAvroGenericRecordStreamSupplier;
import org.infrastructurebuilder.data.DefaultAvroGenericRecordStreamSupplierTest;
import org.infrastructurebuilder.data.DefaultIBDataStream;
import org.infrastructurebuilder.data.IBDataAvroUtils;
import org.infrastructurebuilder.data.IBDataDataStreamRecordFinalizerSupplier;
import org.infrastructurebuilder.data.IBDataStreamRecordFinalizer;
import org.infrastructurebuilder.data.IBDataStructuredDataFieldMetadata;
import org.infrastructurebuilder.data.IBDataStructuredDataMetadata;
import org.infrastructurebuilder.data.model.DataStream;
import org.infrastructurebuilder.data.transform.BA;
import org.infrastructurebuilder.data.transform.line.GenericAvroIBDataRecordFinalizerSupplier.GenericAvroIBDataStreamRecordFinalizer;
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

public class GenericAvroIBDataRecordFinalizerSupplierTest {

  public final static Logger log = LoggerFactory.getLogger(GenericAvroIBDataRecordFinalizerSupplierTest.class);
  private final static TestingPathSupplier wps = new TestingPathSupplier();

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    wps.finalize();
  }

  private GenericAvroIBDataRecordFinalizerSupplier g;
  private ConfigMapSupplier cms;
  private GenericRecord record;
  private Schema schema;

  @Before
  public void setUp() throws Exception {
    cms = new DefaultConfigMapSupplier();
    String schemaString = wps.getTestClasses().resolve("ba.avsc").toAbsolutePath().toString();
    cms.addValue(DefaultMapToGenericRecordIBDataLineTransformerSupplier.SCHEMA_PARAM, schemaString);
    cms.addValue(IBDataStreamRecordFinalizer.NUMBER_OF_ROWS_TO_SKIP_PARAM, "1");
    g = new GenericAvroIBDataRecordFinalizerSupplier(wps, () -> log);
    schema = IBDataAvroUtils.avroSchemaFromString.apply(schemaString);
    record = new Record(schema);

  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testConfigure() {
    IBDataDataStreamRecordFinalizerSupplier<GenericRecord> h = g.configure(cms);
    assertFalse(g == h);
  }

  @Test
  public void testGet() {
    IBDataStreamRecordFinalizer<GenericRecord> q = g.configure(cms).get();
    assertEquals(1, q.getNumberOfRowsToSkip());
    q.writeRecord(record);
  }

  @Test
  public void testAccreteDate() throws Exception {
    List<GenericRecord> l = new ArrayList<>();
    DataStream id = new DataStream();
    id.setUuid(UUID.randomUUID().toString());
    id.setCreationDate(new Date());
    id.setSha512(DefaultAvroGenericRecordStreamSupplierTest.CHECKSUM);
    id.setMetadata(new Xpp3Dom("metadata"));
    DefaultIBDataStream identifier = new DefaultIBDataStream(id, wps.getTestClasses().resolve("ba.avro"));
    DefaultAvroGenericRecordStreamSupplier d = new DefaultAvroGenericRecordStreamSupplier();
    Optional<Stream<GenericRecord>> q = d.from(identifier);
    assertTrue(q.isPresent());
    List<GenericRecord> w = q.get().collect(Collectors.toList());
    GenericAvroIBDataStreamRecordFinalizer f = (GenericAvroIBDataStreamRecordFinalizer) g.configure(cms).get();

    w.forEach(record -> {
      f.writeRecord(record);
    });
    f.close();

    Map<Integer, ? extends IBDataStructuredDataFieldMetadata> smd = f.getStructuredMetadata().get().getFieldMap();
    assertEquals(8, smd.size());
    assertFalse(smd.get(5).isNullable().get());
    assertFalse(smd.get(3).isEnumeration());
    assertEquals(13, smd.get(4).getMaxIntValue().get().intValue());
    assertEquals(6, smd.get(4).getMinIntValue().get().intValue());
  }
}
