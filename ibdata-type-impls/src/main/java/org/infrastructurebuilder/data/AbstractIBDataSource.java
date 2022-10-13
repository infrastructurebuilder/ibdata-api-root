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

import java.util.Optional;

import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.core.Checksum;
import org.infrastructurebuilder.util.credentials.basic.BasicCredentials;
import org.slf4j.Logger;
import org.w3c.dom.Document;

abstract public class AbstractIBDataSource implements IBDataSource {

  protected final String id;
  protected final String source;
  protected final Optional<BasicCredentials> creds;
  protected final Optional<Checksum> checksum;
  protected final Optional<Document> metadata;
  protected final Optional<ConfigMap> additionalConfig;
  protected final Optional<String> name;
  protected final Optional<String> desc;
  private final Logger logger;
  private final boolean expandArchives;

  public AbstractIBDataSource(Logger logger, String id, String source, boolean expand, Optional<String> name, Optional<String> desc, Optional<BasicCredentials> creds,
      Optional<Checksum> checksum, Optional<Document> metadata, Optional<ConfigMap> config) {
    super();
    this.id = requireNonNull(id);
    this.source = requireNonNull(source);
    this.creds = requireNonNull(creds);
    this.checksum = requireNonNull(checksum);
    this.additionalConfig = requireNonNull(config);

    this.metadata = requireNonNull(metadata);
    this.logger = requireNonNull(logger);
    this.name = requireNonNull(name);
    this.desc = requireNonNull(desc);
    this.expandArchives = expand;
  }

  @Override
  public Logger getLog() {
    return this.logger;
  }

  @Override
  public String getSourceURL() {
    return source;
  }

  @Override
  public Optional<BasicCredentials> getCredentials() {
    return creds;
  }

  @Override
  public Optional<Checksum> getChecksum() {
    return checksum;
  }

  @Override
  public Optional<Document> getMetadata() {
    return metadata;
  }

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public Optional<ConfigMap> getAdditionalConfig() {
    return this.additionalConfig;
  }

  @Override
  public Optional<String> getName() {
    return this.name;
  }

  @Override
  public Optional<String> getDescription() {
    return this.desc;
  }

  @Override
  public boolean isExpandArchives() {
    return this.expandArchives;
  }

  /**
   * Override this to acquire additional configuration  OR ELSE IT NEVER HAPPENED!
   */
  @Override
  public IBDataSource withAdditionalConfig(ConfigMap config) {
    return this;
  }
}