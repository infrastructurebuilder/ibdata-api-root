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

import static java.util.stream.Collectors.joining;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

import org.infrastructurebuilder.util.artifacts.Checksum;
import org.infrastructurebuilder.util.artifacts.ChecksumBuilder;
import org.infrastructurebuilder.util.artifacts.ChecksumEnabled;

public interface IBSchema extends ChecksumEnabled, Comparable<IBSchema> {

  /**
   * Return Id. This value is calculated at persistence time, and is thus
   * effectively read-only, but prior to persistence it could, unfortunately, be
   * null.
   *
   * @return possibly null UUID of the local Checksum
   */
  UUID getUuid();

  /**
   * Fetch the temporaryId that this scheme referenced at ingestion. This is only
   * available during ingestion, as it is not persisted.
   *
   * @return
   */
  Optional<String> getTemporaryId();

  /**
   * Get creation date of this stream, required.
   *
   * @return Date
   */
  java.util.Date getCreationDate(); // -- java.util.Date getCreationDate()

  /**
   * Get a detailed description of the schema.
   *
   * @return String
   */
  Optional<String> getDescription(); // -- String getDescription()

  /**
   * get schema fields. The sorting must be via {@code IBField#getIndex()}
   *
   * @return List
   */
  SortedSet<IBField> getSchemaFields(); // -- from java.util.List<IBField> getFields()

  /**
   * Get additional metadata.
   *
   * @return Object
   */
  Metadata getMetadata(); // -- Object getMetadata()

  /**
   * Get the full name of the IBDataSchema.
   *
   * @return String
   */
  Optional<String> getName(); // -- String getName()

  /**
   * MIME type of the schema is probably quite fluid, and is thus stored as String
   * across IBData.
   *
   * @return
   */
  String getMimeType();

  default Optional<IBField> getFieldForIndex(int index) {
    return getSchemaFields().parallelStream().filter(a -> a.getIndex() == index).findFirst();
  }

  @Override
  default int compareTo(IBSchema o) {
    return asChecksum().compareTo(o.asChecksum());
  }

  @Override
  default Checksum asChecksum() {
    Map<String, String> map = getSchemaResourcesMappedFromName().entrySet().stream().collect(Collectors
        .toMap(k -> k.getKey(), v -> v.getValue().stream().map(UUID::toString).sorted().collect(joining("|"))));
    return ChecksumBuilder.newInstance() //
        .addString(getName()) // name
        .addString(getDescription()) // desc
        .addString(getMimeType()) // MIME type
        .addDate(getCreationDate()) // date
        .addString(getUrl()) // url
        .addString(getMetadata().toString()) // metadata as a string
        .addSortedSetChecksumEnabled(
            getSchemaFields().stream().collect(java.util.stream.Collectors.toCollection(TreeSet::new)))
        .addMapStringString(map) // Strings of UUIDs
        .asChecksum();
  }

  /**
   * Return a map of resources. These resources must be within the same archive as
   * the schema definition, and are stored as a DataStream within the local
   * archive.
   *
   * The DataStream that is the archive will have the same MIME type as the schema
   * presents.
   *
   *
   * @return
   */
  Map<String, List<UUID>> getSchemaResourcesMappedFromName();

  /**
   * URL of schema. May be prefaced with a type (i.e.
   * avro:file:/path/to/schema.avsc). Types must map to hints of components that
   * will read that schema.
   *
   * @return
   */

  Optional<String> getUrl();
}