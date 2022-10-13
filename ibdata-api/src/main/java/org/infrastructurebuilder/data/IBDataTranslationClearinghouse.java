/*
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

import java.util.Optional;

import org.infrastructurebuilder.data.schema.IBSchemaTranslator;

public interface IBDataTranslationClearinghouse {

  /**
   * Get the inbound translator for a given source class
   *
   * @param clazz
   * @return
   */
  Optional<IBSchemaTranslator<?, ?>> getInboundTranslatorFor(String clazz);

  /**
   * Get the inbound translator for a given target class
   *
   * @param clazz
   * @return
   */
  Optional<IBSchemaTranslator<?, ?>> getOutboundTranslatorFor(String clazz);

  /**
   * Get the mapping translator, used to transfer between types from the target
   * class
   *
   * @param clazz
   * @return
   */
  Optional<IBMappingTranslator<?>> getMappingTranslatorFor(String clazz);
}
