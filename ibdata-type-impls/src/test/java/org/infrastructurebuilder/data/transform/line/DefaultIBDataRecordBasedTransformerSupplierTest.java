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
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.infrastructurebuilder.data.IBDataStreamRecordFinalizer;
import org.infrastructurebuilder.data.transform.base.IBDataTransformer;
import org.infrastructurebuilder.data.transform.base.line.AbstractIBDataRecordBasedTransformer;
import org.infrastructurebuilder.data.transform.line.DefaultTestIBDataRecordTransformerSupplierStringToString.StringToStringRecordTransformer;
import org.infrastructurebuilder.util.config.DefaultConfigMapSupplier;
import org.infrastructurebuilder.util.core.TestingPathSupplier;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultIBDataRecordBasedTransformerSupplierTest {
  public final static Logger log = LoggerFactory.getLogger(DefaultIBDataRecordBasedTransformerSupplierTest.class);

  public final static TestingPathSupplier wps = new TestingPathSupplier();

  private static final String NAME = StringToStringRecordTransformer.class.getCanonicalName();

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private Map<String, IBDataRecordTransformerSupplier> dataLineTransformerSuppliers;

  private DefaultIBDataRecordBasedTransformerSupplier s;

  private DefaultConfigMapSupplier cms;

  private IBDataStreamRecordFinalizer finalizer;

  private IBDataRecordTransformerSupplier drts;

  @Before
  public void setUp() throws Exception {
    dataLineTransformerSuppliers = new HashMap<>();
    drts = new DefaultTestIBDataRecordTransformerSupplierStringToString(wps, cms, () -> log);
    dataLineTransformerSuppliers.put(NAME, drts);
    cms = new DefaultConfigMapSupplier();
    s = new DefaultIBDataRecordBasedTransformerSupplier(wps, () -> log , dataLineTransformerSuppliers);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetDataLineSuppliers() {
    AbstractIBDataRecordBasedTransformer p = (AbstractIBDataRecordBasedTransformer) s.withFinalizer(finalizer).configure(cms).get();
    Map<String, IBDataRecordTransformerSupplier> q = p.getDataLineSuppliers();
    assertEquals(1,q.size());
  }
  @Test
  public void testGet() {
    IBDataTransformer p = s.withFinalizer(finalizer).configure(cms).get();
    assertNotNull(p);
    assertEquals(DefaultIBDataRecordBasedTransformerSupplier.RECORD_BASED_TRANSFORMER_SUPPLIER,p.getHint());
  }

}
