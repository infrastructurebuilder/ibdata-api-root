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
package org.infrastructurebuilder.data.model;

import static org.junit.Assert.*;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.infrastructurebuilder.data.IBSchema;
import org.infrastructurebuilder.util.artifacts.Checksum;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSchemaTest {
  public final static Logger log = LoggerFactory.getLogger(DataSchemaTest.class);

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private DataSchema d,e;
  private SchemaField f;

  @Before
  public void setUp() throws Exception {
    d = new DataSchema();
    d.setMetadata(new Xpp3Dom("metadata"));
    f  = new SchemaField();

    d.addField(f);
    Thread.currentThread().sleep(10L);
    e = new DataSchema();
    e.addField(f);
    e.setMetadata(new Xpp3Dom("metadata"));
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void test() {
    Checksum k = d.asChecksum();
    Checksum j = e.asChecksum();
    int v = d.compareTo(e);
    log.info("k = " + k + " \nj = " + j + " \nv = " + v);
    assertTrue(v != 0);
  }

}
