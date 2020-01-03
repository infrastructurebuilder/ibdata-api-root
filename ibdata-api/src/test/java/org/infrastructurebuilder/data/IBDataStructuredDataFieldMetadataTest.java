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

import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class IBDataStructuredDataFieldMetadataTest {

  private IBDataStructuredDataFieldMetadata k;

  @Before
  public void setUp() throws Exception {
    k = new IBDataStructuredDataFieldMetadata() {

      private IBDataStructuredDataMetadataType t;

      @Override
      public int getIndex() {
        return 0;
      }

      @Override
      public List<String> getEnumerations() {
        return asList("A", "B");
      }

      @Override
      public String getMinAsStringValue() {
        return null;
      }

      @Override
      public String getMaxAsStringValue() {
        return null;
      }

      @Override
      public Optional<String> getName() {
        return Optional.empty();
      }

      @Override
      public Optional<Boolean> isNullable() {
        return Optional.empty();
      }

      @Override
      public Optional<IBDataStructuredDataMetadataType> getType() {
        return Optional.empty();
      }

    };
  }

  @Test
  public void testDefaults() {
    assertEquals(0, k.getIndex());
    assertEquals(asList("A", "B"), k.getEnumerations());
    assertTrue(k.isEnumeration());
    assertFalse(k.getMaxIntValue().isPresent());
    assertFalse(k.getMinIntValue().isPresent());
    assertEquals(empty(), k.getUniqueValuesCount());
    assertFalse(k.isNullable().isPresent());
    assertFalse(k.getMaxRealValue().isPresent());
    assertFalse(k.getMinRealValue().isPresent());
    assertEquals(empty(), k.getType());
  }

}
