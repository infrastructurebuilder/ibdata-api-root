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

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import java.util.Map;

import org.infrastructurebuilder.util.core.LoggerSupplier;
import org.slf4j.Logger;

abstract public class AbstractIBDataEngineSupplier implements IBDataEngineSupplier {

  private final Map<String, IBDataEngine> engineList;
  private final String id;
  private final LoggerSupplier logger;

  public AbstractIBDataEngineSupplier(LoggerSupplier l, Map<String, IBDataEngine> engineList) {
    this.logger = requireNonNull(l);
    this.engineList = requireNonNull(engineList);
    this.id = getLocalId();
  }

  @Override
  public IBDataEngine get() {
    return ofNullable(this.engineList.get(this.id))
        .orElseThrow(() -> new IBDataException("Engine " + this.id + " not found"));
  }

  @Override
  public final Logger getLog() {
    return this.logger.get();
  }
}
