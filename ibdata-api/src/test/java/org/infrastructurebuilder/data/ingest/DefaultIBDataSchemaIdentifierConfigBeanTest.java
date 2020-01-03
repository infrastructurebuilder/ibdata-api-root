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
package org.infrastructurebuilder.data.ingest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.codehaus.plexus.configuration.xml.XmlPlexusConfiguration;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.infrastructurebuilder.data.IBDataException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;;

public class DefaultIBDataSchemaIdentifierConfigBeanTest {

  private static final String NAME = "A name";
  private static final String MIME_TYPE = "text/avro-avsc";
  private static final String DESCRIPTION = "description";
  private static final String URL = "avro:zip:file:/file.zip!/avro.avsc";
  public final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private DefaultIBDataSchemaIdentifierConfigBean b;
  private Date cd;

  @Before
  public void setUp() throws Exception {
    b = new DefaultIBDataSchemaIdentifierConfigBean();
    cd = sdf.parse("2011-12-03T10:15:30Z");
    b.setCreationDate(cd);
    b.setId("A");
    b.setDescription(DESCRIPTION);
    b.getMetadata();
    b.getCreationDate();
    b.setMetadata(new XmlPlexusConfiguration("metadata"));
    b.setMimeType(MIME_TYPE);
    b.setName(NAME);
    b.setUrl(URL);
    b.setForcedMap(new ArrayList<>());
    b.setSha512(b.asChecksum().toString());
    b.setUuid(b.getUuid().toString());
    UUID uuid = b.getUuid();
    assertEquals("d7de88e5-0a88-391c-97e8-686e15731a16", b.getUuid().toString());
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testHashCode() {
    assertEquals(-1947039881, b.hashCode());
  }

  @Test
  public void testCopy() {
    DefaultIBDataSchemaIdentifierConfigBean c = b.copy();
    assertEquals(b, c);
    assertFalse(b == c);
    c = new DefaultIBDataSchemaIdentifierConfigBean(b);
    assertEquals(b, c);
    assertFalse(b == c);

  }

  @Test
  public void testGetId() {
    assertNotNull(b.getUuid());
  }

  @Test
  public void testSetId() {
    assertEquals("A", b.getTemporaryId().get());
    b.setId("B");
    assertEquals("B", b.getTemporaryId().get());
    assertNotEquals("B", b.getUuid().toString());
  }

  @Test
  public void testGetURL() {
    assertEquals(URL, b.getUrl().get());
  }

  @Test
  public void testGetName() {
    assertEquals(NAME, b.getName().get());
  }

  @Test
  public void testGetDescription() {
    assertEquals(DESCRIPTION, b.getDescription().get());
  }

  @Test
  public void testGetMetadata() {
    Xpp3Dom k = b.getMetadata();
    assertEquals(new Xpp3Dom("metadata"), k);
  }

  @Test
  public void testGetMimeType() {
    assertEquals(MIME_TYPE, b.getMimeType());
  }

  @Test
  public void testGetCreationDate() {
    assertEquals(cd, b.getCreationDate());
  }

  @Test
  public void testSetSchemaQuery() {
    b.setSchemaQuery(new DefaultSchemaQueryBean());
  }

  @Test
  public void testGetSchemaResourcesMappedFromName() {
    assertEquals(0, b.getSchemaResourcesMappedFromName().size());
  }

  @Test
  public void testSetNullSha512() {
    b.setSha512(null); // This is OK?
  }
  @Test(expected = IBDataException.class)
  public void testSetShortSha512() {
    b.setSha512("abcd");
  }

}
