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

import org.infrastructurebuilder.util.DefaultBasicCredentials;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DefaultURLAndCredsTest {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private DefaultURLAndCreds d;
  private DefaultURLAndCreds e;

  @Before
  public void setUp() throws Exception {
    d = new DefaultURLAndCreds("url", Optional.empty());
    e = new DefaultURLAndCreds("url2", Optional.of(new DefaultBasicCredentials("A", Optional.of("B"))));
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetUrl() {
    assertEquals("url", d.getUrl());
  }

  @Test
  public void testGetCreds() {
    assertFalse(d.getCreds().isPresent());
    assertEquals("A", e.getCreds().get().getKeyId());
  }

}
