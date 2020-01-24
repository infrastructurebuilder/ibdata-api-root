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

public class IBSchemaDAOSupplierTest {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private IBSchemaDAOSupplier i1, i2;

  @Before
  public void setUp() throws Exception {
    i1 = new IBSchemaDAOSupplier() {

      @Override
      public IBSchemaDAO get() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public String getTemporaryId() {
        return "1";
      }
    };
    i2 = new IBSchemaDAOSupplier() {

      @Override
      public IBSchemaDAO get() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public String getTemporaryId() {
        // TODO Auto-generated method stub
        return "2";
      }
    };
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void test() {
    assertTrue(i1.compareTo(i2) < 0);
  }

}
