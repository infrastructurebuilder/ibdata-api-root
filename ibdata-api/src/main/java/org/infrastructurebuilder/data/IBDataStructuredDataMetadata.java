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

import static java.util.Optional.empty;
import static java.util.stream.Collectors.toMap;
import static org.infrastructurebuilder.util.core.ChecksumEnabled.safeMapUUID;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public interface IBDataStructuredDataMetadata {
  String getUuid();

  default Optional<UUID> getId() {
    return safeMapUUID.apply(getUuid());
  }

  List<? extends IBDataStructuredDataFieldMetadata> getFields();

  default Map<Integer, ? extends IBDataStructuredDataFieldMetadata> getFieldMap() {
    return getFields().stream().collect(toMap(k -> k.getIndex(), Function.identity()));
  }

  /**
   * Get the {@link IBDataStream} that this metadata belongs to
   * <p>
   * Override in implementations
   *
   * @return
   */
  default Optional<IBDataStream> getParent() {
    return empty();
  }

  /**
   * Set the datastream that is the parent.
   * <p>
   * Don't judge me.  This was the easiest way to get the values here.
   *
   * @param parent
   */
  void setParent(IBDataStream parent);

}
