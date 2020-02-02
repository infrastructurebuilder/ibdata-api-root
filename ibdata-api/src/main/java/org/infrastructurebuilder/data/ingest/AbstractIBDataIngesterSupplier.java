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

import org.infrastructurebuilder.data.IBDataIngester;
import org.infrastructurebuilder.data.IBDataIngesterSupplier;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.config.IBRuntimeUtils;

abstract public class AbstractIBDataIngesterSupplier<P> extends AbstractIBDataConfigurableSupplier<IBDataIngester,P>
    implements IBDataIngesterSupplier<P> {

  protected AbstractIBDataIngesterSupplier(IBRuntimeUtils ibr, ConfigMapSupplier config) {
    super(ibr, config);
  }

}