/*
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
package org.infrastructurebuilder.data.ingest.base;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import java.util.function.Supplier;

import org.codehaus.plexus.configuration.xml.XmlPlexusConfiguration;
import org.infrastructurebuilder.util.constants.IBConstants;
import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.data.Metadata;
import org.infrastructurebuilder.data.ingest.base.DefaultIBDataSchemaIngestionConfigBean;
import org.infrastructurebuilder.util.BasicCredentials;
import org.infrastructurebuilder.util.CredentialsFactory;
import org.infrastructurebuilder.util.DefaultBasicCredentials;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;;

public class DefaultIBDataSchemaIdentifierConfigBeanTest {

  private static final String SCHEMA = "schema";
  private static final String NAME = "A name";
  private static final String DESCRIPTION = "description";

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private DefaultIBDataSchemaIngestionConfigBean b;
  private CredentialsFactory cf = new CredentialsFactory() {

    @Override
    public Optional<BasicCredentials> getCredentialsFor(String query) {
      return Optional.ofNullable(("1".equals(query)) ? new DefaultBasicCredentials("A", Optional.of("B")) : null);
    }
  };

  @Before
  public void setUp() throws Exception {
    b = new DefaultIBDataSchemaIngestionConfigBean();
    b.setTemporaryId("A");
    b.setDescription(DESCRIPTION);
    b.getMetadata();
    b.setMetadata(new XmlPlexusConfiguration("metadata"));
    b.setName(NAME);
    assertTrue(b.equals(b));
    assertFalse(b.equals(null));
    assertFalse(b.equals("X"));
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testHashCode() {
    assertNotEquals(0, b.hashCode());
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

  /*
  @Test
  public void testSetSchemaQuery() {
    b.setSchemaQuery(new SchemaQueryBean());
  }

  @Test(expected = IBDataException.class)
  public void testSetSchemaQueryFail1() {
    b.setInline(new XmlPlexusConfiguration(SCHEMA));
    b.setSchemaQuery(new SchemaQueryBean());
  }

  @Test(expected = IBDataException.class)
  public void testSetSchemaQueryFail2() {
    b.setFiles(emptyList());
    b.setSchemaQuery(new SchemaQueryBean());
  }

  @Test(expected = IBDataException.class)
  public void testSetInlineFail1() {
    b.setSchemaQuery(new SchemaQueryBean());
    b.setInline(new XmlPlexusConfiguration(SCHEMA));
  }

  @Test(expected = IBDataException.class)
  public void testSetInlineFail2() {
    b.setFiles(emptyList());
    b.setInline(new XmlPlexusConfiguration(SCHEMA));
  }
  */
  @Test(expected = IBDataException.class)
  public void testSetInlineFail3() {
    b.setInline(new XmlPlexusConfiguration("A"));
  }

  @Test
  public void testToString() {
    assertNotNull(b.toString());
  }

  @Test
  public void testAsconfigMap() {
    ConfigMap q = b.asConfigMap();
    assertEquals("A", q.get(IBConstants.TEMPORARYID));
  }

}
