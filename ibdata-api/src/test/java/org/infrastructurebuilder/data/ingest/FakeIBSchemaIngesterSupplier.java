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

import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

import org.infrastructurebuilder.data.IBSchemaDAOSupplier;
import org.infrastructurebuilder.data.IBSchemaIngester;
import org.infrastructurebuilder.data.IBSchemaSourceSupplier;
import org.infrastructurebuilder.util.FakeCredentialsFactory;
import org.infrastructurebuilder.util.artifacts.IBArtifactVersionMapper;
import org.infrastructurebuilder.util.artifacts.impl.DefaultGAV;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.config.FakeIBVersionsSupplier;
import org.infrastructurebuilder.util.config.IBRuntimeUtils;
import org.infrastructurebuilder.util.config.IBRuntimeUtilsTesting;
import org.infrastructurebuilder.util.config.TestingPathSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FakeIBSchemaIngesterSupplier extends AbstractIBSchemaIngesterSupplier<Object> {
  public final static Logger log = LoggerFactory.getLogger(FakeIBSchemaIngesterSupplier.class);
  public final static TestingPathSupplier wps = new TestingPathSupplier();
  private final static IBRuntimeUtils ibr = new IBRuntimeUtilsTesting(wps, log,
      new DefaultGAV(new FakeIBVersionsSupplier()), new FakeCredentialsFactory(), new IBArtifactVersionMapper() {
      });

  public FakeIBSchemaIngesterSupplier(ConfigMapSupplier config) {
    super(ibr, config);
  }

  @Override
  public FakeIBSchemaIngesterSupplier getConfiguredSupplier(ConfigMapSupplier cms) {
    return new FakeIBSchemaIngesterSupplier(cms);
  }

  @Override
  protected IBSchemaIngester getInstance(IBRuntimeUtils ibr, Object in) {
    return new FakeIBSchemaIngester(ibr, getConfig().get());
  }

  public class FakeIBSchemaIngester extends AbstractIBSchemaIngester {

    public FakeIBSchemaIngester(IBRuntimeUtils ibr, ConfigMap config) {
      super(ibr, config);
    }

    @Override
    public SortedSet<IBSchemaDAOSupplier> ingest(SortedMap<String, IBSchemaSourceSupplier> dss) {
      TreeSet<IBSchemaDAOSupplier> d = new TreeSet<>();
      return d;
    }

  }
}
