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
package org.infrastructurebuilder.data.type;

import static java.util.Objects.requireNonNull;

import org.infrastructurebuilder.util.artifacts.Weighted;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;

abstract public class IBDataType implements Weighted {
  private final String type;

  public IBDataType(String type) {
    this.type = requireNonNull(type);
  }

  public String getType() {
    return type;
  }

  public String name() {
    return getType();
  }

  public IBDataType configure(ConfigMapSupplier cms) {
    return this;
  }

}
