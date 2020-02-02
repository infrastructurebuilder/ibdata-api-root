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

import java.util.HashMap;
import java.util.Map;

import org.infrastructurebuilder.data.schema.IBSchemaTranslator;
import org.infrastructurebuilder.util.FakeCredentialsFactory;
import org.infrastructurebuilder.util.artifacts.IBArtifactVersionMapper;
import org.infrastructurebuilder.util.artifacts.impl.DefaultGAV;
import org.infrastructurebuilder.util.config.FakeIBVersionsSupplier;
import org.infrastructurebuilder.util.config.IBRuntimeUtils;
import org.infrastructurebuilder.util.config.IBRuntimeUtilsTesting;
import org.infrastructurebuilder.util.config.TestingPathSupplier;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultIBDataTranslationClearinghouseTest {
  public final static Logger log = LoggerFactory.getLogger(DefaultIBDataTranslationClearinghouseTest.class);
  public final static TestingPathSupplier wps= new TestingPathSupplier();
  private final static IBRuntimeUtils ibr = new IBRuntimeUtilsTesting(wps, log,
      new DefaultGAV(new FakeIBVersionsSupplier()), new FakeCredentialsFactory(), new IBArtifactVersionMapper() {
      });


  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private Map<String, IBSchemaTranslator<?, ?>> st = new HashMap<>();
  private Map<String, IBMappingTranslator<?>> mt = new HashMap<>();
  private DefaultIBDataTranslationClearinghouse c;
  private FakeIBSchemaTranslator ft;
  private FakeIBSchemaTranslator f2;
  private FakeIBMappingTranslator fm;

  @Before
  public void setUp() throws Exception {
    ft = new FakeIBSchemaTranslator("I", "O");
    f2 = new FakeIBSchemaTranslator("X", "Y");
    st.put("fake", ft);
    st.put("fake2", f2);
    fm = new FakeIBMappingTranslator("UpperLower");
    mt.put("fake", fm);
    c = new DefaultIBDataTranslationClearinghouse(ibr, st, mt);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetInboundTranslatorFor() {
    assertEquals(ft, c.getInboundTranslatorFor("I").get());
  }

  @Test
  public void testGetOutboundTranslatorFor() {
    assertEquals(f2, c.getOutboundTranslatorFor("Y").get());
  }

  @Test
  public void testGetMappingTranslatorFor() {
    assertEquals(fm,c.getMappingTranslatorFor("UpperLower").get());
  }

}
