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

import static org.infrastructurebuilder.data.DefaultIBDataEngineSupplier.NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.infrastructurebuilder.util.core.TestingPathSupplier;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultIBDataEngineSupplierTest {
  public final static Logger log = LoggerFactory.getLogger(DefaultIBDataEngineSupplierTest.class);

  public final static TestingPathSupplier wps = new TestingPathSupplier();
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    Path tpath = wps.getTestClasses().resolve("test.jar");
    map = new HashMap<>();
    IBDataEngine value = new DefaultIBDataEngine(() -> log);
    map.put(NAME, value);
    d = new DefaultIBDataEngineSupplier(() -> log, map);

    v = (DefaultIBDataEngine) d.get();

    v.setAdditionalURLS(null);
    v.setAdditionalURLS(Arrays.asList(tpath.toUri().toURL()));
    assertNotNull(v);
    assertEquals(0, v.getAvailableIds().size());
    int qv = v.prepopulate();
    d.getLog().info("Got " + v.prepopulate());
    assertEquals(1,v.getAvailableIds().size());
    UUID i = v.getAvailableIds().get(0);
    q = v.fetchDataSetById(i);
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private static DefaultIBDataEngineSupplier d;
  private static Map<String, IBDataEngine> map;
  private static DefaultIBDataEngine v;
  private static Optional<IBDataSet> q;

  @Before
  public void setUp() throws Exception {

  }

  @After
  public void tearDown() throws Exception {
  }


  @Test
  public void testGetLocalId() {
    assertEquals(NAME, d.getLocalId());
  }

  @Test
  public void testGetAndEngine() {
    assertTrue(q.isPresent());
    assertFalse(v.fetchDataStreamById(UUID.randomUUID()).isPresent());
  }

  @Test(expected = IBDataException.class)
  public void testMetadataGet() {

    Map<String, String> patternMap = new HashMap<>();
    patternMap.put("/A", ".*");
    v.fetchDataStreamByMetadataPatternMatcherFromStrings(patternMap);
  }
}
