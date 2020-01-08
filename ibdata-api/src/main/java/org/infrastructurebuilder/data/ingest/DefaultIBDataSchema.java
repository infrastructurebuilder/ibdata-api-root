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
package org.infrastructurebuilder.data.ingest;

import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.data.model.DataSchema;
import org.infrastructurebuilder.data.model.PersistedIBSchema;
import org.infrastructurebuilder.data.model.SchemaAsset;
import org.infrastructurebuilder.data.model.io.xpp3.PersistedIBSchemaXpp3Writer;

public class DefaultIBDataSchema extends DataSchema {

  private static final long serialVersionUID = -6073070167044366925L;
  private final Path workingPath;

  public DefaultIBDataSchema(Path workingPath, List<PersistedIBSchema> dsList) {
    super();
    this.workingPath = Objects.requireNonNull(workingPath);

    setSchemaAssets(dsList
        // Stream
        .stream()
        //
        .map(this::writeSchema)
        //
        .collect(Collectors.toList()));
    setUuid(asChecksum().asUUID().get().toString());
  }

  private final SchemaAsset writeSchema(PersistedIBSchema schema) {
    PersistedIBSchemaXpp3Writer writer = new PersistedIBSchemaXpp3Writer();
    return IBDataException.cet.withReturningTranslation(() -> {
      final Path p = workingPath.resolve(UUID.randomUUID().toString());
      try (Writer w = Files.newBufferedWriter(p)) {
        writer.write(w, schema);
        return new LocalAsset(p, schema);
      }
    });

  }

  private static class LocalAsset extends SchemaAsset {
    private static final long serialVersionUID = -2537255005247301571L;
    private final Path written;
    private final PersistedIBSchema schema;

    LocalAsset(Path written, PersistedIBSchema schema) {
      this.written = Objects.requireNonNull(written);
      this.schema = Objects.requireNonNull(schema);
    }

    public PersistedIBSchema getSchema() {
      return schema;
    }

    public Path getWritten() {
      return written;
    }
  }
}
