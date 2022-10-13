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

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.infrastructurebuilder.util.core.TestingPathSupplier;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class DefaultIBTypedDataStreamSupplierTest {

  public final static TestingPathSupplier wps = new TestingPathSupplier();
  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private IBTypedDataStreamSupplier<String> s;
  private IBDataStream original;
  private List<String> list;

  @Before
  public void setUp() throws Exception {
    Path p = wps.getTestClasses().resolve("lines.txt");
    original = new FakeIBDataStream(p, Optional.empty(), Optional.empty());
    list = Arrays.asList("A", "B", "C");
    s = new DefaultIBTypedDataStreamSupplier<>(original, list.iterator());
  }

  @Test
  public void testGet() {
    List<String> k = s.get().collect(Collectors.toList());
    assertEquals(list, k);
  }

}
