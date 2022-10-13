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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.infrastructurebuilder.data.IBDataAvroUtils;
import org.infrastructurebuilder.util.config.TestingPathSupplier;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GenericRecordMapProxyTest {

  public final static TestingPathSupplier wps = new TestingPathSupplier();

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private Schema schema;
  private GenericRecordBuilder b;
  private GenericRecord r;
  private GenericRecordMapProxy p;

  @Before
  public void setUp() throws Exception {
    schema = IBDataAvroUtils.avroSchemaFromString
        .apply(wps.getTestClasses().resolve("ba.avsc").toAbsolutePath().toString());
    b = new GenericRecordBuilder(schema);
    r = new GenericData.Record(schema);
    p = new GenericRecordMapProxy(r);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGenericRecordMapProxy() {
    assertNotNull(p);
  }

  @Test
  public void testEmptySchema() throws IOException {
    Path p = wps.getTestClasses().resolve("baempty.avsc");

    try (InputStream ins = Files.newInputStream(p)) {
      Schema empty = new Schema.Parser().parse(ins);
      GenericRecord eb = new GenericRecordBuilder(empty).build();
      GenericRecordMapProxy e = new GenericRecordMapProxy(eb);
      assertTrue(e.isEmpty());
    }

  }

  @Test
  public void testGet() {
    assertEquals(r, p.get());
    assertNull(p.get("NOPE!"));
  }

  @Test
  public void testSize() {
    assertEquals(8, p.size());
  }

  @Test
  public void testIsEmpty() {
    assertFalse(p.isEmpty());
  }

  @Test
  public void testContainsKey() {
    assertTrue(p.containsKey("age"));
    assertFalse(p.containsKey("jeff"));
  }

  @Test
  public void testContainsValue() {
    assertFalse(p.containsValue("jeff"));
  }

  @Test
  public void testGetObject() {
    assertNull(p.get("age"));
  }

  @Test
  public void testPut() {
    p.put("country", null);
    p.put("age", 10);
    assertEquals(10, p.get("age"));
    assertNull(p.put("NOSUCHFIELD", "anything"));
  }

  @Test
  public void testRemove() {
    p.remove("country"); // does nothing
  }

  @Test
  public void testPutAll() {
    Map<String, Object> a = new HashMap<>();
    p.putAll(a);
    a.put("age", 10);
    p.putAll(a);
    assertEquals(10, p.get("age"));
  }

  @Test
  public void testClear() {
    p.clear();
  }

  @Test
  public void testKeySet() {
    assertEquals(8, p.keySet().size());
  }

  @Test
  public void testValues() {
    assertEquals(8, p.values().size());
  }

  @Test
  public void testEntrySet() {
    assertEquals(8, p.entrySet().size());
  }

}
