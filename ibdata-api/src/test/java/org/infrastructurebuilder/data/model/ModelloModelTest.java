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
package org.infrastructurebuilder.data.model;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ModelloModelTest {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private DataSet dds;
  private DataStream dstr;

  @Before
  public void setUp() throws Exception {
    dds = new DataSet();
    dstr= new DataStream();
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testHashCode() {
    // Detects model change
    assertEquals(215078020,dds.hashCode());
    assertEquals(1094122300,dstr.hashCode());
  }

  @Test
  public void testClone() {
    DataSet sq = dds.clone();
    assertEquals(dds, sq);
    sq.addStream(dstr);
    assertEquals(sq.clone().getStreams().get(0), dstr);
  }

  @Test
  public void testDataSetDataSet() {
    dds.addStream(dstr);
    DataSet ds2 = new DataSet(dds);
    assertEquals(dds.clone(), ds2);
  }

  @Test
  public void testCompareTo() {
    assertEquals(0,dds.compareTo(dds.clone()));
  }

}
