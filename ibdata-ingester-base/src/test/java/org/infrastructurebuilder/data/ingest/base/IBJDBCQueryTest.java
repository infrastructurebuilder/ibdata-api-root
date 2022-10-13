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

import static java.util.Optional.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Optional;

import org.infrastructurebuilder.data.ingest.base.IBJDBCQuery;
import org.infrastructurebuilder.util.BasicCredentials;
import org.infrastructurebuilder.util.CredentialsFactory;
import org.infrastructurebuilder.util.DefaultBasicCredentials;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class IBJDBCQueryTest {

  private static final String JDBC__URL = "jdbc:h2:here";

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private IBJDBCQuery q;
  private CredentialsFactory cf = new CredentialsFactory() {
    @Override
    public Optional<BasicCredentials> getCredentialsFor(String query) {
      return of(new DefaultBasicCredentials("SA", Optional.empty()));
    }
  };

  @Before
  public void setUp() throws Exception {
    q = new IBJDBCQuery();
    q.setServerId("1");
    q.setUrl(JDBC__URL);
    assertNotNull(q.toString());
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetServerId() {
    assertEquals("1", q.getServerId().get());
  }

  @Test
  public void testGetUrl() {
    assertEquals(JDBC__URL, q.getUrl());
  }

  @Test
  public void testGetCreds() {
    BasicCredentials v = cf.getCredentialsFor(q).get();
    assertEquals("SA", v.getKeyId());
    assertFalse(v.getSecret().isPresent());
  }

}
