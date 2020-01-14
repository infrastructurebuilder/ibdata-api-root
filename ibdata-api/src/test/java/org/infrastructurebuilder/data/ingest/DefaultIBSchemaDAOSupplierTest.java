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

import static org.infrastructurebuilder.IBConstants.DEFAULT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.Map;

import org.infrastructurebuilder.data.IBDataStreamSupplier;
import org.infrastructurebuilder.data.IBSchema;
import org.infrastructurebuilder.data.IBSchemaDAO;
import org.infrastructurebuilder.data.IBSchemaSource;
import org.infrastructurebuilder.data.model.PersistedIBSchema;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DefaultIBSchemaDAOSupplierTest {

  private static final String ID = "id";

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private IBSchemaDAO dao;
  private String tempId;
  private DefaultIBSchemaDAOSupplier s;
  private IBSchemaSource<?> src;
  private Map<String, IBDataStreamSupplier> map;
  private IBSchema schema;

  @Before
  public void setUp() throws Exception {
    tempId = ID;
    schema = new PersistedIBSchema().clone();
    map = new HashMap<>();
    src = new FakeIBSchemaSource<Object>();
    dao = new DefaultIBSchemaDAO(schema, map, src);
    s = new DefaultIBSchemaDAOSupplier(tempId, dao);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGet() {
    assertEquals(s.get(), dao);
  }
  @Test
  public void testId() {
    assertEquals(ID, s.getTemporaryId());
  }

  @Test
  public void testDAOStuff() {
    DefaultIBSchemaDAO d = (DefaultIBSchemaDAO) s.get();
    assertEquals(0,d.get().size());
    assertFalse(d.getOriginalAssetKeyName().isPresent());
    assertEquals(DEFAULT, d.getPrimaryAssetKeyName());
    assertEquals(schema, d.getSchema());
  }

}
