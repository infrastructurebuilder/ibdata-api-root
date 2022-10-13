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

import static java.nio.file.Files.createTempFile;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.of;
import static org.infrastructurebuilder.util.constants.IBConstants.AVRO_BINARY;
import static org.infrastructurebuilder.data.IBDataAvroUtils.fromSchemaAndPathAndTranslator;
import static org.infrastructurebuilder.data.IBDataException.cet;

import java.io.IOException;
import java.nio.file.Path;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.data.ingest.IBDataAvroRecordWriter;
import org.infrastructurebuilder.util.LoggerSupplier;
import org.infrastructurebuilder.util.core.Checksum;
import org.infrastructurebuilder.util.config.PathSupplier;
import org.infrastructurebuilder.util.files.BasicIBChecksumPathType;
import org.infrastructurebuilder.util.files.IBChecksumPathType;
import org.jooq.Record;
import org.slf4j.Logger;

/**
 * Writes Jooq Record instances to a stream as Avro GenericRecord instances using the mapped Schema and optional GenericData
 * @author mykel.alvis
 *
 */
public class JooqRecordWriter implements IBDataAvroRecordWriter<Record> {

  private final Path workingPath;
  private final Schema schema;
  private final Logger log;
  private final GenericData f;

  public JooqRecordWriter(LoggerSupplier l, PathSupplier wps, Schema schema, GenericData f) {
    this.log = requireNonNull(l).get();
    this.workingPath = requireNonNull(wps).get();
    this.schema = requireNonNull(schema);
    this.f = requireNonNull(f);
  }

  @Override
  public IBChecksumPathType writeRecords(Iterable<Record> result) {
//    GenericData gd = new MapProxyGenericData(this.f);
    Path path = cet.withReturningTranslation(() -> createTempFile(workingPath, "JooqRecords", ".avro"));
    try (DataFileWriter<GenericRecord> w = fromSchemaAndPathAndTranslator(path, schema, of(f))) {
      for (Record r : result) {
        w.append(new JooqRecordMapProxy(r, schema, log).get());
      }
    } catch (IOException e) {
      throw new IBDataException("Failed to writeRecords", e);
    }
    Checksum c = new Checksum(path);
    Path targetName = this.workingPath.resolve(c.asUUID().get().toString() + ".avro");
    return cet.withReturningTranslation(() -> new BasicIBChecksumPathType(path, c, AVRO_BINARY).moveTo(targetName));
  }



}
