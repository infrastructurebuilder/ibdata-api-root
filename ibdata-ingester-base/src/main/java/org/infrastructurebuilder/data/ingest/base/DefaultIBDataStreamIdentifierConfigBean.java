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
package org.infrastructurebuilder.data.ingest.base;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.infrastructurebuilder.data.IBMetadataUtils.translateToMetadata;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.codehaus.plexus.configuration.xml.XmlPlexusConfiguration;
import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.data.IBDataProvenance;
import org.infrastructurebuilder.data.Metadata;
import org.infrastructurebuilder.data.model.DataStream;
import org.infrastructurebuilder.data.model.DataStreamStructuredMetadata;
import org.infrastructurebuilder.util.core.Checksum;;

public class DefaultIBDataStreamIdentifierConfigBean extends DataStream {
  private static final long serialVersionUID = -4944685799856848753L;
  private boolean expandArchives;
  private SchemaQueryBean schemaQuery = null;
  private IBJDBCQuery databaseQuery;

  public DefaultIBDataStreamIdentifierConfigBean() {
  }

  public DefaultIBDataStreamIdentifierConfigBean(DataStream o) {
    super(o);
    this.setProvenance(null);
    this.setStructuredDataDescriptor(new DataStreamStructuredMetadata());
    this.setUuid(null); // Has to be set at finalization time
    if (o instanceof DefaultIBDataStreamIdentifierConfigBean) {
      DefaultIBDataStreamIdentifierConfigBean p = (DefaultIBDataStreamIdentifierConfigBean) o;
      setTemporaryId(p.getTemporaryId().orElse(null));
      this.expandArchives = p.expandArchives;
      this.schemaQuery = p.getSchemaQuery().map(SchemaQueryBean::copy).orElse(null);
      this.databaseQuery = p.getDatabaseQuery().map(IBJDBCQuery::copy).orElse(null);
    }
  }

  DefaultIBDataStreamIdentifierConfigBean copy() {
    return new DefaultIBDataStreamIdentifierConfigBean(this);
  }

  public void setId(String id) {
    super.setTemporaryId(id);
  }

  public void setMetadata(XmlPlexusConfiguration metadata) {
    super.setMetadata(translateToMetadata.apply(metadata));
  }

  @Override
  public void setSha512(String checksum) {
    if (checksum != null && checksum.length() != 128)
      throw new IBDataException("Sha512s are 128 hex characters");
    super.setSha512(checksum);
  }

  public void setExpandArchives(boolean expandArchives) {
    this.expandArchives = expandArchives;
  }

  @Override
  public boolean isExpandArchives() {
    return expandArchives;
  }

  @Override
  public int hashCode() {
    return 31 * super.hashCode() + Objects.hash(expandArchives, schemaQuery);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (!(obj instanceof DefaultIBDataStreamIdentifierConfigBean))
      return false;
    DefaultIBDataStreamIdentifierConfigBean other = (DefaultIBDataStreamIdentifierConfigBean) obj;
    return expandArchives == other.expandArchives && Objects.equals(schemaQuery, other.schemaQuery);
  }

  /**
   * Configurations can never provide provenance directly. It is inferred from the
   * dependency chain.
   */
  @Override
  public Optional<IBDataProvenance> getProvenance() {
    return empty();
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder().append("TempID" + getTemporaryId())
        .append("DefaultIBDataStreamIdentifierConfigBean SUPER->").append(super.toString());
    builder.append("DefaultIBDataStreamIdentifierConfigBean [expandArchives=").append(expandArchives)
        .append(", schemaQuery=").append(schemaQuery).append("]");
    return builder.toString();
  }

  @Override
  public Checksum getChecksum() {
    /*
     * In the case of an ingested value, this needs to be calculated. Note that this
     * needs to be kept in sync with super.getChecksum() except it returns null
     * instead of throws IBDataException
     */
    return ofNullable(getSha512()).filter(s -> s.length() == 128) // Length of a sha512
        .map(org.infrastructurebuilder.util.core.Checksum::new).orElse(null);
  }

  @Override
  public Metadata getMetadata() {
    return super.getMetadata();
  }

  public void setDatabaseQuery(IBJDBCQuery databaseQuery) {
    this.databaseQuery = databaseQuery;
  }

  public Optional<IBJDBCQuery> getDatabaseQuery() {
    return ofNullable(databaseQuery);
  }

  @Override
  public String getReferencedSchema() {
    if (getSchemaQuery().isPresent()) {
      return getSchemaQuery().map(sq -> {
        List<UUID> l = sq.get();
        if (l.size() != 1)
          throw new IBDataException("List of queried schemas was more than 1 : " + l);
        return l.get(0).toString();
      }).orElseThrow(() -> new IBDataException("Query must return a single schema id"));
    }
    return super.getReferencedSchema();
  }

  public Optional<SchemaQueryBean> getSchemaQuery() {
    return ofNullable(schemaQuery);
  }

  @Override
  public Optional<String> getUrl() {
    if (getDatabaseQuery().isPresent())
      return Optional.of(getDatabaseQuery().get().getUrl());
    return super.getUrl();
  }
}
