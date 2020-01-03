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
package org.infrastructurebuilder.data.ingest;

import static java.util.Optional.empty;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.codehaus.plexus.configuration.xml.XmlPlexusConfiguration;
import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.data.IBDataProvenance;
import org.infrastructurebuilder.data.IBDataStreamIdentifier;
import org.infrastructurebuilder.data.IBMetadataUtils;
import org.infrastructurebuilder.data.model.DataStream;
import org.infrastructurebuilder.data.model.DataStreamStructuredMetadata;;

public class DefaultIBDataStreamIdentifierConfigBean extends DataStream implements IBDataStreamIdentifier {

  private boolean expandArchives;
  private DefaultSchemaQueryBean schemaQuery = new DefaultSchemaQueryBean();

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
      this.schemaQuery = p.schemaQuery;
    }
  }

  DefaultIBDataStreamIdentifierConfigBean copy() {
    return new DefaultIBDataStreamIdentifierConfigBean(this);
  }

  public void setId(String id) {
    super.setTemporaryId(id);
  }

  public void setMetadata(XmlPlexusConfiguration metadata) {
    super.setMetadata(IBMetadataUtils.translateToXpp3Dom.apply(metadata));
  }

  public void setSha512(String checksum) {
    if (checksum != null && checksum.length() != 128)
      throw new IBDataException("Sha512s are 128 hex characters");
    super.setSha512(checksum);
  }

  public void setExpandArchives(boolean expandArchives) {
    this.expandArchives = expandArchives;
  }

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
    StringBuilder builder = new StringBuilder().append("TempID" + getTemporaryId()).append("DefaultIBDataStreamIdentifierConfigBean SUPER->").append(super.toString());
    builder.append("DefaultIBDataStreamIdentifierConfigBean [expandArchives=").append(expandArchives)
        .append(", schemaQuery=").append(schemaQuery).append("]");
    return builder.toString();
  }

}
