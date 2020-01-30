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

import java.util.Objects;
import java.util.Optional;

import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.util.BasicCredentials;
import org.infrastructurebuilder.util.CredentialsFactory;
import org.infrastructurebuilder.util.URLAndCreds;

public class IBJDBCQuery implements URLAndCreds {

  private String url; // jdbc url
  private String serverId; // from settings
  private String table;
  private String where;

  public IBJDBCQuery() {
  }


  public IBJDBCQuery(String url2, String serverId2, String table2, String where2) {
    this.url = url2;
    this.serverId = serverId2;
    this.table = table2;
    this.where = where2;
  }


  public IBJDBCQuery copy(){
    return new IBJDBCQuery(url, serverId, table, where);
  }
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
    builder.append("IBJDBCQuery [")
    .append("url=").append(url)
    .append(", table=").append(table)
    .append(", where=").append(where)
    .append(", serverId=").append(serverId)
    .append("]");
    return builder.toString();
  }

  public void setTable(String table) {
    this.table = table;
  }

  public void setWhere(String where) {
    this.where = where;
  }

  public Optional<String> getWhere() {
    return ofNullable(where);
  }

  public String getTable() {
    return requireNonNull(table, "table name is required");
  }

  public Optional<String> getQuery() {
    return ofNullable(where);
  }


  @Override
  public Optional<String> getCredentialsQuery() {
    return getServerId();
  }
}
