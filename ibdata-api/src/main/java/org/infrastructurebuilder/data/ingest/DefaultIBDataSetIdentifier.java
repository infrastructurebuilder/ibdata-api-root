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

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static org.infrastructurebuilder.data.IBMetadataUtils.translateToXpp3Dom;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.data.IBDataSetIdentifier;
import org.infrastructurebuilder.data.IBMetadataUtils;
import org.infrastructurebuilder.data.model.DataSet;
import org.w3c.dom.Document;

/**
 * Configuration Bean for plugin
 *
 * Inbound metadata is XmlPlexusConfiguration transformed to Xpp3Dom (because it's easy)
 * @author mykel.alvis
 *
 */
public class DefaultIBDataSetIdentifier implements IBDataSetIdentifier {

  private String name = null;
  private String description = null;
  private String path = null;
  private UUID id = null;
  private String groupId = null, artifactId = null, version = null; // Not actually params.  Injected by ingestion process

  private List<DefaultIBDataStreamIdentifierConfigBean> streams = new ArrayList<>();
  private Date creationDate;

  private Xpp3Dom metadata;

  public DefaultIBDataSetIdentifier() {
    this.name = "default";
  }

  public DefaultIBDataSetIdentifier(DefaultIBDataSetIdentifier i) {
    this.name = i.getName().orElse("default");
    this.description = i.getDescription().orElse(null);
    this.path = i.getPath();
    this.id = i.getId();
    this.groupId = i.getGroupId();
    this.artifactId = i.getArtifactId();
    this.version = i.getVersion();
    this.metadata = translateToXpp3Dom.apply(i.getMetadata());
    this.streams = i.getStreams().stream().map(DefaultIBDataStreamIdentifierConfigBean::new)
        .collect(Collectors.toList());
  }

  @Override
  public UUID getId() {
    return Optional.ofNullable(this.id).orElse(UUID.randomUUID());
  }

  @Override
  public Optional<String> getName() {
    return ofNullable(this.name);
  }

  @Override
  public Optional<String> getDescription() {
    return ofNullable(this.description);
  }

  @Override
  public Date getCreationDate() {
    if (this.creationDate == null)
      this.creationDate = new Date();
    return this.creationDate;
  }

  @Override
  public Document getMetadata() {
    return IBMetadataUtils.fromXpp3Dom.apply(metadata);
  }

  public List<DefaultIBDataStreamIdentifierConfigBean> getStreams() {

    return streams.stream().collect(Collectors.toList());
  }

  public void setMetadata(Xpp3Dom metadata) {
    this.metadata = translateToXpp3Dom.apply(metadata);
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setStreams(List<DefaultIBDataStreamIdentifierConfigBean> stream2s) {
    this.streams = stream2s;

  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getPath() {
    return this.path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  @Override
  public String getGroupId() {
    return Optional.ofNullable(this.groupId).orElseThrow(() -> new IBDataException("No groupId set"));
  }

  @Override
  public String getArtifactId() {
    return Optional.ofNullable(this.artifactId).orElseThrow(() -> new IBDataException("No artifactId set"));
  }

  @Override
  public String getVersion() {
    return Optional.ofNullable(this.version).orElseThrow(() -> new IBDataException("No version set"));
  }

  public final DefaultIBDataSetIdentifier injectGAV(String groupId, String artifactId, String version) {
    this.groupId = requireNonNull(groupId);
    this.artifactId = requireNonNull(artifactId);
    this.version = requireNonNull(version);
    return this;
  }

  public DataSet asDataSet() {
    DataSet ds = new DataSet();
    // Use getters of this object to set these, please
    ds.setGroupId(getGroupId());
    ds.setArtifactId(getArtifactId());
    ds.setVersion(getVersion());
    ds.setDataSetName(getName().orElse(null));
    ds.setDataSetDescription(getDescription().orElse(null));
    ds.setMetadata(translateToXpp3Dom.apply(getMetadata()));
    ds.setPath(getPath());
    return ds;
  }


}
