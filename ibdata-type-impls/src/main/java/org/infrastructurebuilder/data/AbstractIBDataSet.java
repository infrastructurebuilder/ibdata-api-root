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
package org.infrastructurebuilder.data;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

abstract public class AbstractIBDataSet implements IBDataSet {

  private final String id;
  private final Instant creationDate;
  private final Metadata metadata;
  private final String name;
  private final String description;
  private final String path;
  private final String groupId, artifactId, version;

  private Path underlyingPath = null;

  protected AbstractIBDataSet(IBDataSet set) {
    this(requireNonNull(set).getId(), set.getCreationDate(), set.getMetadata(), set.getName(), set.getDescription(),
        set.getLocalPath().map(Path::toString), set.getGroupId(), set.getArtifactId(), set.getVersion());
  }

  public AbstractIBDataSet(UUID id, Instant date, Metadata metadata, String name, Optional<String> description,
      Optional<String> path, String groupId, String artifactId, String version) {
    this.id = requireNonNull(id, getClass().getCanonicalName() + "." + "id").toString();
    this.creationDate = requireNonNull(date, getClass().getCanonicalName() + "." + "creationDate");
    this.metadata = requireNonNull(metadata, getClass().getCanonicalName() + "." + "metadata");
    this.name = requireNonNull(name, getClass().getCanonicalName() + "." + "name");
    this.description = requireNonNull(description, getClass().getCanonicalName() + "." + "description").orElse(null);
    this.path = requireNonNull(path, getClass().getCanonicalName() + "." + "path").orElse(null);
    this.groupId = requireNonNull(groupId, getClass().getCanonicalName() + "." + "groupId");
    this.artifactId = requireNonNull(artifactId, getClass().getCanonicalName() + "." + "artifactId");
    this.version = requireNonNull(version, getClass().getCanonicalName() + "." + "version");
  }

  @Override
  public Instant getCreationDate() {
    return this.creationDate;
  }

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public Optional<String> getDescription() {
    return ofNullable(this.description);

  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public String getPath() {
    return this.path.orElse(underLyingPath().map(Path::toString).orElse(null));
  }

  @Override
  public String getGroupId() {
    return this.groupId;
  }

  @Override
  public String getArtifactId() {
    return this.artifactId;
  }

  @Override
  public String getVersion() {
    return this.version;
  }

  @Override
  public Metadata getMetadata() {
    return this.metadata;
  }

  protected AbstractIBDataSet setUnderlyingPath(Path p) {
    this.underlyingPath = p;
    return this;
  }

  private Optional<Path> underLyingPath() {
    return ofNullable(underlyingPath);
  }

}
