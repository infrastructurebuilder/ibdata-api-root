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
package org.infrastructurebuilder.data.transform;

import static org.infrastructurebuilder.data.transform.Record.FIELD_KEY;
import static org.infrastructurebuilder.data.transform.Record.FIELD_MAP;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.infrastructurebuilder.data.IBDataConstants;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RecordTest {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private Record r;

  @Before
  public void setUp() throws Exception {
    r = new Record();
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testSetFields() {
    r.setFields(Arrays.asList("A", "B"));
    Map<String, String> fieldMap = new HashMap<>();
    fieldMap.put("D", "E");
    r.setFieldMap(fieldMap);
    r.setId("id");
    r.setHint(null);
    ConfigMap config = new ConfigMap();
    config.put(FIELD_MAP, "X");
    config.put(FIELD_KEY, "Y");
    config.put("DD", "CC");

    r.setConfig(config);

    ConfigMap q = r.configurationAsMap();
    assertEquals(3,q.size());
    assertEquals(fieldMap, q.get(FIELD_MAP));
    assertEquals(Arrays.asList("A", "B"), q.get(FIELD_KEY));
    assertEquals("CC", q.get("DD"));
  }

  @Test
  public void testJoinKey() {
    r.setId("A");
    assertEquals("A" + IBDataConstants.MAP_SPLITTER + "A", r.joinKey());
  }

}
