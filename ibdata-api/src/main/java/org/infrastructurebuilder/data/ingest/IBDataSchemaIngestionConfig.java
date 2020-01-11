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

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.infrastructurebuilder.data.Metadata;

public interface IBDataSchemaIngestionConfig {

  String getTemporaryId();

  Optional<String> getName();

  Optional<String> getDescription();

  Metadata getMetadata();

  Optional<String> getInline();

  Optional<SchemaQueryBean> getSchemaQuery();

  Optional<List<Path>> getFiles();

  default Supplier<StringBuilder> toStringSupplier(Class<?> thisClazz) {
    StringBuilder builder = new StringBuilder();
    builder
        // Class name
        .append(thisClazz.getName())
        // The rest
        .append(" [") // started
        .append("temporaryId=").append(getTemporaryId()) // temp
        .append(", name=").append(getName()) // Name
        .append(", description=").append(getDescription()) // desc
        .append(", metadata=").append(getMetadata()) // meta
        .append(", inline=").append(getInline()) // inline
        .append(", schemaQuery=").append(getSchemaQuery()) // query
    ;
//        .append("]");  // Apply this in the toString method
    return () -> builder;

  }
}