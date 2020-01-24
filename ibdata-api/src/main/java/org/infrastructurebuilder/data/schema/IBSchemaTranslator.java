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
package org.infrastructurebuilder.data.schema;

import static java.util.Optional.empty;

import java.util.List;
import java.util.Optional;

import org.infrastructurebuilder.data.IBDataDecoratedDAO;
import org.infrastructurebuilder.data.IBSchema;
import org.infrastructurebuilder.util.LoggerEnabled;

/**
 * IBSchemaTranslator instances are self-contained, injected transformers that
 * theoretically produce working instances of the targeted types to and from an
 * IBSchema instance.
 *
 * In several cases, the inbound type and the outbound type are radically different.
 * For instance the default database implementation uses Jooq to read the database
 * schema and liquibase to write it.  This might not be optimal, but it's easily
 * replaceable.
 *
 * @author mykel.alvis
 *
 * @param <I> type to translate from
 * @param <O> type to translate to
 */
public interface IBSchemaTranslator<I, O> extends LoggerEnabled {

  /**
   * By contract, this is the fully qualified string name of type for I
   * empty() means that this translator cannot perform inbound translations
   * @return
   */
  default Optional<String> getInboundType() {
    return empty();
  }

  /**
   * By contract, this is the fully qualified string name of type for O
   * empty() means that this translator cannot perform outbound translations
   * @return
   */
  default Optional<String> getOutboundType() {
    return empty();
  }

  /**
   * Translate from a known schema type to a list of IBSchema
   *
   * The value must be wrapped in a {@link IBDataDecoratedDAO} in order
   * to ensure required data is available.
   *
   * Contractually, the system will never call this unless getInboundType is not empty()
   *
   * This allows multiple resulting IBSchema from a single source, for references
   * and sub-records
   *
   * @param s
   * @return
   */
  default Optional<List<IBSchema>> from(List<IBDataDecoratedDAO<I>> s) {
    return empty();
  }

  /**
   * Translate from an IBSchema instance to a known type.
   * Contractually, the system will never call this unless getOutboundType is not empty()
   *
   * @param s
   * @return
   */
  default Optional<List<O>> to(List<IBSchema> s) {
    return empty();
  }

}
