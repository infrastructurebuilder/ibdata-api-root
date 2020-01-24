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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.infrastructurebuilder.IBConstants;
import org.infrastructurebuilder.data.model.IndexField;
import org.infrastructurebuilder.data.model.PersistedIBSchema;
import org.infrastructurebuilder.data.model.SchemaField;
import org.infrastructurebuilder.data.model.SchemaIndex;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class IBSchemaTest {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private PersistedIBSchema i1, i2;
  private List<SchemaField> fields = new ArrayList<>();
  private List<SchemaIndex> indexes = new ArrayList<>();
  private SchemaField field1;
  private SchemaIndex index1;
  private List<IndexField> iFields = new ArrayList<>();
  private IndexField iField1;

  @Before
  public void setUp() throws Exception {
    field1 = new SchemaField();
    field1.setIndex(0);
    iField1 = new IndexField();
    iField1.setIndex(0);
    iField1.setType(IBFieldIndexType.DEFAULT.name());
    index1 = new SchemaIndex();
    index1.setName("index1");
    index1.setFields(iFields);
    index1.setDeprecated(false);
    index1.setDescription("desc");
    index1.setType(IBDataIndexingType.DEFAULT.name());
    index1.setUnique(true);
    index1.setVersionAppeared("1.0");
    fields.add(field1);
    indexes.add(index1);

    i1 = new PersistedIBSchema();
    i1.setUrl("url");
    i1.setName("name");
    i1.setNameSpace("nameSpace");
    i1.setMimeType(IBConstants.TEXT_PLAIN);
    i1.setFields(fields);
    i1.setIndexes(indexes);
    i1.setUuid(i1.asChecksum().get().get().toString());
    i2 = new PersistedIBSchema();
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testAsChecksum() {
    assertNotNull(i1.asChecksum());
  }

  @Test
  public void testGetUuid() {
    assertNotNull(i1.getUuid());
  }

  @Test
  public void testGetTemporaryId() {
    assertFalse(i1.getTemporaryId().isPresent());
    i1.setTemporaryId("ID");
    assertEquals(i1.getTemporaryId().get(), "ID");
  }

  @Test
  public void testGetCreationDate() {
    assertNotNull(i1.getCreationDate());
  }

  @Test
  public void testGetDescription() {
    assertFalse(i1.getDescription().isPresent());
    i1.setDescription("D");
    assertEquals("D", i1.getDescription().get());
  }

  @Test
  public void testGetSchemaFields() {
    List<SchemaField> feilds = i1.getFields();
    assertEquals(1, feilds.size());
  }

  @Test
  public void testGetSchemaIndexes() {
    List<SchemaIndex> i = i1.getIndexes();
    assertEquals(1, i.size());
  }

  @Test
  public void testGetMetadata() {
    assertNotNull(i1.getMetadata());
  }

  @Test
  public void testGetNameSpace() {
    assertEquals("nameSpace", i1.getNameSpace().get());
  }

  @Test
  public void testGetName() {
    assertEquals("name", i1.getName().get());
  }

  @Test
  public void testGetMimeType() {
    assertEquals(IBConstants.TEXT_PLAIN, i1.getMimeType());
  }

  @Test
  public void testGetFieldForIndex() {
    Optional<IBField> a = i1.getFieldForIndex(0);
    assertTrue(a.isPresent());
  }

  @Test
  public void testCompareToIBSchema() {
    assertFalse(i1.compareTo(i2) == 0);
  }

  @Test
  public void testGetUrl() {
    assertEquals("url", i1.getUrl().get());
  }

}
