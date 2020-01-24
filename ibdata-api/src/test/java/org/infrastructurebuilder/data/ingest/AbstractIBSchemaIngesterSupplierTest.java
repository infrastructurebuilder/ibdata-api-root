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

import static org.junit.Assert.*;

import org.infrastructurebuilder.data.IBSchemaIngester;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.config.DefaultConfigMapSupplier;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AbstractIBSchemaIngesterSupplierTest {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private ConfigMapSupplier cms;
  private FakeIBSchemaIngesterSupplier a;
  private IBSchemaIngester i;

  @Before
  public void setUp() throws Exception {
    cms = new DefaultConfigMapSupplier();
    a = (FakeIBSchemaIngesterSupplier) new FakeIBSchemaIngesterSupplier(cms).configure(cms);
    i = a.get();
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testAbstractIBSchemaIngesterSupplier() {
    assertNotNull(a);
    assertNotNull(i);
  }

}
