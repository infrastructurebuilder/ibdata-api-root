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

import static java.util.Collections.emptyList;
import static java.util.Collections.synchronizedSortedMap;
import static java.util.Objects.requireNonNull;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.infrastructurebuilder.IBConstants.DEFAULT;
import static org.infrastructurebuilder.data.IBMetadataUtils.translateToMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.codehaus.plexus.configuration.xml.XmlPlexusConfiguration;
import org.infrastructurebuilder.data.model.DataSchema;
import org.infrastructurebuilder.data.model.DataSet;
import org.infrastructurebuilder.data.model.DataStream;
import org.infrastructurebuilder.util.CredentialsFactory;;

/**
 * Configuration Bean for plugin
 *
 * Inbound metadata is XmlPlexusConfiguration transformed to Metadata (because
 * it's easy)
 *
 * @author mykel.alvis
 *
 */
public class DefaultIBDataSetIdentifier extends DataSet {
  private static final long serialVersionUID = -7357725622978715720L;
  private List<DefaultIBDataStreamIdentifierConfigBean> dataStreams = new ArrayList<>();
  private List<DefaultIBDataSchemaIngestionConfigBean> dataSchemas = new ArrayList<>();

  @SuppressWarnings("unused") // Used to type the inbound setter
  private XmlPlexusConfiguration metadata;
  private CredentialsFactory factory;

  public DefaultIBDataSetIdentifier() {
    super();
    setName("default");
  }

  private DefaultIBDataSetIdentifier(DefaultIBDataSetIdentifier i) {
    super(i);
    setName(i.getName().orElse(DEFAULT));
    this.dataStreams = i.getDataStreams().stream().map(DefaultIBDataStreamIdentifierConfigBean::new).collect(toList());
    this.dataSchemas = i.getDataSchemas().stream().map(DefaultIBDataSchemaIngestionConfigBean::new).collect(toList());
  }

  DefaultIBDataSetIdentifier setFactory(CredentialsFactory factory) {
    this.factory = factory;
    return this;
  }
  /**
   * Inbound DataStreams may set the "referencedSchema" to be the temporaryId of
   * an inbound schema. This will all be cleared up during the ingestion
   * finalization. As that value is not a UUID, it must be obtained using
   * {@link DataStream#getReferencedSchema()} and not
   * {@link DataStream#getReferencedSchemaId()}
   */
  @Override
  public void setStreams(List<DataStream> streams) {
    setDataStreams(streams.stream().map(DefaultIBDataStreamIdentifierConfigBean::new).collect(toList()));
  }

  @Override
  public List<DataStream> getStreams() {
    return getDataStreams().stream().collect(toList());
  }

  /**
   * Not viable during ingestion at present time
   */
  @Override
  public List<DataSchema> getSchemas() {
    return emptyList();
  }

  public List<DefaultIBDataStreamIdentifierConfigBean> getDataStreams() {
    return dataStreams.stream().map(DefaultIBDataStreamIdentifierConfigBean::new).collect(toList());
  }

  public List<DefaultIBDataSchemaIngestionConfigBean> getDataSchemas() {
    return dataSchemas.stream().collect(toList());
  }

  public void setMetadata(XmlPlexusConfiguration metadata) {
    super.setMetadata(translateToMetadata.apply(metadata));
  }

  public void setDataStreams(List<DefaultIBDataStreamIdentifierConfigBean> streams) {
    this.dataStreams = requireNonNull(streams);
  }

  public void setDataSchemas(List<DefaultIBDataSchemaIngestionConfigBean> dataSchemas) {
    this.dataSchemas = requireNonNull(dataSchemas).stream().collect(toList());
  }

  public final DefaultIBDataSetIdentifier injectGAV(String groupId, String artifactId, String version) {
    setGroupId(requireNonNull(groupId));
    setArtifactId(requireNonNull(artifactId));
    setVersion(requireNonNull(version));
    return this;
  }

  public DataSet asDataSet() {
    DataSet ds = new DataSet();
    // Use getters of this object to set these, please
    ds.setGroupId(getGroupId());
    ds.setArtifactId(getArtifactId());
    ds.setVersion(getVersion());
    ds.setName(getName().orElse(null));
    ds.setDescription(getDescription().orElse(null));
    ds.setMetadata(getMetadata());
    ds.setPath(getLocalPath().orElse(null));
    ds.setCreationDate(getCreationDate());
    ds.setStreams(getStreams());
    ds.setSchemas(getSchemas());
//        ds.setSchemas(getDataSchemas().stream().map(DefaultIBDataSchemaIngestionConfig::new).collect(toList()))
    ;
    return ds;
  }

  DefaultIBDataSetIdentifier copy() {
    return new DefaultIBDataSetIdentifier(this);
  }

  @Override
  public String toString() {
    final int maxLen = 10;
    StringBuilder builder = new StringBuilder();
    builder.append("DefaultIBDataSetIdentifier [ ").append("id = ").append(getUuid()).append(", dataStreams=")
        .append(dataStreams != null ? dataStreams.subList(0, Math.min(dataStreams.size(), maxLen)) : null)
        .append(", dataSchemas=")
        .append(dataSchemas != null ? dataSchemas.subList(0, Math.min(dataSchemas.size(), maxLen)) : null)
        .append(", metadata=").append(metadata).append("]");
    return builder.toString();
  }

  public SortedMap<String, IBDataSchemaIngestionConfig> asSchemaIngestion() {
    return dataSchemas.stream().collect(toMap(k -> k.getTemporaryId(), identity(), (left, right) -> {
      throw new RuntimeException(String.format("Duplicate key for %s and %s", left, right));
    }, () -> synchronizedSortedMap(new TreeMap<>())));
  }

}
