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

import java.nio.file.Path;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

import org.infrastructurebuilder.data.IBSchemaDAOSupplier;
import org.infrastructurebuilder.data.IBSchemaIngester;
import org.infrastructurebuilder.data.IBSchemaSourceSupplier;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.config.PathSupplier;
import org.infrastructurebuilder.util.config.TestingPathSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FakeIBSchemaIngesterSupplier extends AbstractIBSchemaIngesterSupplier<Object> {
  public final static Logger log = LoggerFactory.getLogger(FakeIBSchemaIngesterSupplier.class);
  public final static TestingPathSupplier wps = new TestingPathSupplier();

  public FakeIBSchemaIngesterSupplier(ConfigMapSupplier config) {
    super(wps, () -> log, config);
  }

  @Override
  public FakeIBSchemaIngesterSupplier getConfiguredSupplier(ConfigMapSupplier cms) {
    return new FakeIBSchemaIngesterSupplier(cms);
  }

  @Override
  protected IBSchemaIngester getInstance(PathSupplier wps, Object in) {
    return new FakeIBSchemaIngester(wps.get(), getLog(), getConfig().get());
  }

  public class FakeIBSchemaIngester extends AbstractIBSchemaIngester {

    public FakeIBSchemaIngester(Path workingPath, Logger log, ConfigMap config) {
      super(workingPath, log, config);
    }

    @Override
    public SortedSet<IBSchemaDAOSupplier> ingest(SortedMap<String, IBSchemaSourceSupplier> dss) {
      TreeSet<IBSchemaDAOSupplier> d = new TreeSet<>();
      return d;
    }

  }
}
