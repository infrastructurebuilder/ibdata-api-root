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

import static org.infrastructurebuilder.exceptions.IBException.cet;

import java.nio.file.Path;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.specific.SpecificDatumReader;

public final class DefaultAvroIBTypedRecordDataStreamSupplier<T> extends DefaultIBTypedDataStreamSupplier<T> {

  public DefaultAvroIBTypedRecordDataStreamSupplier(Path targetPath, IBDataStream stream, SpecificData c,
      boolean parallel) {
    super(stream,
        cet.returns(() -> new DataFileReader<T>(new DefaultSeekableInputFromInputStream(targetPath, stream.get()),
            new SpecificDatumReader<T>(c))),
        parallel);
  }

}