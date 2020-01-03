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

import static java.util.Optional.empty;
import static org.infrastructurebuilder.data.IBMetadataUtils.*;
import static org.infrastructurebuilder.data.IBMetadataUtils.toDataStream;
import static org.infrastructurebuilder.data.IBMetadataUtils.translateToXpp3Dom;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.infrastructurebuilder.data.model.DataStream;
import org.infrastructurebuilder.util.IBUtils;
import org.infrastructurebuilder.util.artifacts.Checksum;
import org.infrastructurebuilder.util.config.TestingPathSupplier;
import org.junit.Before;
import org.junit.Test;

public class IBMetadataUtilsTest {
  public static final String TEST_INPUT_0_11_XML_WO_SLASH = "test-input-0.11.xml";
  public static final String TEST_INPUT_0_11_XML = "/" + TEST_INPUT_0_11_XML_WO_SLASH;
  public static final String TEST_INPUT_0_11_XML_CHECKSUM = "4089182ee2abfa7873021088b08abadbe5b2226fde07c0d8497a5f40eeb8a1163caa9d4d3fec32467d4e63d7325a08e60d6f89c77f85d9d07f9ae1ba2a083a93";

  private static final String EMPTY_DOCUMENT_CHECKSUM = "62c884b6429dbc1910cc5a29a3024593e3d66a9e5c089766158f73c11c03de4d8f9b280457077f5e61a567d0837ab396178e9099d9c7c088db14c8dee750958b";
  private Xpp3Dom testDocument;
  private Xpp3Dom testDocument2;
  private final TestingPathSupplier wps = new TestingPathSupplier();

  @Before
  public void setUp() throws Exception {
    testDocument = emptyXpp3Supplier.get();
    testDocument2 = translateToXpp3Dom.apply(IBUtils.readFile(wps.getTestClasses().resolve(TEST_INPUT_0_11_XML_WO_SLASH)));
  }

  @Test
  public void test() {
    new IBMetadataUtils();
  }

  @Test
  public void testAsChecksum() {
    Checksum c = asChecksum.apply(testDocument);
    assertEquals(EMPTY_DOCUMENT_CHECKSUM, c.toString());
    assertEquals(TEST_INPUT_0_11_XML_CHECKSUM, asChecksum.apply(testDocument2).toString());
  }

  @Test
  public void testToDataStream() throws IOException {
    Path p = wps.get();
    Path v = p.resolve(UUID.randomUUID().toString());
    try (PrintWriter w = new PrintWriter(Files.newBufferedWriter(v))) {
      w.println("Hi!");
    }
    FakeIBDataStream d2 = new FakeIBDataStream(v, empty());
    assertEquals(128, EMPTY_DOCUMENT_CHECKSUM.length());
    d2.setSha512(EMPTY_DOCUMENT_CHECKSUM);
    Checksum q = d2.getChecksum();
    DataStream ds = toDataStream.apply(d2);
    assertNotNull(ds);
  }

}
