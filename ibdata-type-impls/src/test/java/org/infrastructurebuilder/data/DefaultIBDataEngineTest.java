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

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.util.Arrays;

import org.infrastructurebuilder.util.core.TestingPathSupplier;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultIBDataEngineTest {

  public final static Logger log = LoggerFactory.getLogger(DefaultIBDataEngineTest.class);
  public final static TestingPathSupplier wps = new TestingPathSupplier();

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private DefaultIBDataEngine de;

  @Before
  public void setUp() throws Exception {
    de = new DefaultIBDataEngine(() -> log);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void test() throws MalformedURLException {
    assertEquals(0, de.getAvailableIds().size());
    de.setAdditionalURLS(Arrays.asList(wps.getTestClasses().resolve("test.jar").toUri().toURL()));
    assertEquals(1, de.prepopulate());
    assertEquals(1, de.getAvailableIds().size());
  }

}
