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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class IBDataStructuredDataMetadataTest {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private IBDataStructuredDataMetadata l;
  private List<IBDataStructuredDataFieldMetadata> fields = new ArrayList<IBDataStructuredDataFieldMetadata>();
  private String i;
  private String k = UUID.randomUUID().toString();

  @Before
  public void setUp() throws Exception {
    i = null;
    l = new IBDataStructuredDataMetadata() {
      @Override
      public String getUuid() {
        return i;
      }

      @Override
      public List<? extends IBDataStructuredDataFieldMetadata> getFields() {
        return fields;
      }

    };
  }

  @Test
  public void test() {
    assertEquals(Optional.empty(), l.getId());
    i = k;
    assertEquals(k, l.getId().get().toString());
    assertEquals(0, l.getFieldMap().size());
  }

}
