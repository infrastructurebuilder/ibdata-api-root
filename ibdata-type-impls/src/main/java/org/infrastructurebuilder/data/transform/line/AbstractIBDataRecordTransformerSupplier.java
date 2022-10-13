package org.infrastructurebuilder.data.transform.line;
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

import java.nio.file.Path;
import java.util.Objects;

import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.core.LoggerSupplier;
import org.infrastructurebuilder.util.core.PathSupplier;
import org.slf4j.Logger;

abstract public class AbstractIBDataRecordTransformerSupplier<I, O> implements IBDataRecordTransformerSupplier<I, O> {
  private final PathSupplier wps;
  private final ConfigMapSupplier config;
  private Logger logger;

  protected AbstractIBDataRecordTransformerSupplier(PathSupplier wps, ConfigMapSupplier cms, LoggerSupplier l) {
    this.wps = Objects.requireNonNull(wps);
    this.config = cms;
    this.logger = l.get();
  }

  @Override
  public IBDataRecordTransformer<I, O> get() {
    return getUnconfiguredTransformerInstance(this.wps.get()).configure(getConfigSupplier().get());
  }

  @Override
  abstract public AbstractIBDataRecordTransformerSupplier<I, O> configure(ConfigMapSupplier cms);

  /**
   * Must return a new instance of an IBDataTransformer
   * @param workingPath
   * @return
   */
  protected abstract IBDataRecordTransformer<I, O> getUnconfiguredTransformerInstance(Path workingPath);

  protected PathSupplier getWps() {
    return wps;
  }

  protected ConfigMapSupplier getConfigSupplier() {
    return config;
  }

  @Override
  public Logger getLogger() {
    return this.logger;
  }


}