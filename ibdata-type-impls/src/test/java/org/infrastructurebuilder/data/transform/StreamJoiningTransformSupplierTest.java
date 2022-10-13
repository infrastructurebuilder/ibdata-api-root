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
package org.infrastructurebuilder.data.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Transformer;

import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.data.IBDataSet;
import org.infrastructurebuilder.data.IBDataStream;
import org.infrastructurebuilder.data.IBDataStreamRecordFinalizer;
import org.infrastructurebuilder.data.transform.base.IBDataTransformationResult;
import org.infrastructurebuilder.data.transform.base.IBDataTransformer;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.config.DefaultConfigMapSupplier;
import org.infrastructurebuilder.util.core.TestingPathSupplier;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamJoiningTransformSupplierTest {
  final static Logger log = LoggerFactory.getLogger(StreamJoiningTransformSupplierTest.class);

  final static TestingPathSupplier wps = new TestingPathSupplier();

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private StreamJoiningTransformSupplier s;

  private IBDataStreamRecordFinalizer<?> ts2;

  private ConfigMapSupplier cms;

  private List<IBDataStream> suppliedStreams = new ArrayList<>();

  private IBDataSet ds;

  private Transformer p;

  @Before
  public void setUp() throws Exception {
    cms = new DefaultConfigMapSupplier();
    s = new StreamJoiningTransformSupplier(wps, () -> log);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test(expected = IBDataException.class)
  public void testGet() {
    IBDataTransformer t = s.withFinalizer(ts2).configure(cms).get();
    assertEquals(StreamJoiningTransformSupplier.STREAM_JOIN, t.getHint());
    assertNotNull(t);
    IBDataTransformationResult q = t/* .configure(cms.get()) */.transform(p, ds, suppliedStreams, true);
    IBDataSet i = q.get().get();
  }

}
