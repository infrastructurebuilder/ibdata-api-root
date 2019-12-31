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
import static java.util.Optional.ofNullable;
import static org.infrastructurebuilder.data.IBMetadataUtils.translateToXpp3Dom;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.codehaus.plexus.configuration.xml.XmlPlexusConfiguration;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.data.IBDataProvenance;
import org.infrastructurebuilder.data.IBDataStreamIdentifier;
import org.infrastructurebuilder.data.IBDataStructuredDataMetadata;
import org.infrastructurebuilder.data.IBMetadataUtils;
import org.infrastructurebuilder.data.IBSchema;
import org.infrastructurebuilder.util.artifacts.Checksum;
import org.w3c.dom.Document;

public class DefaultIBDataStreamIdentifierConfigBean implements IBDataStreamIdentifier {

  private String temporaryId;
  private String name;
  private String sha512;
  private String url;
  private XmlPlexusConfiguration metadata;
  private String mimeType;
  private String description;
  private String path;
  private Date creationDate;
  private UUID id;
  private boolean expandArchives;
  private DefaultSchemaQueryBean schemaQuery = new DefaultSchemaQueryBean();


  public DefaultIBDataStreamIdentifierConfigBean() {
  }

  public DefaultIBDataStreamIdentifierConfigBean(DefaultIBDataStreamIdentifierConfigBean o) {
    this.temporaryId = o.temporaryId;
    this.name = o.name;
    this.sha512 = o.sha512;
    this.url = o.url;
    this.metadata = o.metadata; // FIXME Make a copy!
    this.mimeType = o.mimeType;
    this.description = o.description;
    this.path = o.path;
    this.creationDate = o.creationDate;
    this.id = o.id;
  }

  DefaultIBDataStreamIdentifierConfigBean copy() {
    return new DefaultIBDataStreamIdentifierConfigBean(this);
  }

  public void setId(String id) {
    this.temporaryId = id;
  }

  public String getTemporaryId() {
    return temporaryId;
  }

  public void setTemporaryId(String temporaryId) {
    this.temporaryId = temporaryId;
  }

  @Override
  public Optional<String> getURL() {
    return ofNullable(url);
  }

  @Override
  public Optional<String> getName() {
    return ofNullable(name);
  }

  @Override
  public Optional<String> getDescription() {
    return ofNullable(description);
  }

  @Override
  public Document getMetadata() {
    return IBMetadataUtils.fromXpp3Dom.apply(this.metadata);
  }

  @Override
  public String getMimeType() {
    return mimeType;
  }

  /**
   * Overrides the (non-nullable) getChecksum() so that we can have null checksums
   * here
   */
  @Override
  public Checksum getChecksum() {
    return (sha512 != null) ? new Checksum(sha512) : null;
  }

  @Override
  public String getSha512() {
    return sha512;
  }

  @Override
  public Date getCreationDate() {
    if (this.creationDate == null)
      this.creationDate = new Date();
    return this.creationDate;
  }

  @Override
  public UUID getId() {
    if (this.id == null)
      this.id = UUID.randomUUID();
    return this.id;
  }

  public void setMetadata(XmlPlexusConfiguration metadata) {
    this.metadata = metadata;
  }

  @Override
  public String getPath() {
    return this.path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setSha512(String checksum) {
    if (checksum != null && checksum.length() != 128)
      throw new IBDataException("Sha512s are 128 hex characters");
    this.sha512 = checksum;
  }

  public void setExpandArchives(boolean expandArchives) {
    this.expandArchives = expandArchives;
  }

  public boolean isExpandArchives() {
    return expandArchives;
  }

  @Override
  public int hashCode() {
    return Objects.hash(creationDate, description, id, metadata, mimeType, name, path, sha512, temporaryId, url);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    DefaultIBDataStreamIdentifierConfigBean other = (DefaultIBDataStreamIdentifierConfigBean) obj;
    return Objects.equals(getCreationDate(), other.getCreationDate())
        && Objects.equals(getDescription(), other.getDescription()) && Objects.equals(getId(), other.getId())
        && Objects.equals(getMetadata().toString(), other.getMetadata().toString())
        && Objects.equals(getMimeType(), other.getMimeType()) && Objects.equals(getName(), other.getName())
        && Objects.equals(getPath(), other.getPath()) && Objects.equals(getChecksum(), other.getChecksum())
        && Objects.equals(getTemporaryId(), other.getTemporaryId()) && Objects.equals(url, other.url);
  }

  @Override
  public Optional<IBDataStructuredDataMetadata> getStructuredDataMetadata() {
    return empty();
  }

  @Override
  public String getOriginalLength() {
    return null; // Not yet determinable
  }

  @Override
  public String getOriginalRowCount() {
    return null; // Not yet determinable
  }

  @Override
  public Optional<IBSchema> getSchema() {
    Optional<UUID> schemaId = this.schemaQuery.get();
    return empty(); // FIXME Lookup the schema
  }

  /**
   * Configurations can never provide provenance directly.  It is inferred from the dependency chain.
   */
  @Override
  public Optional<IBDataProvenance> getProvenance() {
    return empty();
  }

  @Override
  public Optional<UUID> getReferencedSchemaId() {
    // TODO Get referenced Schema Id for a given config bean
    return empty();
  }

}
