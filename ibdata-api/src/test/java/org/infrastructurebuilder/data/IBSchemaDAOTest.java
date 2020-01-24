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

import static org.junit.Assert.*;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.infrastructurebuilder.util.config.TestingPathSupplier;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class IBSchemaDAOTest {
  private final static TestingPathSupplier wps = new TestingPathSupplier();
  private static final String TESTFILE = "ibschematest.xml";

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private IBSchemaDAO d;
  private Map<String, IBDataStreamSupplier> map;
  private IBDataStreamSupplier dss;
  private IBDataStream ds;
  private Path path;

  @Before
  public void setUp() throws Exception {
    path= wps.getTestClasses().resolve(TESTFILE);
    ds = new FakeIBDataStream(path, Optional.empty());
    dss = new IBDataStreamSupplier() {
      @Override
      public IBDataStream get() {
        return ds;
      }
    };
    map = new HashMap<>();
    d = new IBSchemaDAO() {
      @Override
      public Map<String, IBDataStreamSupplier> get() {
        return map;
      }
    };
    map.put(d.getPrimaryAssetKeyName(), dss);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetSchema() {
    IBSchema s = d.getSchema();
    assertNotNull(s);
  }

  @Test
  public void testGetOriginalAssetKeyName() {
    assertFalse(d.getOriginalAssetKeyName().isPresent());
  }

  @Test
  public void testGetSource() {
    assertFalse(d.getSource().isPresent());
  }


}
