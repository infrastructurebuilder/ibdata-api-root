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

import static java.util.Optional.empty;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.infrastructurebuilder.data.IBSchemaSource;
import org.infrastructurebuilder.data.Metadata;
import org.infrastructurebuilder.util.BasicCredentials;
import org.infrastructurebuilder.util.core.Checksum;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.files.IBResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FakeIBSchemaSource<P> implements IBSchemaSource<P> {
  public static final String ID = "id";
  public final static Logger log = LoggerFactory.getLogger(FakeIBSchemaSource.class);

  @Override
  public Map<String,IBResource> get() {
    return Collections.emptyMap();
  }

  @Override
  public Logger getLog() {
    return log;
  }

  @Override
  public String getId() {
    return ID;
  }

  @Override
  public Optional<BasicCredentials> getCredentials() {
    return empty();
  }

  @Override
  public Optional<Checksum> getChecksum() {
    return empty();
  }

  @Override
  public Optional<Metadata> getMetadata() {
    return empty();
  }

  @Override
  public Optional<String> getName() {
    return empty();
  }

  @Override
  public Optional<String> getDescription() {
    return empty();
  }

  @Override
  public IBSchemaSource<P> configure(ConfigMap config) {
      return this;
  }

}
