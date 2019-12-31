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

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DefaultSchemaQueryBeanTest {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private DefaultSchemaQueryBean d;

  @Before
  public void setUp() throws Exception {
    d = new DefaultSchemaQueryBean();
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testSetByLookup() {
    d.setByLookup("ABC");
    assertFalse(d.get().isPresent());
  }

  @Test
  public void testSetByUUID() {
    d.setByUUID("52ea2fe6-5467-34ec-bea9-c61454946c96");
    assertEquals("52ea2fe6-5467-34ec-bea9-c61454946c96", d.get().get().toString());
  }

}
