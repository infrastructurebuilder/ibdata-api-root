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
package org.infrastructurebuilder.data.schema;

import static java.util.Collections.emptyList;
import static org.junit.Assert.*;

import org.infrastructurebuilder.data.FakeIBSchemaTranslator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IBSchemaTranslatorTest {
  public final static Logger log = LoggerFactory.getLogger(IBSchemaTranslatorTest.class);

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private IBSchemaTranslator<Object, Object> t;

  @Before
  public void setUp() throws Exception {
    t = new IBSchemaTranslator<Object, Object>() {
      @Override
      public Logger getLog() {
        return log;
      }
    };
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void test() {
    assertFalse(t.getInboundType().isPresent());
    assertFalse(t.getOutboundType().isPresent());
    assertFalse(t.from(emptyList()).isPresent());
    assertFalse(t.to(emptyList()).isPresent());
  }

}
