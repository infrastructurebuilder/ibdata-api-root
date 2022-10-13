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
import static java.util.Optional.of;
import static org.infrastructurebuilder.data.IBMetadataUtils.emptyDocumentSupplier;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.net.MalformedURLException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.xml.transform.TransformerException;

import org.infrastructurebuilder.data.model.IBMetadataUtils;
import org.infrastructurebuilder.util.constants.IBConstants;
import org.infrastructurebuilder.util.core.Checksum;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DefaultIBDataStreamIdentifierTest {

  private static final String DEFAULT_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";
  private IBDataSource ibds;
  private DefaultIBDataStreamIdentifier i;
  private UUID id;
  private Optional<String> url;
  private Optional<String> name;
  private Optional<String> description;
  private Checksum checksum;
  private Date creationDate;
  private Document metadata;
  private String mimeType;
  private Optional<String> path;
  private DefaultIBDataStreamIdentifier i2;

  @Before
  public void setUp() throws Exception {
    ibds = null;
    id = UUID.randomUUID();
    url = of("https://www.google.com");
    name = of("dsname");
    description = of("dsdescription");
    checksum = new Checksum("cdef");
    creationDate = new Date();
    metadata = emptyDocumentSupplier.get();
    mimeType = IBConstants.APPLICATION_OCTET_STREAM;
    path = of("./");
    i = new DefaultIBDataStreamIdentifier(id, url, name, description, checksum, creationDate, metadata, mimeType, path,empty(), empty());
    i2 = new DefaultIBDataStreamIdentifier(i);
  }


  @Test
  public void testHashCode() {
    assertEquals(i.hashCode(), i.hashCode());
    assertEquals(i.hashCode(), i2.hashCode());
    assertNotEquals(i.hashCode(),
        //
        new DefaultIBDataStreamIdentifier(null, url, name, description, checksum, creationDate, metadata, mimeType,
            empty(),empty(), empty()).hashCode());
  }

  @Test
  public void testGetId() {
    assertEquals(id, i.getId());
  }

  @Test
  public void testGetURL() {
    assertEquals(url, i.getURL());
  }

  @Test
  public void testGetName() {
    assertEquals(name, i.getName());
  }

  @Test
  public void testGetDescription() {
    assertEquals(description, i.getDescription());
  }

  @Test
  public void testGetChecksum() {
    assertEquals(checksum, i.getChecksum());
  }

  @Test
  public void testGetCreationDate() {
    assertEquals(creationDate, i.getCreationDate());
  }

  @Test
  public void testGetMetadata() {
    assertEquals(DEFAULT_XML, IBMetadataUtils.stringifyDocument.apply(i.getMetadata()));
  }

  @Test
  public void testGetMimeType() {
    assertEquals(IBConstants.APPLICATION_OCTET_STREAM, i.getMimeType());
  }

  @Test
  public void testGetPath() {
    assertEquals(path.get(), i.getPath());
  }

  @Test
  public void testEquals() throws MalformedURLException, TransformerException {
    i = new DefaultIBDataStreamIdentifier(id, url, name, description, checksum, creationDate, metadata, mimeType, path,empty(), empty());

    Document doc = emptyDocumentSupplier.get();
    Element root = doc.createElement("root");
    Element newChild = doc.createElement("something");
    newChild.setTextContent("ABC");
    // create the root element node
    Element element = doc.createElement("root");
    doc.appendChild(element);

    // create a comment node given the specified string
    Comment comment = doc.createComment("This is a comment");
    doc.insertBefore(comment, element);

    // add element after the first child of the root element
    Element itemElement = doc.createElement("item");
    element.appendChild(itemElement);

    // add an attribute to the node
    itemElement.setAttribute("myattr", "attrvalue");

    // create text for the node
    itemElement.insertBefore(doc.createTextNode("text"), itemElement.getLastChild());

    root.appendChild(newChild);

    String thedoc = IBMetadataUtils.stringifyDocument.apply(doc);

    assertEquals(i, i);
    assertNotEquals(i, null);
    assertNotEquals(i, "abc");
    assertNotEquals(i,
        new DefaultIBDataStreamIdentifier(null, url, name, description, checksum, creationDate, doc, mimeType, path,empty(), empty()));
    assertNotEquals(i,
        new DefaultIBDataStreamIdentifier(id, url, name, description, checksum, creationDate, doc, mimeType, path,empty(), empty()));
    assertNotEquals(i, new DefaultIBDataStreamIdentifier(id, of("https://himomImhome.com"), name, description, checksum,
        creationDate, doc, mimeType, path,empty(), empty()));
    assertNotEquals(i, new DefaultIBDataStreamIdentifier(id, url, name, description, new Checksum("abcd"), creationDate,
        metadata, mimeType, path,empty(), empty()));
    assertNotEquals(i,
        new DefaultIBDataStreamIdentifier(id, url, name, description, checksum, new Date(), metadata, mimeType, path,empty(), empty()));
    assertNotEquals(i, new DefaultIBDataStreamIdentifier(id, url, of("newName"), description, checksum, creationDate,
        metadata, mimeType, path,empty(), empty()));
    assertNotEquals(i, new DefaultIBDataStreamIdentifier(id, url, name, of("newDesc"), checksum, creationDate, metadata,
        mimeType, path,empty(), empty()));
    assertNotEquals(i, new DefaultIBDataStreamIdentifier(UUID.randomUUID(), url, name, description, checksum,
        creationDate, metadata, mimeType, path,empty(), empty()));
    UUID id1 = UUID.randomUUID();
    assertNotEquals(
        new DefaultIBDataStreamIdentifier(null, url, name, description, checksum, creationDate, metadata, mimeType,
            path,empty(), empty()),
        new DefaultIBDataStreamIdentifier(id1, url, name, description, checksum, creationDate, metadata, mimeType,
            path,empty(), empty()));
    assertEquals(
        new DefaultIBDataStreamIdentifier(null, url, name, description, checksum, creationDate, metadata, mimeType,
            path,empty(), empty()),
        new DefaultIBDataStreamIdentifier(null, url, name, description, checksum, creationDate, metadata, mimeType,
            path,empty(), empty()));
    assertEquals(
        new DefaultIBDataStreamIdentifier(id1, url, name, description, checksum, creationDate, metadata, mimeType,
            path,empty(), empty()),
        new DefaultIBDataStreamIdentifier(id1, url, name, description, checksum, creationDate, metadata, mimeType,
            path,empty(), empty()));
    assertNotEquals(i, new DefaultIBDataStreamIdentifier(id, url, name, description, checksum, creationDate, metadata,
        "other/type", path,empty(), empty()));

    assertNotEquals(
        new DefaultIBDataStreamIdentifier(id1, url, name, description, checksum, creationDate, metadata, mimeType,
            empty(),empty(), empty()),
        new DefaultIBDataStreamIdentifier(id1, url, name, description, checksum, creationDate, metadata, mimeType,
            path,empty(), empty()));
    assertEquals(
        new DefaultIBDataStreamIdentifier(null, url, name, description, checksum, creationDate, metadata, mimeType,
            empty(),empty(), empty()),
        new DefaultIBDataStreamIdentifier(null, url, name, description, checksum, creationDate, metadata, mimeType,
            empty(),empty(), empty()));
    assertNotEquals(
        new DefaultIBDataStreamIdentifier(id1, url, name, description, checksum, creationDate, metadata, mimeType,
            path,empty(), empty()),
        new DefaultIBDataStreamIdentifier(id1, url, name, description, checksum, creationDate, metadata, mimeType,
            empty(),empty(), empty()));

    assertNotEquals(i, new DefaultIBDataStreamIdentifier(id1, url, name, description, checksum, creationDate, metadata,
        mimeType, path,empty(), empty()));
    assertEquals(i, i2);
  }

}
