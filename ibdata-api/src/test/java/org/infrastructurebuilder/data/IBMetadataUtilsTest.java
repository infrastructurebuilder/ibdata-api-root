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
import static org.infrastructurebuilder.data.IBMetadataUtils.asChecksum;
import static org.infrastructurebuilder.data.IBMetadataUtils.builderSupplier;
import static org.infrastructurebuilder.data.IBMetadataUtils.emptyDocumentSupplier;
import static org.infrastructurebuilder.data.IBMetadataUtils.toDataStream;
import static org.infrastructurebuilder.data.IBMetadataUtils.translateToXpp3Dom;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.infrastructurebuilder.data.model.DataStream;
import org.infrastructurebuilder.util.artifacts.Checksum;
import org.infrastructurebuilder.util.config.TestingPathSupplier;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class IBMetadataUtilsTest {
  public static final String TEST_INPUT_0_11_XML_WO_SLASH = "test-input-0.11.xml";
  public static final String TEST_INPUT_0_11_XML = "/" + TEST_INPUT_0_11_XML_WO_SLASH;
  public static final String TEST_INPUT_0_11_XML_CHECKSUM = "a0861598d78c33fb6c954cb11c387baa3f161395ee989d39199e4df85e937fd6ba8fc2f843d766027ae8388584a3055ca917e501ac09c2838323feb625969837";

  private static final String EMPTY_DOCUMENT_CHECKSUM = "e6bd3955a95d3bdcd94c4da7b5f83d7ed9776499b214e84b4d32f112660155f11a4ab976f1a6ea8c0e8a8132b5e4c86a0260a9e1d4a7b3956588e5f5b24238f2";
  private Document testDocument;
  private Document testDocument2;
  private final TestingPathSupplier wps = new TestingPathSupplier();

  @Before
  public void setUp() throws Exception {
    testDocument = emptyDocumentSupplier.get();
    InputStream is = getClass().getResourceAsStream(TEST_INPUT_0_11_XML);
    testDocument2 = builderSupplier.get().parse(is);
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
    d2.setSha512(EMPTY_DOCUMENT_CHECKSUM);
    DataStream ds = toDataStream.apply(d2);
    assertNotNull(ds);
  }

  @Test
  public void testToXpp3Dom() throws SAXException, IOException {
    String a = "<x>y</x>";
    assertNotNull(translateToXpp3Dom.apply(a));
    assertNotNull(translateToXpp3Dom
        .apply(builderSupplier.get().parse(new ByteArrayInputStream("<meta><xyx/></meta>".getBytes()))));
    assertNotNull(translateToXpp3Dom.apply(emptyDocumentSupplier.get()));
    assertNotNull(translateToXpp3Dom.apply(new Xpp3Dom("metadata")));
  }

  @Test
  public void testW3cEqualser() {
    String ls = "<metadata/>";
    String rs = ls;
    Document l = IBMetadataUtils.fromXpp3Dom.apply(ls);
    Document r = IBMetadataUtils.fromXpp3Dom.apply(rs);
    assertTrue(IBMetadataUtils.w3cDocumentEqualser.apply(l, r));
  }
}
