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
import static java.util.Optional.empty;

import java.util.Optional;

public class DefaultIBDataTransformationError implements IBDataTransformationError {

  private final Optional<Throwable> e;
  private final Optional<String> message;

  DefaultIBDataTransformationError() {
    this(empty(), empty());
  }

  public DefaultIBDataTransformationError(Optional<Throwable> e, Optional<String> message) {
    this.e = requireNonNull(e);
    this.message = requireNonNull(message);
  }

  @Override
  public Optional<String> getMessage() {
    return message;
  }

  @Override
  public Optional<Throwable> getError() {
    return e;
  }
}
