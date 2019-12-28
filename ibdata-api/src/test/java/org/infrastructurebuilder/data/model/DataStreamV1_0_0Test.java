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

import static org.junit.Assert.*;

import org.infrastructurebuilder.data.IBDataException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DataStreamV1_0_0Test {

  public static final String CHECKSUM = "3b2c63ccb53069e8b0472ba50053fcae7d1cc84ef774ff2b01c8a0658637901b7d91e71534243b5d29ee246e925efb985b4dbd7330ab1ab251d1e1b8848b9c49";

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private DataStream d;

  @Before
  public void setUp() throws Exception {
    d = new DataStream();
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test(expected =IBDataException.class)
  public void testGetChecksumUnavailable() {
    d.getChecksum();
  }
  @Test(expected =IBDataException.class)
  public void testGetChecksumBad() {
    int q= CHECKSUM.length();
    d.setSha512("ABCD");
    d.getChecksum();
  }
  @Test
  public void testGetChecksumAvailable() {
    d.setSha512(CHECKSUM);
    d.getChecksum();
    assertEquals(CHECKSUM, d.getChecksum().toString());
  }

}
