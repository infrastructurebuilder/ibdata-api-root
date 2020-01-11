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
import static java.util.stream.Collectors.toList;
import static org.infrastructurebuilder.data.IBMetadataUtils.translateToMetadata;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.codehaus.plexus.configuration.xml.XmlPlexusConfiguration;
import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.data.Metadata;;

public class DefaultIBDataSchemaIngestionConfig implements IBDataSchemaIngestionConfig {

  private static final String ELEMENT = "Only a single element of inline, files or schemaQuery is allowed";

  private String temporaryId = "default";
  private String name;
  private String description;
  private XmlPlexusConfiguration metadata;
  private XmlPlexusConfiguration inline;
  private List<File> files;
  private SchemaQueryBean schemaQuery;

  public DefaultIBDataSchemaIngestionConfig() {
    super();
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

  public void setSchemaQuery(SchemaQueryBean schemaQuery) {
    if (this.files != null || this.inline != null)
      throw new IBDataException(ELEMENT);
    this.schemaQuery = schemaQuery;
  }

  public void setInline(XmlPlexusConfiguration inline) {
    if (this.files != null || this.schemaQuery != null)
      throw new IBDataException(ELEMENT);
    this.inline = inline;
  }

  @Override
  public Optional<String> getInline() {
    return ofNullable(inline).map(XmlPlexusConfiguration::toString);
  }

  @Override
  public Optional<SchemaQueryBean> getSchemaQuery() {
    return ofNullable(schemaQuery);
  }

  public void setFiles(List<File> files) {
    if (this.schemaQuery != null || this.inline != null)
      throw new IBDataException(ELEMENT);
    this.files = files;
  }

  @Override
  public Optional<List<Path>> getFiles() {
    return ofNullable(files).map(f -> f.stream().map(File::toPath).collect(toList()));
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
    return Objects.hash(description, files, inline, getMetadata(), name, schemaQuery, temporaryId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof DefaultIBDataSchemaIngestionConfig))
      return false;
    DefaultIBDataSchemaIngestionConfig other = (DefaultIBDataSchemaIngestionConfig) obj;
    return Objects.equals(description, other.description) && Objects.equals(files, other.files)
        && Objects.equals(inline, other.inline) && Objects.equals(metadata, other.metadata)
        && Objects.equals(name, other.name) && Objects.equals(schemaQuery, other.schemaQuery)
        && Objects.equals(temporaryId, other.temporaryId);
  }

  @Override
  public String toString() {
    StringBuilder builder = toStringSupplier(this.getClass()).get();
    return builder
        // Add files?  Maybe?
        .append(", files=").append(files)
        // final closer
        .append("]")
        //
        .toString();
  }



}
