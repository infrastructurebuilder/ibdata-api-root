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
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.StreamSupport.stream;

import java.util.Iterator;
import java.util.stream.Stream;

public class DefaultIBTypedDataStreamSupplier<T> extends DefaultIBDataStreamIdentifier
    implements IBTypedDataStreamSupplier<T> {

  private final Iterator<T> iterable;
  private final boolean parallel;

  public DefaultIBTypedDataStreamSupplier(IBDataStream original, Iterator<T> iterable) {
    this(original, iterable, false);
  }

  public DefaultIBTypedDataStreamSupplier(IBDataStream original, Iterator<T> iterable, boolean parallel) {
    super(original);
    this.iterable = requireNonNull(iterable);
    this.parallel = parallel;
  }

  @Override
  public Stream<T> get() {
    return stream(spliteratorUnknownSize(requireNonNull(iterable), 0), parallel);
  }

}
