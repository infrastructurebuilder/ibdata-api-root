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

import java.util.Objects;

import org.infrastructurebuilder.data.IBDataSchemaSupplier;
import org.infrastructurebuilder.util.LoggerSupplier;
import org.infrastructurebuilder.util.config.AbstractCMSConfigurableSupplier;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;

public abstract class AbstractIBDataSchemaHarvester extends AbstractCMSConfigurableSupplier<IBDataSchemaSupplier> {

  protected AbstractIBDataSchemaHarvester(ConfigMapSupplier config, LoggerSupplier l) {
    super(config, l);
  }

// Sorta like this
//  public AbstractCMSConfigurableSupplier<IBDataSchemaSupplier> getConfiguredSupplier(ConfigMapSupplier cms) {
//    return new AbstractIBDataSchemaHarvester(cms, () -> getLog());
//  }

  public abstract class AbstractIBDataSchemaSupplier implements IBDataSchemaSupplier {

    private final String id;

    public AbstractIBDataSchemaSupplier(String id) {
      this.id = Objects.requireNonNull(id);
    }

    @Override
    public String getId() {
      return this.id;
    }

  }
}
