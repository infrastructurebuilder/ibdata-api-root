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
package org.infrastructurebuilder.data.archiver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.System.Logger;

import org.junit.Test;

public class DefaultIBDataPlexusIoZipFileResourceCollectionTest {
  public final static Logger logger = System.getLogger(DefaultIBDataPlexusIoZipFileResourceCollectionTest.class.toString());

  @Test
  public void testIoZipFileResourceCollection() {
    DefaultIBDataPlexusIoZipFileResourceCollection v = new DefaultIBDataPlexusIoZipFileResourceCollection(() -> logger);
    assertNotNull(v);
    assertEquals(logger, v.getLogger());
  }

}
