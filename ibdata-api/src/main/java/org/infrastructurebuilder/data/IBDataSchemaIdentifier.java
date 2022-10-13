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
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.infrastructurebuilder.util.core.IBUtils.nullSafeStringComparator;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.infrastructurebuilder.util.core.Checksum;
import org.infrastructurebuilder.util.core.ChecksumBuilder;
import org.infrastructurebuilder.util.core.ChecksumEnabled;

/**
 * An IBDataSchema is a data schema definition for some quantity of data that
 * could be provided by IBData. It consists of multiple assets, each of which
 * define elements of such data, and each of which are produced uniquely via the
 * system.
 *
 * An IBDataSchema is backed by a persistent model which <b><i>technically
 * could</i></b> be queried by fetching the W3C Document value for the schema,
 * but the internal representations of that model are not designed for
 * consumption outside of IBData.
 *
 * Producing a new type within IBData is neither trivial nor difficult. The
 * provider specifies what degree of provision it will produce and what
 * mechanisms it can provide for intercommunication. IBData handles the
 * translation of that type based of weighted efficiency of translations.
 *
 * @author mykel.alvis
 *
 */
public interface IBDataSchemaIdentifier extends ChecksumEnabled {
  public final static Comparator<IBDataSchemaIdentifier> comparator = new Comparator<>() {
    @Override
    public int compare(IBDataSchemaIdentifier l, IBDataSchemaIdentifier r) {
      // TODO Auto-generated method stub
      return nullSafeStringComparator.compare(l.getId(), ofNullable(r).map(IBDataSchemaIdentifier::getId).orElse(null));
    }
  };
  public static final String PRIMARY = "ibdata-primary-schema";
  /**
   * Returns persisted UUID from asChecksum
   * @return
   */
  UUID getUuid();
  /**
   * getId returns an unpersisted identifier only for ingestion purposes.  Do not depend on this value post-persistence.
   * @return
   */
  String getId();

  String getName();

  String getDescription();

  Date getCreationDate();

  IBSchemaType getSchemaType();

  Metadata getMetadata();

  /**
   * Map of String identifiers to String URLs that identify resources produced
   * during the transformation
   *
   * @return
   */
  List<IBDataSchemaAsset> getSchemaAssets();

  @Override
  default Checksum asChecksum() {
    return ChecksumBuilder.newInstance() //
        .addString(getName()) // name
        .addString(getDescription()) // desc
        .addDate(getCreationDate()) // create date
        .addChecksumEnabled(getSchemaType()) // schema type
        .addChecksumEnabled(getMetadata()) //
        .addListChecksumEnabled(getSchemaAssets().stream().collect(toList())) // assets
        // fin
        .asChecksum();
  }

  default List<IBDataStreamSupplier> getSuppliedDataStreams() {
    throw new IBDataException("getSuppliedDataStreams not implemented in the model or by default.  It must be overriden to supply");
  }

  default Optional<String> getCredentialsQuery() {
    return empty();
  }

}
