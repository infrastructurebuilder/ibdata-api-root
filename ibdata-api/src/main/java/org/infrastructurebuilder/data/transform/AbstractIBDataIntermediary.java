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

import static java.util.Objects.requireNonNull;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.infrastructurebuilder.data.IBSchema;

abstract public class AbstractIBDataIntermediary extends ConcurrentHashMap<String, Object>
    implements IBDataIntermediary {
  private static final long serialVersionUID = -6387373520560457471L;
  private final IBSchema schema;
  private final IBDataTypedMapper t = new IBDataTypedMapper() {

    @Override
    public <T> Optional<T> get(String type, Object source) {
      return Optional.ofNullable((T) source);
    }

  };

  public AbstractIBDataIntermediary(IBSchema s) {
    super(requireNonNull(s).getSchemaFields().size());
    this.schema = s;
  }

  public AbstractIBDataIntermediary(IBSchema s, Map<? extends String, ? extends Object> m) {
    super(m);
    this.schema = requireNonNull(s);
  }

  public AbstractIBDataIntermediary(IBSchema s, float loadFactor) {
    super(requireNonNull(s).getSchemaFields().size(), loadFactor);
    this.schema = requireNonNull(s);
  }

  public AbstractIBDataIntermediary(IBSchema s, float loadFactor, int concurrencyLevel) {
    super(requireNonNull(s).getSchemaFields().size(), loadFactor, concurrencyLevel);
    this.schema = requireNonNull(s);
  }

  @Override
  public IBSchema getSchema() {
    return this.schema;
  }

  public IBDataTypedMapper getTypeMapper() {
    return t;
  }

  @Override
  public <T> Optional<T> getTyped(String targetType, String name) {
    return getTypeMapper().get(targetType, get(name));
  }

}
