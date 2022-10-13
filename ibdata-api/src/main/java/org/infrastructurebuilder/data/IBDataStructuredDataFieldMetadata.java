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

import static java.util.Optional.empty;

import java.math.BigDecimal;
import java.math.BigInteger;
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
   * Required. 0-based index of this field mapped to SchemaField
   *
   * @return
   */
  int getIndex();

  default Optional<IBField> getSchemaField() {
    return getSchema().flatMap(s -> s.getFieldForIndex(getIndex()));
  }

  default Optional<IBSchema> getSchema() {
    return getParent().flatMap(IBDataStructuredDataMetadata::getParent).flatMap(IBDataStream::getSchema);
  }

  /**
   * Get the {@link IBDataStructuredDataMetadata} that holds this field, if
   * available
   * <p>
   * Override in implementations
   *
   * @return
   */
  default Optional<IBDataStructuredDataMetadata> getParent() {
    return empty();
  }

  void setParent(IBDataStructuredDataMetadata parent);

  default Optional<BigInteger> getMaxIntValue() {
    try {
      return getMax().map(BigInteger::new);
    } catch (NumberFormatException e) {
      return empty();
    }
  }

  default Optional<BigInteger> getMinIntValue() {
    try {

      return getMin().map(BigInteger::new);
    } catch (NumberFormatException e) {
      return empty();
    }
  }

  default Optional<BigDecimal> getMaxRealValue() {
    try {
      return getMax().map(BigDecimal::new);
    } catch (NumberFormatException e) {
      return empty();
    }
  }

  default Optional<BigDecimal> getMinRealValue() {
    try {
      return getMin().map(BigDecimal::new);
    } catch (NumberFormatException e) {
      return empty();
    }
  }

  /**
   * Is this field nulled, different from the schema field
   * {@code IBField#isNullable()}
   *
   * @return returns empty if unknown, or Optional<True> if this field contains a
   *         null
   */
  Optional<Boolean> isNulled();

  Optional<String> getMin();

  Optional<String> getMax();

  Optional<Integer> getUniqueValuesCount();
}
