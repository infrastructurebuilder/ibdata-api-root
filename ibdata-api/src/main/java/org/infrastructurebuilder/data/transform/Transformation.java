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
package org.infrastructurebuilder.data.transform;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static org.infrastructurebuilder.data.IBMetadataUtils.translateToXpp3Dom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.infrastructurebuilder.data.DataSetEnabled;
import org.infrastructurebuilder.data.model.DataSet;
import org.infrastructurebuilder.util.config.ConfigMap;

public class Transformation implements DataSetEnabled {

  public static final String DEFAULT = "default";
  public static final String DEFAULT_TRANSFORM = "default-transform";
  private String id = DEFAULT;
  private List<Transformer> transformers = new ArrayList<>();
  private String finalizer = null;
  private Map<String, String> finalizerConfig = new HashMap<>();

  // Not set with plugin config
  private String groupId, artifactId, version, name, description;
  private Xpp3Dom metadata;

  @Override
  public String toString() {
    return "Transformation [id=" + id + ", transformers=" + transformers + ", finalizer=" + finalizer + ", groupId="
        + groupId + ", artifactId=" + artifactId + ", version=" + version + ", name=" + name + ", metadata=" + metadata
        + "]";
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setTransformers(List<Transformer> transformers) {
    this.transformers = transformers;
  }

  public void setFinalizer(String finalizer) {
    this.finalizer = finalizer;
  }

  public void setFinalizerConfig(Map<String, String> finalizerConfig) {
    this.finalizerConfig = finalizerConfig;
  }

  public void setMetadata(Object metadata) {
    this.metadata = translateToXpp3Dom.apply(metadata);
  }

  public void setArtifactId(String artifactId) {
    this.artifactId = artifactId;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getId() {
    return id;
  }

  public List<Transformer> getTransformers() {
    return transformers.stream().map(t -> t.copy(this)).collect(Collectors.toList());
  }

  public String getFinalizer() {
    return ofNullable(finalizer).orElse(DEFAULT_TRANSFORM);
  }

  public ConfigMap getFinalizerConfig() {
    return new ConfigMap(finalizerConfig.entrySet().stream().collect(toMap(k -> k.getKey(), v -> v.getValue())));

  }

  public void forceDefaults(String groupId, String artifactId, String version) {
    setGroupId(groupId);
    setArtifactId(artifactId);
    setVersion(version);
    if (this.metadata == null)
      this.metadata = new Xpp3Dom("metadata"); // FIXME Where do we make metadata happen for a transformer
  }

  @Override
  public DataSet asDataSet() {
    DataSet dsi = new DataSet();
    dsi.setGroupId(this.groupId);
    dsi.setArtifactId(this.artifactId);
    dsi.setVersion(this.version);
    dsi.setDataSetName(this.name);
    dsi.setDataSetDescription(this.description);
    dsi.setMetadata(metadata);
    return dsi;
  }

}