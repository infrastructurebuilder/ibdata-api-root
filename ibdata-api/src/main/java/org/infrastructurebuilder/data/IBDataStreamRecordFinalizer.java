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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public interface IBDataStreamRecordFinalizer<T> extends Supplier<InputStream>, AutoCloseable {
  public final static String NUMBER_OF_ROWS_TO_SKIP_PARAM = "numberOfRowsToSkip";

  String getId();

  default int getNumberOfRowsToSkip() {
    return 0;
  }

  /**
   * This method actually writes the final transformation.
   * @param recordToWrite
   * @return
   */
  Optional<IBDataTransformationError> writeRecord(T recordToWrite);

  /**
   * This value
   * @return
   * @throws IOException
   */
  OutputStream getWriterTarget() throws IOException;

  Supplier<IBDataStream> finalizeRecord(IBDataStreamIdentifier ds);

  Path getWorkingPath();

  default Optional<List<Class<?>>> accepts() {
    return empty();
  }

  default Optional<String> produces() {
    return empty();
  }
}
