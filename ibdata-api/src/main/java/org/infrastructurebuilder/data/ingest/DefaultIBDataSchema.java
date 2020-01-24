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

import static java.nio.file.Files.newBufferedWriter;
import static java.util.Collections.emptyMap;
import static java.util.Objects.requireNonNull;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static org.infrastructurebuilder.data.IBDataException.cet;
import static org.infrastructurebuilder.util.files.DefaultIBResource.fromPath;

import java.io.Writer;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.infrastructurebuilder.data.model.DataSchema;
import org.infrastructurebuilder.data.model.PersistedIBSchema;
import org.infrastructurebuilder.data.model.SchemaAsset;
import org.infrastructurebuilder.data.model.io.xpp3.PersistedIBSchemaXpp3Writer;

public class DefaultIBDataSchema extends DataSchema {

  private static final long serialVersionUID = -6073070167044366925L;

  public DefaultIBDataSchema(Path workingPath, PersistedIBSchema primary, Optional<Map<String, Path>> moreAssets) {
    super();
    final PersistedIBSchemaXpp3Writer writer = new PersistedIBSchemaXpp3Writer();

    Map<String, Path> finalized = new HashMap<>();
    finalized.putAll(requireNonNull(moreAssets).orElse(emptyMap()));
    // Unapologetically wipe out any hope of joy for inserting a PRIMARY asset
    finalized.put(PRIMARY, cet.withReturningTranslation(() -> {
      final Path p = workingPath.resolve(randomUUID().toString());
      try (Writer w = newBufferedWriter(p)) {
        writer.write(w, requireNonNull(primary, "Primary schema"));
        return p;
      }
    }));
    setSchemaAssets(finalized.entrySet().stream().map(e -> {
      return new SchemaAsset(e.getKey(), cet.withReturningTranslation(
          // Getting the correct SHA-512 can be expensive
          () -> fromPath(e.getValue()) // Expensive part
              .moveTo(requireNonNull(workingPath, "working path")))
          .getChecksum().toString());
    }).collect(toList()));
    setUuid(asChecksum().asUUID().get().toString());
  }

}
