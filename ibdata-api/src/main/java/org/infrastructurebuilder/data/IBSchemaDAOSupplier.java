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

import java.util.function.Supplier;

/**
 * The contract is that an IBSchemaDAOSupplier will supply all the required
 * data for the underlying persistence of an {@link IBSchema} as an
 * {@link IBSchemaDAO}.
 * <p>
 * Note that an IBSchemaDAOSupplier can supply a very arbitrary number
 * of resources within the IBSchemaDAO instance.
 * <b><i>Warning:</i></b> IBSchemaDAOSupplier instances are intrinsically
 * dangerous. They provide relatively arbitrary data streams during ingestion,
 * and thus should be subject to as much scrutiny as is possible. If the system
 * is using schema supplies from outside of the InfrastructureBuilder project,
 * then results may be unpredictable.
 * <p>
 * A schema supplier implementation <b>must</b>
 * <ol>
 * <li>Persist the IBSchema instance underneath</li>
 * <li>Supply that persisted IBSchema file as an
 * {@link IBDataStreamSupplier}</li>
 * <li>Supply the mapped name of that IBDataStreamSupplier as a string in
 * {@code getPrimaryAssetName()}</li>
 * </ol>
 * <p>
 * A schema supplier implementation <b>should</b>
 * <ol>
 * <li>Provide a stream to the original inbound schema, if available, mapping
 * that to the key value provided by {@code getOriginalAssetName}.  This is not
 * applicable if ingesting an inline schema directly, but if translating an
 * Avro schema into an IBSchema, then storing the original avro schem with some
 * mapped key that conforms to {@link IBSchemaDAO#getOriginalAssetKeyName()} is
 * required.
 * </ol>
 * <b>What this REALLY means</b>
 * <p>
 * {@link IBSchemaDAOSupplier} instances or their producers must maintain
 * internal state. Once the {@code get} method is called, producing an
 * {@link IBSchema}, they must update any internal storage for
 * {@code getAssetSuppliers}.
 * <p>
 *
 * @author mykel.alvis
 * @see IBSchemaIngesterSupplier
 * @see IBSchemaIngester
 */
public interface IBSchemaDAOSupplier extends Supplier<IBSchemaDAO>, Comparable<IBSchemaDAOSupplier> {
  String getTemporaryId();

  @Override
  default int compareTo(IBSchemaDAOSupplier o) {
    return getTemporaryId().compareTo(o.getTemporaryId());
  }
}
