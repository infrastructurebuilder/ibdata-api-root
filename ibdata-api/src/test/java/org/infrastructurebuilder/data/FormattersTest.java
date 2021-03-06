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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.infrastructurebuilder.util.config.ConfigMap;
import org.junit.Test;

public class FormattersTest {

  @Test
  public void test() {
    Formatters f = new Formatters() {
    };
    assertTrue(f.isBlankFieldNullInUnion());
    assertNotNull(f.getTimeFormatter());
    assertNotNull(f.getDateFormatter());
    assertNotNull(f.getTimestampFormatter());
  }

  @Test
  public void test2() {
    Formatters f = new Formatters(new ConfigMap()) {

    };
    assertTrue(f.isBlankFieldNullInUnion());
    assertNotNull(f.getTimeFormatter());
    assertNotNull(f.getDateFormatter());
    assertNotNull(f.getTimestampFormatter());
  }
}
