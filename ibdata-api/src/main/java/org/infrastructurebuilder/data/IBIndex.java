/*
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
import java.util.SortedSet;

import org.infrastructurebuilder.util.core.Checksum;
import org.infrastructurebuilder.util.core.ChecksumEnabled;

/**
 * An IBIndex describes (possible) indexing mechanisms for serialized types
 *
 * @author mykel.alvis
 *
 */
public interface IBIndex extends ChecksumEnabled, Comparable<IBIndex> {

  /**
   * Get a detailed description of the IBDataField.
   *
   * @return String
   */
  String getDescription(); // -- String getDescription()

  /**
   * Get the full name of the IBDataField. This name may be munged during
   * translation.
   *
   * @return String
   */
  String getName(); // -- String getName()

  /**
   * Get value must match the typing enumeration in IBData (currently found in
   * IBDataIndexingType).
   *
   * The default value (DEFAULT) indicates that this index is not unique and
   * is ascending naturally ordered
   *
   * @return String
   */
  String getType(); // -- String getType()

  /**
   * Get version where this IBIndex became valid. Versions should be truncated
   * to api version. This field is required when updating a schema to a new
   * version.
   *
   * @return String
   */
  String getVersionAppeared(); // -- String getVersionAppeared()

  /**
   * Get version where this IBIndex became invalid. Versions should be
   * truncated to api version. This field is required when producing a deprecation
   * (note that a non-null value here does not indicate a deprecation. The flag
   * must still be set in order to deprecate).
   *
   * @return String
   */
  String getVersionDeprecated(); // -- String getVersionDeprecated()

  /**
   * Get if set to true, this IBIndex is deprecated.
   *
   * Schema deprecation is different than language deprecation.
   *
   * A deprecated index is not represented at all in outbound schema, but might be
   * accepted as inbound schema. Deprecation definitely means different things to
   * different underlying providers, but it may (likely) not be possible to back
   * out a deprecation.
   *
   * For some providers, the only means of moving forward is to re-add the field
   * as a new field with a later "versionAppeared".
   *
   * @return boolean
   */
  boolean isDeprecated(); // -- boolean isDeprecated()

  /**
   * Get if true, then this index only allows unique combinations of it's constituents.
   *
   * @return boolean
   */
  boolean isUnique(); // -- boolean isUnque()

  SortedSet<IBIndexField> getIndexFields();
  @Override
  default Checksum asChecksum() {
    return org.infrastructurebuilder.util.core.ChecksumBuilder.newInstance() //
        .addString(getType())  // Type
        .addString(getName()) // name
        .addString(getDescription()) // Desc
        .addString(getVersionAppeared()) // appeard
        .addString(getVersionDeprecated()) // disappeared
        .addBoolean(isUnique()) // nullable field
        .asChecksum();
  }

  /**
   * The only viable implementation of compareTo is this.
   * <p>
   * Do not try to override this method. It won't work :)
   */
  @Override
  default int compareTo(IBIndex o) {
    return getName().compareTo(o.getName());
  }

  default Optional<IBDataIndexingType> getIndexingType() {
    try {
      return Optional.of(IBDataIndexingType.valueOf(getType()));
    } catch (Throwable t) {
      return Optional.empty();
    }
  }

}