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

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.infrastructurebuilder.data.IBDataException.cet;
import static org.infrastructurebuilder.data.IBMetadataUtils.translateToMetadata;
import static org.infrastructurebuilder.util.core.IBUtils.nullSafeURLMapper;

import java.io.StringReader;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.codehaus.plexus.configuration.xml.XmlPlexusConfiguration;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.data.Metadata;

public class DefaultIBDataSchemaIngestionConfigBean implements IBDataSchemaIngestionConfig {

  private static final String ELEMENT = "Only a single element of inline, files or schemaQuery is allowed";

  private String temporaryId = "default";
  private String name;
  private String description;
  private XmlPlexusConfiguration metadata;
  private List<String> urls;
  private String serverId;
  private IBJDBCQuery databaseQuery;
  private XmlPlexusConfiguration inline;
//  private SchemaQueryBean schemaQuery;

  public DefaultIBDataSchemaIngestionConfigBean() {
    super();
  }

  public DefaultIBDataSchemaIngestionConfigBean(IBDataSchemaIngestionConfig i) {
    this();
    this.temporaryId = i.getTemporaryId();
    this.name = i.getName().orElse(null);
    this.description = i.getDescription().orElse(null);
    this.metadata = new XmlPlexusConfiguration(i.getMetadata());
    this.urls = i.getUrls().map(l -> l.stream().map(URL::toExternalForm).collect(toList())).orElse(null);
    this.serverId = i.getCredentialsQuery().orElse(null);
    this.databaseQuery = i.getJDBCQuery().map(IBJDBCQuery::new).orElse(null);
    this.inline = i.getInline().map(XmlPlexusConfiguration::new).orElse(null);
  }

  public void setTemporaryId(String temporaryId) {
    this.temporaryId = temporaryId;
  }

  @Override
  public String getTemporaryId() {
    return temporaryId;
  }

  public void setMetadata(XmlPlexusConfiguration metadata) {
    this.metadata = requireNonNull(metadata);
  }

  @Override
  public Metadata getMetadata() {
    return translateToMetadata.apply(metadata);
  }

//  public void setSchemaQuery(SchemaQueryBean schemaQuery) {
//    if (this.urls != null || this.inline != null)
//      throw new IBDataException(ELEMENT);
//    this.schemaQuery = schemaQuery;
//  }

  public void setInline(XmlPlexusConfiguration inline) {
    if (this.urls != null /* || this.schemaQuery != null */)
      throw new IBDataException(ELEMENT);
    this.inline = inline;
    Xpp3Dom[] c = getInline().get().getChildren();
    if (c.length != 1)
      throw new IBDataException("Inline config must be a single <schema/>");
    if (!"schema".equals(c[0].getName()))
      throw new IBDataException("Inline config must be a <schema/>");
  }

  @Override
  public Optional<Xpp3Dom> getInline() {
    return ofNullable(inline).map(XmlPlexusConfiguration::toString).map(StringReader::new)
        .map(s -> cet.withReturningTranslation(() -> Xpp3DomBuilder.build(s)));
  }

  /*
   * @Override public Optional<SchemaQueryBean> getSchemaQuery() { return
   * ofNullable(schemaQuery); }
   *
   */
  public void setUrls(List<String> urls) {
    if (/* this.schemaQuery != null || */ this.inline != null)
      throw new IBDataException(ELEMENT);
    this.urls = urls;
  }

  @Override
  public Optional<List<URL>> getUrls() {
    return ofNullable(urls).map(
        f -> f.stream().map(v -> nullSafeURLMapper.apply(v).orElseThrow(() -> new IBDataException("Invalid URL " + v)))
            .collect(toList()));
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDescription(String description) {
    this.description = description;
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
  public int hashCode() {
    return Objects.hash(description, urls, inline, getMetadata(), name, temporaryId, databaseQuery/* , schemaQuery */);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof DefaultIBDataSchemaIngestionConfigBean))
      return false;
    DefaultIBDataSchemaIngestionConfigBean other = (DefaultIBDataSchemaIngestionConfigBean) obj;
    return Objects.equals(description, other.description) && Objects.equals(urls, other.urls)
        && Objects.equals(inline, other.inline) && Objects.equals(metadata, other.metadata)
        && Objects.equals(name, other.name) // && Objects.equals(schemaQuery, other.schemaQuery)
        && Objects.equals(databaseQuery, other.databaseQuery) && Objects.equals(temporaryId, other.temporaryId);
  }

  @Override
  public String toString() {
    StringBuilder builder = toStringSupplier(this.getClass()).get();
    return builder
        // Add files? Maybe?
        .append(", files=").append(urls)
        // final closer
        .append("]")
        //
        .toString();
  }

  public void setDatabaseQuery(IBJDBCQuery databaseQuery) {
    this.databaseQuery = databaseQuery;
  }

  @Override
  public Optional<IBJDBCQuery> getJDBCQuery() {
    return ofNullable(this.databaseQuery);
  }

  public void setServerId(String credentialsQuery) {
    this.serverId = credentialsQuery;
  }

  @Override
  public Optional<String> getCredentialsQuery() {
    return ofNullable(this.serverId);
  }
}
