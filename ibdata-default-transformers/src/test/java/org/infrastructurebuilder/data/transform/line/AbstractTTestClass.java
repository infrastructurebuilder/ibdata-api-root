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
package org.infrastructurebuilder.data.transform.line;

import static org.junit.Assert.*;

import java.util.Map;

import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.util.LoggerSupplier;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.config.DefaultConfigMapSupplier;
import org.infrastructurebuilder.util.config.PathSupplier;
import org.infrastructurebuilder.util.config.TestingPathSupplier;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class AbstractTTestClass<T extends AbstractIBDataRecordTransformerSupplier<I, O>, I, O> {
  public final static Logger log = LoggerFactory.getLogger(AbstractTTestClass.class);
  public final static TestingPathSupplier wps = new TestingPathSupplier();

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    wps.finalize();
  }

  protected T t;
  protected I i;
  protected AbstractIBDataRecordTransformerSupplier<I, O> q;

  /**
   * Override to supply different config
   * @return
   */
  public ConfigMapSupplier getDefaultCMS() {
    return new DefaultConfigMapSupplier(getCMS());
  }

  protected abstract ConfigMapSupplier getCMS();

  /**
   * Override to run additional tests
   * @param c the configured transformer under test
   */
  public void runSuccessTestOn(IBDataRecordTransformer<I, O> c) {

  }

  @Before
  public void setUp() throws Exception {
    t = getT(wps, () -> log);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void test() {
    IBDataRecordTransformer<I, O> c = t.configure(getDefaultCMS()).get();
    O s = getSuccessTestValue();
    O tt = c.apply(getSuccessTestData());
    assertEquals(s, tt);
    runSuccessTestOn(c);
    c.accepts().ifPresent(cons -> {
      assertTrue(cons.contains(getI()));
    });
    c.produces().ifPresent(cons -> {
      assertEquals(cons, getO());

    });
  }

  @Test(expected = IBDataException.class)
  abstract public void failTest() throws Exception;

  abstract Class<I> getI();

  abstract Class<O> getO();

  abstract T getT(PathSupplier wps, LoggerSupplier l);

  abstract O getSuccessTestValue();

  abstract I getSuccessTestData();

}
