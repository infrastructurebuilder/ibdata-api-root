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

import static java.util.stream.Collectors.toList;
import static org.infrastructurebuilder.data.IBMetadataUtilsTest.TEST_INPUT_0_11_XML;
import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.infrastructurebuilder.data.model.DataSet;
import org.infrastructurebuilder.util.artifacts.Checksum;
import org.junit.Before;
import org.junit.Test;

public class IBDataSetTest extends AbstractModelTest {

  private IBDataSet ds;
  private List<String> idList = Arrays.asList("ABC");

  @Before
  public void setUp() throws Exception {
    super.setUp();
    try (InputStream t = getClass().getResourceAsStream(TEST_INPUT_0_11_XML);) {
      DataSet dsRead = IBDataModelUtils.mapInputStreamToDataSet.apply(t);
      dsRead.setPath(ibd.toAbsolutePath().getParent().toString());
      ds = new FakeIBDataSet(dsRead, o11Paths);
      assertEquals(2, ds.getStreamSuppliers().size());
    }
  }

  @Test
  public void testGetStreamSuppliers() {
    assertEquals("183d3030-6dae-4f33-acde-79eacbaa8c2d", ds.getStreamSuppliers().get(0).get().getId().toString());
  }

  @Test
  public void testGetDataChecksum() {
    assertEquals("a08d6fa3-fa41-303e-9488-b28634b5d30c", ds.getDataChecksum().get().asUUID().get().toString());
  }

  @Test
  public void testGetDataSetMetadataChecksum() {
    assertEquals("5ced8242-13d1-3fb2-bdfa-83c68f3babe5", ds.getDataSetMetadataChecksum().asUUID().get().toString());
  }

  @Test
  public void testAsChecksumType() {
    Checksum v = ds.asChecksumType().getChecksum();
    assertEquals(new Checksum(), v);
  }

  @Test
  public void testAsStreamsList() {
    List<String> k = ds.asStreamsList().stream().map(IBDataStream::getId).map(UUID::toString).collect(toList());
    List<String> expected = Arrays.asList("183d3030-6dae-4f33-acde-79eacbaa8c2d",
        "0dfb7bc9-73aa-4f7e-b735-cbccfa052733");
    assertEquals(expected, k);
  }

  @Test
  public void testGetGAV() {
    assertEquals(gav, ds.getGAV());
  }

}
