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
package org.infrastructurebuilder.data.transform.base;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.infrastructurebuilder.data.IBMetadataUtils.translateToMetadata;
import static org.infrastructurebuilder.util.constants.IBConstants.APPLICATION_OCTET_STREAM;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.infrastructurebuilder.data.IBDataStream;
import org.infrastructurebuilder.data.IBMetadataUtils;
import org.infrastructurebuilder.data.Metadata;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.config.DefaultConfigMapSupplier;
;

public class Transformer implements Cloneable {
  /**
   * Required id, used as the hint if no hint supplied
   */
  private String id;
  private String hint;
  private ConfigMap configuration = new ConfigMap();
  private boolean failOnAnyError = true;
  private List<DataStreamMatcher> sources = new ArrayList<>();
  private String targetMimeType = APPLICATION_OCTET_STREAM;
  private Metadata targetStreamMetadata;
  private final IBTransformation transformation;

  public Transformer() {
    this(null, null);
  }

  protected Transformer(Transformer o, IBTransformation t) {
    if (o!= null) {
      this.id = o.id;
      this.hint = o.hint;
      this.configuration = new ConfigMap(configuration);
      this.failOnAnyError = o.failOnAnyError;
      this.sources = o.sources.stream().collect(toList());
      this.targetMimeType = o.targetMimeType;
      this.targetStreamMetadata  = o.targetStreamMetadata;
    }
    this.transformation = t;
  }



  @Override
  public String toString() {
    return "Transformer [id=" + id + ", hint=" + hint + ", failOnAnyError=" + failOnAnyError + ", targetMimeType="
        + targetMimeType + "]";
  }

  public String getId() {
    return id;
  }

  public String getHint() {
    return Optional.ofNullable(this.hint).orElse(id);
  }

  public ConfigMap getConfiguration() {
    return configuration;
  }

  public void setId(String id) {
    this.id = requireNonNull(id);
  }

  public void setHint(String hint) {
    this.hint = hint;
  }

  public String getTargetMimeType() {
    return targetMimeType;
  }

  public void setTargetMimeType(String targetMimeType) {
    this.targetMimeType = targetMimeType;
  }

  public void setConfiguration(ConfigMap configuration) {
    this.configuration = requireNonNull(configuration);
  }

  public boolean isFailOnAnyError() {
    return failOnAnyError;
  }

  public void setFailOnAnyError(boolean failOnAnyError) {
    this.failOnAnyError = failOnAnyError;
  }

  public ConfigMapSupplier getConfigurationAsConfigMapSupplier(ConfigMapSupplier defaults) {
    return new DefaultConfigMapSupplier(defaults).addConfiguration(this.configuration);
  }

  public List<DataStreamMatcher> getSources() {
    return sources;
  }

  public void setSources(List<DataStreamMatcher> sources) {
    this.sources = sources;
  }

  public void setTargetStreamMetadata(Object targetStreamMetadata) {
    this.targetStreamMetadata = translateToMetadata.apply(targetStreamMetadata);
  }

  public Metadata getTargetStreamMetadata() {
    return targetStreamMetadata;
  }

  public Metadata getTargetStreamMetadataAsDocument() {
    return IBMetadataUtils.translateToMetadata.apply(targetStreamMetadata);
  }

  private boolean matchesSources(IBDataStream stream) {
    return getSources().size() == 0 || getSources().stream().anyMatch(s -> s.matches(stream));
  }

  public List<IBDataStream> asMatchingStreams(Collection<IBDataStream> streams) {
    return requireNonNull(streams).stream().filter(this::matchesSources).collect(toList());
  }

  public Transformer copy(IBTransformation t) {
    return new Transformer(this, t);
  }

  public IBTransformation getTransformation() {
    return transformation;
  }

}
