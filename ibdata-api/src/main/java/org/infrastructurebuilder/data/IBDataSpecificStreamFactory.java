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

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * This returns a specific type of object (which we obviously don't deal with yet)
 * for a given mime type.
 *
 * @author mykel.alvis
 *
 */
public interface IBDataSpecificStreamFactory<T> {

  /**
   * Order of use. Higher numbers are ahead of lower numbers
   * @return
   */
  default int getWeight() {
    return 0;
  }

  /**
   * The mime types this factory will support
   * @return
   */
  List<String> getRespondTypes();

  /**
   * A Strem of objects, relative to the mimeType of the stream itself
   * @param ds The stream to read the objects from
   * @return A Stream of Objects.  All should be the same type, but not necessarily knowable ahead of time
   */
  Optional<Stream<T>> from(IBDataStream ds);

  default boolean respondsTo(String mimeType) {
    return getRespondTypes().contains(mimeType);
  }
}
