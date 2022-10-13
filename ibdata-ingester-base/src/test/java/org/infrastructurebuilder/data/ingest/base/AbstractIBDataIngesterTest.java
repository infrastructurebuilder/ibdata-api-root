/*
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
package org.infrastructurebuilder.data.ingest.base;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.nio.file.Path;
import java.util.List;
import java.util.SortedMap;

import org.infrastructurebuilder.data.IBDataSourceSupplier;
import org.infrastructurebuilder.data.IBDataStreamSupplier;
import org.infrastructurebuilder.data.ingest.base.AbstractIBDataIngester;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.IBRuntimeUtilsTesting;
import org.infrastructurebuilder.util.config.TestingPathSupplier;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractIBDataIngesterTest {
  private static final Logger log = LoggerFactory.getLogger(AbstractIBDataIngesterTest.class);
  private AbstractIBDataIngester i;
  private final static IBRuntimeUtilsTesting ibr = new IBRuntimeUtilsTesting(log);

  @Before
  public void setUp() throws Exception {
    i = new AbstractIBDataIngester(ibr, new ConfigMap()) {
      @Override
      public List<IBDataStreamSupplier> ingest(SortedMap<String, IBDataSourceSupplier<?>> dss) {
        return emptyList();
      }
    };
  }

  @Test
  public void testGetLog() {
    assertNotNull(i.getLog());
  }

  @Test
  public void testGetConfig() {
    assertEquals(0, i.getConfig().size());
  }

  @Test
  public void testGetWorkingPath() {
    assertNotNull(i.getWorkingPath());
  }

}
