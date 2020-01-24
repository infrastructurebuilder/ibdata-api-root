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
package org.infrastructurebuilder.data.schema.liquibase;

import static org.junit.Assert.*;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.infrastructurebuilder.data.IBDataException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class IBDataLiquibaseChangeSetTest {

  private static final String EMPTY_CHANGESET_CHECKSUM = "a5911bc9754847f4119615e8853a0ca9fb704bb0c9d6a1b68d39a0ea8fe72404a0690ad4d8c266d9c2cd2800179ba43f2e8ca8abdc1abb96e5f1954a2f6ace2b";

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private IBDataLiquibaseChangeSet c;
  private Xpp3Dom src;

  @Before
  public void setUp() throws Exception {
    src = new Xpp3Dom(IBDataLiquibaseChangeSet.CHANGE_SET);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test(expected = IBDataException.class)
  public void testIBDataLiquibaseChangeSetXpp3DomFail() {
    c = new IBDataLiquibaseChangeSet(new Xpp3Dom("bob"));
  }

  @Test
  public void testIBDataLiquibaseChangeSetXpp3Dom() {
    c = new IBDataLiquibaseChangeSet(src);
    assertEquals(EMPTY_CHANGESET_CHECKSUM, c.asChecksum().toString());
  }

}
