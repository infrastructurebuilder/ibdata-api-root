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

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Initial reference impl.
 *
 * @author mykel.alvis
 *
 */
@Named
public class DefaultIBStreamerFactory implements IBStreamerFactory {
  protected List<IBDataSpecificStreamFactory> sortedSuppliers;

  @Inject
  public DefaultIBStreamerFactory(
      // Type suppliers
      List<IBDataSpecificStreamFactory<?>> typedSuppliers) {
    // Sorted by weight
    this.sortedSuppliers = Objects.requireNonNull(typedSuppliers).stream()
        .sorted((f1, f2) -> Integer.compare(f2.getWeight(), f1.getWeight())).collect(Collectors.toList());

  }

  @SuppressWarnings("unchecked")
  @Override
  public Optional<Stream<? extends Object>> from(IBDataStream ds) {
    return sortedSuppliers.stream()
        // Only get responsive elements
        .filter(ss -> ss.respondsTo(ds.getMimeType()))
        // "first" has highest weight
        .findFirst()
        // flatMap to the optional return
        .flatMap(s -> s.from(ds));
  }

}
