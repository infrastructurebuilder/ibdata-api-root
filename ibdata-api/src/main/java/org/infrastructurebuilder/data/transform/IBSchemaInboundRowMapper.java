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

import java.util.List;
import java.util.Optional;

import org.infrastructurebuilder.data.IBDataTransformationError;
import org.infrastructurebuilder.data.IBSchema;
import org.infrastructurebuilder.util.config.ConfigMap;

/**
 * Maps a typed object, conforming to
 * {@code IBSchemaInboundRowMapper#getInboundType()} to an
 * {@link IBDataIntermediary} if possible.
 *
 * Usable instances of this have been configured using the schema that the intermediary
 * will conform to.
 *
 * @author mykel.alvis
 *
 * @param <I> The type that will be supplied to the map() function in order to produce
 * an {@link IBDataIntermediary}.
 */
public interface IBSchemaInboundRowMapper<I> {

  /**
   * The expected schema representation of the type I.  For instance, an Avro {@link GenericRecord}
   * is represented by an Avro {@link Schema}.
   * @return
   */
  String getInboundType();

  IBSchemaInboundRowMapper<I> configure(IBSchema inboundTargetSchema, ConfigMap cm);

  Optional<IBDataIntermediary> map(I row);

  List<IBDataTransformationError> getCurrentErrorsList();
}
