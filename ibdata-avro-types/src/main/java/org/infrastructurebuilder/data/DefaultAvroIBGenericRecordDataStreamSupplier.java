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

import static org.infrastructurebuilder.data.IBDataException.cet;

import java.nio.file.Path;
import java.util.Optional;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;

public final class DefaultAvroIBGenericRecordDataStreamSupplier
    extends DefaultIBTypedDataStreamSupplier<GenericRecord> {

  public DefaultAvroIBGenericRecordDataStreamSupplier(Path targetPath, IBDataStream stream, boolean parallel,
      Schema schema) {
    super(stream,
        cet.withReturningTranslation(
            () -> new DataFileReader<GenericRecord>(new DefaultSeekableInputFromInputStream(targetPath, stream.get()),
                new GenericDatumReader<GenericRecord>(schema))),
        parallel);
  }

}