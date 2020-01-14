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

import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;

import java.util.Map;

import org.infrastructurebuilder.data.IBDataStreamSupplier;
import org.infrastructurebuilder.data.IBSchema;
import org.infrastructurebuilder.data.IBSchemaDAO;
import org.infrastructurebuilder.data.IBSchemaSource;

public class DefaultIBSchemaDAO implements IBSchemaDAO {

  private final IBSchema schema;
  private final Map<String, IBDataStreamSupplier> map;
  private final IBSchemaSource<?> source;

  public DefaultIBSchemaDAO(IBSchema schema, Map<String, IBDataStreamSupplier> map, IBSchemaSource<?> src) {
    this.schema = requireNonNull(schema);
    this.map = requireNonNull(map);
    this.source = requireNonNull(src);
  }

  @Override
  public Map<String, IBDataStreamSupplier> get() {
    return unmodifiableMap(map);
  }

  @Override
  public IBSchema getSchema() {
    return schema;
  }

}
