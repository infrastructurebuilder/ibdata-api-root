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

import java.util.Arrays;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.infrastructurebuilder.data.IBDataException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class IBDataLiquibaseRollbackTest {

  private static final String TESTED_CHECKSUM = "3f320581351a49806352675a69a63d62d79204049c93a61ab80717d4a6ebfb75b712de7b768639355619fc47ccc4c91a012abbc3256cf0082dd8826e75ec8891";

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private IBDataLiquibaseRollback r;

  @Before
  public void setUp() throws Exception {
    r = new IBDataLiquibaseRollback("id", "author", "dbms", false, false, false , Arrays.asList("A"), false, LiquibaseQuotingStrategies.QUOTE_ALL_OBJECTS);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test(expected = IBDataException.class)
  public void testFail() {
    r = new IBDataLiquibaseRollback(new Xpp3Dom("bob"));
  }
  @Test
  public void testXpp3Dom() {
    r = new IBDataLiquibaseRollback(new Xpp3Dom(IBDataLiquibaseRollback.ROLLBACK));
    assertNotNull(r);
  }


  @Test
  public void test() {
    r = r.addComment("comment");
    assertEquals(TESTED_CHECKSUM, r.asChecksum().toString());
  }

}
