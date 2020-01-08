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

import java.util.Optional;

import org.infrastructurebuilder.util.artifacts.Checksum;
import org.infrastructurebuilder.util.artifacts.ChecksumBuilder;
import org.infrastructurebuilder.util.artifacts.ChecksumEnabled;

public interface IBSchemaType extends ChecksumEnabled {
  /**
   * Returns the "type" of this schema, which is the hint for its component
   *
   * IBData defines a "default" type, which is actually used as the translation
   * format for other types.
   *
   * @return
   */

  String getType();

  /**
   * Obtain an indicator for the component that provides the underlying technology
   * for this schema type. For instance, "avro", "protobuf"
   *
   * The actual artifact that performs the work should be a versioned provider,
   * and should be queryable for its api version.
   *
   * Note that IBData is not currently an OSGi application but rather simply
   * expects that a given provider will be generally stable and will conform
   * strictly to Semantic Versioning api principles.  YMMV.
   *
   * @return
   */
  String getTechnology();

  /**
   * The identifier for the versioned provider of the schema-ingestion technology.
   * Normally this will be a Maven GAV in "dependency-get" format
   *
   * @return
   */
  Optional<String> getVersionedProvider();

  @Override
  default Checksum asChecksum() {
    return ChecksumBuilder.newInstance() //
        .addString(getType()) // type
        .addString(getTechnology()) // tech
        .addString(getVersionedProvider()) // versioned prov
        // fin
        .asChecksum();
  }
}
