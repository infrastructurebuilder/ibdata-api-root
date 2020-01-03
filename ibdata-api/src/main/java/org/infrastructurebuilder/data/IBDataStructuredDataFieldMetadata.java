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

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

/**
 * This type's name is a mouthful.
 *
 * This type describes an optional piece of metadata about a field in a
 * DataStream that contains structured (or somewhat structured) data. These
 * values are meant to be calculated at transformation time, so the
 * transformation should produce them. They could change during the course of a
 * transformation so it's important that we be able to manipulate the list
 * easily.
 *
 * @author mykel.alvis
 *
 */
public interface IBDataStructuredDataFieldMetadata {

  /**
   * Required. 0-based index of this field
   *
   * @return
   */
  int getIndex();

  /**
   * If we have a name, return it
   *
   * @return
   */
  Optional<String> getName();

  /**
   * Returns the string representations of enumeration values (the "name()"s).
   *
   * @return
   */
  List<String> getEnumerations();

  default boolean isEnumeration() {
    return ofNullable(getEnumerations()).orElse(emptyList()).size() > 0;
  }

  default Optional<BigInteger> getMaxIntValue() {
    try {
      return ofNullable(getMaxAsStringValue()).map(BigInteger::new);
    } catch (NumberFormatException e) {
      return empty();
    }
  }


  default Optional<BigInteger> getMinIntValue() {
    try {

      return ofNullable(getMinAsStringValue()).map(BigInteger::new);
    } catch (NumberFormatException e) {
      return empty();
    }
  }

  default Optional<BigDecimal> getMaxRealValue() {
    try {
      return ofNullable(getMaxAsStringValue()).map(BigDecimal::new);
    } catch (NumberFormatException e) {
      return empty();
    }
  }

  default Optional<BigDecimal> getMinRealValue() {
    try {
      return ofNullable(getMinAsStringValue()).map(BigDecimal::new);
    } catch (NumberFormatException e) {
      return empty();
    }
  }



  default Optional<Integer> getUniqueValuesCount() {
    return empty();
  }

  /**
   * Is this field nullable
   *
   * @return returns empty if unknown, or Optional<True> if nullable
   */
  Optional<Boolean> isNullable();

  String getMinAsStringValue();

  String getMaxAsStringValue();

  Optional<IBDataStructuredDataMetadataType> getType();

}
