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

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;

import org.infrastructurebuilder.util.artifacts.Checksum;
import org.infrastructurebuilder.util.artifacts.ChecksumBuilder;
import org.infrastructurebuilder.util.artifacts.ChecksumEnabled;

public interface IBIndexField extends ChecksumEnabled, Comparable<IBIndexField> {

  int getIndex();

  String getType();

  default Optional<IBFieldIndexType> getIndexFieldType() {
    try {
      return of(IBFieldIndexType.valueOf(getType()));
    }
    catch (Throwable t) {
      return empty();
    }
  }

  @Override
  default int compareTo(IBIndexField o) {
    return Integer.compare(getIndex(), o.getIndex());
  }

  @Override
  default Checksum asChecksum() {
    return ChecksumBuilder.newInstance() //
        .addInteger(getIndex()) // index
        .addString(getType()) // type
        .asChecksum();
  }
}
