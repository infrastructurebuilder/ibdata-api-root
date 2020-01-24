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
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import org.infrastructurebuilder.data.schema.IBSchemaTranslator;
import org.infrastructurebuilder.util.LoggerSupplier;
import org.slf4j.Logger;

@Named
public class DefaultIBDataTranslationClearinghouse implements IBDataTranslationClearinghouse {

  private final Map<String, IBSchemaTranslator<?, ?>> st;
  private final Map<String, IBMappingTranslator<?>> mt;
  private final Map<String, IBSchemaTranslator<?, ?>> stInboundByClass;
  private final Map<String, IBSchemaTranslator<?, ?>> stOutboundByClass;
  private final Map<String, IBMappingTranslator<?>> mtByClass;
  private final Logger log;

  @Inject
  public DefaultIBDataTranslationClearinghouse(LoggerSupplier l,
      Map<String, IBSchemaTranslator<?, ?>> schemaTranslators, Map<String, IBMappingTranslator<?>> mappingTranslators) {
    this.log = requireNonNull(l).get();
    this.st = requireNonNull(schemaTranslators);
    this.mt = requireNonNull(mappingTranslators);
    this.stInboundByClass = this.st.values().stream().filter(v -> v.getInboundType().isPresent())
        .collect(toMap(k -> k.getInboundType().get(), identity()));
    this.stOutboundByClass = this.st.values().stream().filter(v -> v.getOutboundType().isPresent())
        .collect(toMap(k -> k.getOutboundType().get(), identity()));
    this.mtByClass = this.mt.values().stream().collect(toMap(k -> k.getType(), identity()));
    log.info(String.format("Loaded %s schema translators and %d mapping translators", st.size(), mt.size()));
  }

  @Override
  public Optional<IBSchemaTranslator<?,?>> getInboundTranslatorFor(String clazz) {
    return ofNullable(stInboundByClass.get(requireNonNull(clazz)));
  }
  @Override
  public Optional<IBSchemaTranslator<?,?>> getOutboundTranslatorFor(String clazz) {
    return ofNullable(stOutboundByClass.get(requireNonNull(clazz)));
  }

  @Override
  public Optional<IBMappingTranslator<?>> getMappingTranslatorFor(String clazz) {
    return ofNullable(mtByClass.get(requireNonNull(clazz)));
  }

}
