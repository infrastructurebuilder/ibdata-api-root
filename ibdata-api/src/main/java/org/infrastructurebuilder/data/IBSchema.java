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

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import org.infrastructurebuilder.util.artifacts.Checksum;
import org.infrastructurebuilder.util.artifacts.ChecksumBuilder;
import org.infrastructurebuilder.util.artifacts.ChecksumEnabled;

public interface IBSchema extends ChecksumEnabled, Comparable<IBSchema> {

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
  String getDescription(); // -- String getDescription()

  /**
   * get schema fields.
   *
   * @return List
   */
  SortedSet<IBField> getSchemaFields(); // -- from java.util.List<IBField> getFields()

  /**
   * Get additional metadata.
   *
   * @return Object
   */
  Object getMetadata(); // -- Object getMetadata()

  /**
   * Get the full name of the IBDataSchema.
   *
   * @return String
   */
  String getName(); // -- String getName()

  /**
   * Get the id as a UUID
   *
   * @return
   */
  java.util.UUID getId();

  @Override
  default int compareTo(IBSchema o) {
    return asChecksum().compareTo(o.asChecksum());
  }

  @Override
  default Checksum asChecksum() {
    return ChecksumBuilder.newInstance() //
        .addString(getName()) // name
        .addString(getDescription()) // desc
        .addDate(getCreationDate()) // date
        .addString(getMetadata().toString()) // metadata as a string
        .addSortedSetChecksumEnabled(
            getSchemaFields().stream().collect(java.util.stream.Collectors.toCollection(TreeSet::new)))
        .asChecksum();
  }

  Map<String, UUID> getForcedMapping();
}