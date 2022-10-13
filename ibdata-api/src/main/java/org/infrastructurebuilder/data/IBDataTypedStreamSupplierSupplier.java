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

//import static java.util.Objects.requireNonNull;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface IBDataTypedStreamSupplierSupplier {
//  // Weighted use.  Higher number gets used first
//  default Integer getWeight() {
//    return 0;
//  }
//
//  /**
//   * Does this factory supply suppliers for the given MIME type?
//   * @param mimeType
//   * @return true if the factory can supply a supplier for the given mime type
//   */
//  default boolean respondsTo(String mimeType) {
//    return getRespondsToTypes().contains(requireNonNull(mimeType));
//  }
//
//  List<String> getRespondsToTypes();
//
//  /**
//   * GEt the supplier from the stream
//   * @param ds
//   * @return a supplier of some type.  IF this factory does not respondsTyp that streams's type, return empty()
//   */
//  Optional<IBDataSpecificStreamFactory> from(IBDataStream ds);
//
//}
