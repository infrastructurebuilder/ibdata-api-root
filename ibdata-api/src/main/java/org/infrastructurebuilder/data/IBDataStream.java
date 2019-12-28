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

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Supplier;

import org.infrastructurebuilder.util.files.TypeToExtensionMapper;

/**
 * Supplies a new instance of an InputStream every time get() is called
 * @author mykel.alvis
 *
 */
public interface IBDataStream extends Supplier<InputStream>, AutoCloseable, IBDataStreamIdentifier {

  /**
   * Relocate the local stream to some new parent location according to data checksum.  How this happens
   * depends mostly on how the stream is stored in the given instance of the DSS.
   * @param newWorkingPath
   * @return a new or updated IBDataStreamSupplier
   */
  IBDataStream relocateTo(Path newWorkingPath, TypeToExtensionMapper t2e);

  Optional<IBDataStructuredDataMetadata> getIBDataStructuredDataMetadata();

}
