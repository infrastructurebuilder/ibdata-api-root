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

import java.util.UUID;

import org.infrastructurebuilder.util.artifacts.Checksum;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class IBDataSchemaAssetTest {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private IBDataSchemaAsset a, b;

  @Before
  public void setUp() throws Exception {
    a = new IBDataSchemaAsset() {
      @Override
      public String getId() {
        return "id";
      }

      @Override
      public String getSha512() {
        return new Checksum("ABCD").toString();
      }
    };
    b= new IBDataSchemaAsset() {
      @Override
      public String getId() {
        return "id2";
      }

      @Override
      public String getSha512() {
        return new Checksum("abcd").toString();
      }
    };
  }

  @After
  public void tearDown() throws Exception {
  }


  @Test
  public void testGetId() {
    assertEquals("id", a.getId());
  }

  @Test
  public void testGetAssetUUID() {
    UUID c = a.getAssetUUID();
    assertEquals(UUID.fromString("7838496f-d058-3421-bbb5-00bb6f472f13"), c);
  }

  @Test
  public void testGetSha512() {
    assertEquals("abcd", a.asChecksum().toString());
  }

  @Test(expected = IBDataException.class)
  public void testGet() {
    a.get();
  }

}
