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

import java.nio.file.Path;
import java.util.UUID;
import java.util.function.Supplier;

import org.infrastructurebuilder.util.artifacts.Checksum;
import org.infrastructurebuilder.util.artifacts.ChecksumEnabled;

/**
 * Defines an asset held by a schema. All assets are assumed to be source
 * material of some form, although not necessarily textual.
 *
 * The Supplier function:
 *
 * get() produces a direct Path representation of the schema asset.
 *
 * The contract for this method is as follows: If the asset currently exists as
 * a path (i.e. getURL() represents a real Path on the filesystem, then this
 * method returns that value as a Path to a ReadOnly file.
 *
 * Otherwise, produce a path that contains the value pointed to by getURL as a
 * Path and return that value as a ReadOnly file.
 *
 * The value produced by get() must never be recaptured by the system.
 *
 * The value produced by get() is not guaranteed to be consistent
 *
 */
public interface IBDataSchemaAsset extends Supplier<Path>, ChecksumEnabled {
  /**
   * Unique identifier for a schema asset per-IBDataSchema instance. This
   * identifier should be consistent across istances of the schema type.
   *
   * @return unique per-asset identifier. For example "avro-avsc" might be an id.
   *         The use of this identifier is specific only on a per-type basis
   */
  String getId();

  /**
   * Data Stream ID for the actual asset's value
   *
   * @return
   */
  default UUID getAssetUUID() {
    return asChecksum().asUUID().orElseThrow(() -> new IBDataException("No SHA-512 for this asset " + getId()));
  }

  /**
   * SHA-512 value of the stored representation of this schema. Note that this is
   * value for the stored byte representation, not the representation on the
   * filesystem. Some filesystems might do something that alters this.
   *
   * At present, all textual data written within IBData (and probably
   * Infrastructure Builder as a whole) is in UTF-8.
   *
   * @return string representation of a sha-512 checksum of the bytes of the
   *         stored asset
   */

  String getSha512();

  @Override
  default Checksum asChecksum() {
    return new Checksum(getSha512());
  }

  @Override
  default Path get() {
    throw new IBDataException("Defaults implementations do not supply a viable path.");
  }

}
