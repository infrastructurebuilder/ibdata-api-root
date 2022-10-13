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
package org.infrastructurebuilder.data.transform;

import java.nio.file.Path;
import java.util.Objects;

import org.infrastructurebuilder.data.IBDataStreamRecordFinalizer;
import org.infrastructurebuilder.data.transform.base.IBDataTransformer;
import org.infrastructurebuilder.data.transform.base.IBDataTransformerSupplier;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.core.LoggerEnabled;
import org.infrastructurebuilder.util.core.LoggerSupplier;
import org.infrastructurebuilder.util.core.PathSupplier;
import org.slf4j.Logger;

abstract public class AbstractIBDataTransformerSupplier implements IBDataTransformerSupplier, LoggerEnabled {
  private final PathSupplier wps;
  private final ConfigMapSupplier config;
  private final LoggerSupplier logger;

  public AbstractIBDataTransformerSupplier(
      // These go into your impls
      PathSupplier wps, LoggerSupplier l) {
    this(wps, l, null);
  }

  protected AbstractIBDataTransformerSupplier(
      // These go into your impls
      PathSupplier wps, LoggerSupplier l, ConfigMapSupplier cms) {
    this.wps = Objects.requireNonNull(wps);
    this.config = cms;
    this.logger = Objects.requireNonNull(l);
  }

  public PathSupplier getWps() {
    return wps;
  }

  @Override
  public Logger getLog() {
    return this.logger.get();
  }

  public ConfigMap getConfig() {
    return config.get();
  }

  @Override
  public abstract IBDataTransformerSupplier configure(ConfigMapSupplier cms);

  @Override
  public IBDataTransformerSupplier withFinalizer(IBDataStreamRecordFinalizer<?> ts2) {
    return this;
  }

  @Override
  public IBDataTransformer get() {
    return getConfiguredTransformerInstance(this.wps.get());
  }

  /**
   * Must return a new instance of an IBDataTransformer
   * @param workingPath
   * @return
   */
  protected abstract IBDataTransformer getConfiguredTransformerInstance(Path workingPath);

}
