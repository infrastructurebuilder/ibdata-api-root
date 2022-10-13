/*
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
package org.infrastructurebuilder.data.transform.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;

import org.infrastructurebuilder.data.IBDataConstants;
import org.infrastructurebuilder.data.transform.base.IBTransformation;
import org.infrastructurebuilder.data.transform.base.Record;
import org.infrastructurebuilder.data.transform.base.RecordTransformer;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.config.DefaultConfigMapSupplier;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RecordTransformerTest {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private RecordTransformer r;
  private List<Record> records;
  private Record record1;
  private Record record2;

  @Before
  public void setUp() throws Exception {
    r = new RecordTransformer();
    record1 = new Record();
    record2 = new Record();
    records = Arrays.asList(record1, record2);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetConfigurationAsConfigMapSupplier() {
    r.setHint("X");
    r.setId("Y");
    record1.setId("1");
    record2.setId("2");
    r.setRecords(records);
    ConfigMapSupplier q = r.getConfigurationAsConfigMapSupplier(new DefaultConfigMapSupplier());
    ConfigMap q1 = q.get();
    assertEquals(1, q1.size());
    assertEquals("1|1,2|2", q1.get(IBDataConstants.TRANSFORMERSLIST).toString());
  }

  @Test
  public void testSetRecords() {
    r.setRecords(records);
    assertEquals(Arrays.asList(record1, record2), r.getRecords());
  }

  @Test
  public void testSetRecordFinalizer() {
    assertNull(r.getRecordFinalizer());
    r.setRecordFinalizer("A");
    assertEquals("A", r.getRecordFinalizer());
    r.setRecordFinalizerConfig(new ConfigMap());
    assertNotNull(r.getRecordFinalizerConfig(new DefaultConfigMapSupplier()));
  }

  @Test
  public void testCopyTransformation() {
    r.setRecords(records);
    assertNull(r.getTransformation());
    IBTransformation t = new FakeIBTransformation();
    RecordTransformer q = r.copy(t);
    assertEquals(t, q.getTransformation());
  }

}
