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

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AbstractIBDataDecoratedDAOTest {

  private static final String VAL = "val";
  private static final String SOURCE = "Source";
  private static final String DESC = "desc";
  private static final String VERSION = "1.0";
  private static final String NAME = "name";
  private static final String NAME_SPACE = "nameSpace";

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private AbstractIBDataDecoratedDAO<String> d;

  @Before
  public void setUp() throws Exception {
    d = new AbstractIBDataDecoratedDAO<String>(NAME_SPACE, NAME, VERSION, DESC, Optional.of(SOURCE), VAL) {
      @Override
      public Class<String> getType() {
        return String.class;
      }
    };
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testAbstractIBDataDecoratedDAO() {
    assertNotNull(d);
    assertEquals(NAME_SPACE, d.getNameSpace());
    assertEquals(NAME, d.getName());
    assertEquals(VERSION, d.getThisVersion());
    assertEquals(DESC, d.getDescription());
    assertEquals(SOURCE, d.getSource().get());
    assertEquals(VAL, d.get());
  }

}
