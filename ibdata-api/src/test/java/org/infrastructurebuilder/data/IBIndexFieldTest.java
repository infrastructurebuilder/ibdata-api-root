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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class IBIndexFieldTest {

  private static final String DEFAULT_CHECKSUM = "d2f30f1f4c2064d7248c52a3532e47e4f56bcc6070518ce7c3bb415eba273cd9e092a161acc204f3c8828ecc6011f2c1147b02fcb87c150d7b304bd889e60e92";

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private IBIndexField i0;
  private IBIndexField i1;

  @Before
  public void setUp() throws Exception {
    i0 = new IBIndexField() {

      @Override
      public int getIndex() {
        return 0;
      }

      @Override
      public String getType() {
        return IBFieldIndexType.ASCENDING.name();
      }

    };
    i1 = new IBIndexField() {

      @Override
      public int getIndex() {
        return 1;
      }

      @Override
      public String getType() {
        return "nonesuch";
      }
    };
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testAsChecksum() {
    assertEquals(DEFAULT_CHECKSUM, i0.asChecksum().toString());
  }

  @Test
  public void testGetIndex() {
    assertEquals(0, i0.getIndex());
  }

  @Test
  public void testGetType() {
    assertEquals(IBFieldIndexType.ASCENDING.name(), i0.getType());
  }

  @Test
  public void testGetIndexFieldType() {
    assertEquals(IBFieldIndexType.ASCENDING, i0.getIndexFieldType().get());
    assertFalse(i1.getIndexFieldType().isPresent());
  }

  @Test
  public void testCompareToIBIndexField() {
    assertTrue(i0.compareTo(i1) < 0);
  }

}
