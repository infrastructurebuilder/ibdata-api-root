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

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.util.Optional;

import org.infrastructurebuilder.data.IBDataIngester;
import org.infrastructurebuilder.util.config.AbstractCMSConfigurableSupplier;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.config.DefaultConfigMapSupplier;
import org.infrastructurebuilder.util.config.PathSupplier;
import org.infrastructurebuilder.util.config.TestingPathSupplier;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractIBDataIngesterSupplierTest {
  private final Logger log = LoggerFactory.getLogger(AbstractIBDataIngesterTest.class);
  private Path p;
  private Path c;
  private DefaultConfigMapSupplier cms;
  private AbstractIBDataIngesterSupplier<Object> i;
  private final static TestingPathSupplier wps = new TestingPathSupplier();

  @Before
  public void setUp() throws Exception {
    p = wps.get();
    c = wps.get();
    cms = new DefaultConfigMapSupplier();
    i = new AbstractIBDataIngesterSupplier<Object>(wps, () -> log, cms) {

      @Override
      public AbstractCMSConfigurableSupplier<IBDataIngester, Object> getConfiguredSupplier(ConfigMapSupplier cms) {
        return this;
      }

      @Override
      protected IBDataIngester getInstance(PathSupplier workingPath, Optional<Object> in) {
        return null;
      }
    };
  }

  @Test
  public void testGetLog() {
    assertEquals(log, i.getLog());
  }

  @Test
  public void testGetConfig() {
    assertEquals(0, i.getConfig().get().size());
  }

}
