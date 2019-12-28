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
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class IBDataTypedStreamSupplierSupplierTest {

  private IBDataTypedStreamSupplierSupplier i;

  @Before
  public void setUp() throws Exception {
    i = new IBDataTypedStreamSupplierSupplier() {

      @Override
      public List<String> getRespondsToTypes() {
        return Arrays.asList("A","C");
      }

      @Override
      public Optional<IBDataSpecificStreamFactory> from(IBDataStream ds) {
        return null;
      }
    };
  }

  @Test
  public void testGetWeight() {
    assertEquals(0,i.getWeight().intValue());
  }

  @Test
  public void testRespondsTo() {
    assertTrue(i.respondsTo("A"));
    assertTrue(i.respondsTo("C"));
    assertFalse(i.respondsTo("B"));
  }

}
