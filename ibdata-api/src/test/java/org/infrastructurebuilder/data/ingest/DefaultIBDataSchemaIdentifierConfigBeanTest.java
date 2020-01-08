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

import org.codehaus.plexus.configuration.xml.XmlPlexusConfiguration;
import org.infrastructurebuilder.data.Metadata;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;;

public class DefaultIBDataSchemaIdentifierConfigBeanTest {

  private static final String NAME = "A name";
  private static final String DESCRIPTION = "description";

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private DefaultIBDataSchemaIngestionConfig b;

  @Before
  public void setUp() throws Exception {
    b = new DefaultIBDataSchemaIngestionConfig();
    b.setTemporaryId("A");
    b.setDescription(DESCRIPTION);
    b.getMetadata();
    b.setMetadata(new XmlPlexusConfiguration("metadata"));
    b.setName(NAME);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testHashCode() {
    assertEquals(635231887, b.hashCode());
  }

  @Test
  public void testSetId() {
    assertEquals("A", b.getTemporaryId());
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
    Metadata k = b.getMetadata();
    assertEquals(new Metadata(), k);
  }

  @Test
  public void testSetSchemaQuery() {
    b.setSchemaQuery(new SchemaQueryBean());
  }

}
