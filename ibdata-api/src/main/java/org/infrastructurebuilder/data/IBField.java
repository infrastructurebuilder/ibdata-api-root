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

import org.infrastructurebuilder.util.artifacts.Checksum;
import org.infrastructurebuilder.util.artifacts.ChecksumEnabled;

public interface IBField extends ChecksumEnabled, Comparable<IBField> {

  /**
   * Get describes the IBfield from a base of 0. Default value set to -1 to ensure
   * setting proper index. Indices are forced reset prior to persistence.
   * <p>
   * Note that the equals and comparable are all done based on field
   * and no other values.  This means that tracking index creation during
   * schema ingestion is essential.
   *
   * @return int zero-based field index within the set
   */
  int getIndex(); // -- int getIndex()
  /**
   * Get a detailed description of the IBDataField.
   *
   * @return String
   */
  String getDescription(); // -- String getDescription()

  /**
   * Method getEnumerations.
   *
   * @return List
   */
  java.util.List<String> getEnumerations(); // -- java.util.List<String> getEnumerations()


  /**
   * Get additional metadata.
   *
   * @return Object
   */
  Object getMetadata(); // -- Object getMetadata()

  /**
   * Get the full name of the IBDataField. This name may be munged during
   * translation.
   *
   * @return String
   */
  String getName(); // -- String getName()

  /**
   * Get value must match the typing enumeration in IBData (currently found in
   * IBDataStructuredDataMetadataType).
   *
   * The default value (STREAM) indicates that this schema denotes a single file
   * that is a stream of bytes, and that that is the most structure that can be
   * inferred. Due to the nature of STREAM, it is very important to correctly set
   * the IBDataField type.
   *
   * @return String
   */
  String getType(); // -- String getType()

  /**
   * Get version where this IBDataField became valid. Versions should be truncated
   * to api version. This field is required when updating a schema to a new
   * version.
   *
   * @return String
   */
  String getVersionAppeared(); // -- String getVersionAppeared()

  /**
   * Get version where this IBDataField became invalid. Versions should be
   * truncated to api version. This field is required when producing a deprecation
   * (note that a non-null value here does not indicate a deprecation. The flag
   * must still be set in order to deprecate).
   *
   * @return String
   */
  String getVersionDeprecated(); // -- String getVersionDeprecated()

  /**
   * Get if set to true, this IBDataField is deprecated.
   *
   * Schema deprecation is different than language deprecation.
   *
   * A deprecated field is not represented in outbound schema, but might be
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
   * Get if true, then this field is allowed to be null.
   *
   * @return boolean
   */
  boolean isNullable(); // -- boolean isNullable()

  default Checksum asChecksum() {
    return org.infrastructurebuilder.util.artifacts.ChecksumBuilder.newInstance() //
        .addString(getName()) // name
        .addString(getDescription()) // Desc
        .addString(getMetadata().toString()) // metadata
        .addString(getVersionAppeared()) // appeard
        .addString(getVersionDeprecated()) // disappeared
        .addBoolean(isNullable()) // nullable field
        .asChecksum();
  }

  /**
   * The only viable implementation of compareTo is this.
   * <p>
   * Do not override this method
   */
  @Override
  default int compareTo(IBField o) {
    return Integer.compare(getIndex(), o.getIndex());
  }

}