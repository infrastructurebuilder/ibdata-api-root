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

import java.util.List;

/**
 * An IBDataSchema is a data schema definition for some "full set" of data that
 * could be provided by IBData. It consists of multiple assets, each of which
 * define elements of such data, and each of which are produced uniquely via the
 * system.
 *
 * An IBDataSchema is backed by a persistent model which technically
 * <b><i>technically could</i></b> be queried by fetching the W3C Document value
 * for the schema, but the internal representations of that model are not
 * designed for consumption outside of IBData.
 *
 * Producing a new type within IBData is neither trivial nor difficult. The
 * provider specifies what degree of provision it will produce and what
 * mechanisms it can provide for intercommunication. IBData handles the
 * translation of that type based of weighted efficiency of translations.
 *
 * @author mykel.alvis
 *
 */
public interface IBDataSchema {
  /**
   * Returns the "type" of this schema, which is the hint for its component
   *
   * IBData defines a "default" type, which is actually used as the translation
   * format for various types.
   *
   * @return
   */
  String getId();

  /**
   * Obtain an indicator for the component that provides the underlying technology
   * for this schema type. For instance, "avro", "protobuf"
   *
   * The actual artifact that performs the work should be a versioned provider,
   * and should be queryable for its api version.
   *
   * associated with this schema. Note that IBData is not currently an OSGi
   * application but rather simply expects that a given provider will be generally
   * stable and will conform strictly to Semantic Versioning api principles.
   *
   * @return
   */
  String getTechnology();

  String getVersionedProviderIdentifier();

  /**
   * Map of String identifiers to String URLs that identify resources produced
   * during the transformation
   *
   * @return
   */
  List<IBDataSchemaAsset> getSchemaAssets();

}
