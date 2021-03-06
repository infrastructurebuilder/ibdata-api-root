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

import static org.junit.Assert.assertFalse;

import org.infrastructurebuilder.util.config.ConfigMap;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IBDataRecordTransformerTest {

  private final static Logger log = LoggerFactory.getLogger(IBDataRecordTransformerTest.class);

  private IBDataRecordTransformer<Object, Object> i;

  @Before
  public void setUp() throws Exception {
    i = new IBDataRecordTransformer<Object,Object>() {

      @Override
      public Object apply(Object t) {
        return t;
      }

      @Override
      public String getHint() {
        return "very-dumb";
      }

      @Override
      public boolean respondsTo(Object o) {
        return false;
      }

      @Override
      public IBDataRecordTransformer<Object, Object> configure(ConfigMap cms) {
        return this;
      }

      @Override
      public Logger getLogger() {
        return log;
      }
      @Override
      public Class<Object> getInboundClass() {
        return Object.class;
      }

      @Override
      public Class<Object> getOutboundClass() {
        return getInboundClass();
      }

    };
  }

  @Test
  public void testProduces() {
    assertFalse(i.produces().isPresent());
  }

  @Test
  public void testAccepts() {
    assertFalse(i.accepts().isPresent());
  }

}
