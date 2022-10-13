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
package org.infrastructurebuilder.data.schema.liquibase.refactor;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.infrastructurebuilder.data.IBDataException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AddPrimaryKeyTest {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private AddPrimaryKey a;

  @Before
  public void setUp() throws Exception {
    a = new AddPrimaryKey("catalog", "schema", "table", "tableSpace", "constraintName", false, true, true);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test(expected = IBDataException.class)
  public void testAddPrimaryKeyFail() {
    assertEquals("A", a.get().toString());
  }
  @Test
  public void testAddPrimaryKey() {
    a = a.addColumns(Arrays.asList("A","B"));
    assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<addPrimaryKey catalogName=\"catalog\" tablespace=\"tableSpace\" columnNames=\"A,B\" clustered=\"false\" unique=\"true\" constraintName=\"constraintName\" schemaName=\"schema\" tableName=\"table\" validate=\"true\"/>", a.get().toString());
  }

}
