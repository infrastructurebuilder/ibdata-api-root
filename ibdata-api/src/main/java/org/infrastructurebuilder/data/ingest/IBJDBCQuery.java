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

import java.util.Optional;

import org.infrastructurebuilder.data.URLAndCreds;
import org.infrastructurebuilder.util.BasicCredentials;
import org.infrastructurebuilder.util.CredentialsFactory;

public class IBJDBCQuery implements URLAndCreds {

  private String url; // jdbc url
  private String serverId; // from settings
  private CredentialsFactory factory;

  public void setServerId(String serverId) {
    this.serverId = serverId;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Optional<String> getServerId() {
    return ofNullable(serverId);
  }

  public String getUrl() {
    return requireNonNull(url, "database query url is required");
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("IBJDBCQuery [url=").append(url).append(", serverId=").append(serverId).append("]");
    return builder.toString();
  }

  IBJDBCQuery setFactory(CredentialsFactory factory) {
    this.factory = factory;
    return this;
  }

  @Override
  public Optional<BasicCredentials> getCreds() {
    return getServerId().flatMap(factory::getCredentialsFor);
  }

}
