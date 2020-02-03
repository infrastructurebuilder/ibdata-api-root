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

import static java.util.Optional.empty;
import static org.infrastructurebuilder.IBConstants.DESCRIPTION;
import static org.infrastructurebuilder.IBConstants.NAME;
import static org.infrastructurebuilder.IBConstants.TEMPORARYID;
import static org.infrastructurebuilder.data.IBDataConstants.CREDS_QUERY;
import static org.infrastructurebuilder.data.IBDataConstants.METADATA;
import static org.infrastructurebuilder.data.IBDataConstants.SCHEMA;
import static org.infrastructurebuilder.data.IBDataConstants.SCHEMA_QUERY;
import static org.infrastructurebuilder.data.IBDataConstants.URLS;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.infrastructurebuilder.data.Metadata;
import org.infrastructurebuilder.util.config.ConfigMap;

public interface IBDataSchemaIngestionConfig {

  String getTemporaryId();

  Optional<String> getName();

  Optional<String> getDescription();

  Metadata getMetadata();

  Optional<Xpp3Dom> getInline();

  @Deprecated
  default Optional<SchemaQueryBean> getSchemaQuery() {
    return empty();
  }

  Optional<IBJDBCQuery> getJDBCQuery();

  Optional<List<URL>> getUrls();

  Optional<String> getCredentialsQuery();

  default Supplier<StringBuilder> toStringSupplier(Class<?> thisClazz) {
    StringBuilder builder = new StringBuilder();
    builder
        // Class name
        .append(thisClazz.getName())
        // The rest
        .append(" [") // started
        .append("temporaryId=").append(getTemporaryId()) // temp
        .append(", name=").append(getName()) // Name
        .append(", description=").append(getDescription()) // desc
        .append(", metadata=").append(getMetadata()) // meta
        .append(", inline=").append(getInline()) // inline
        .append(", schemaQuery=").append(getSchemaQuery()) // query
    ;
//        .append("]");  // Apply this in the toString method
    return () -> builder;
  }

  default ConfigMap asConfigMap() {
    ConfigMap cm = new ConfigMap();
    cm.put(TEMPORARYID, getTemporaryId());
    getName().ifPresent(n -> cm.put(NAME, n));
    getDescription().ifPresent(d -> cm.put(DESCRIPTION, d));
    cm.put(METADATA, getMetadata());
    getInline().ifPresent(i -> cm.put(SCHEMA, i));
    getUrls().ifPresent(i -> cm.put(URLS, i));
    getSchemaQuery().ifPresent(i -> cm.put(SCHEMA_QUERY, i));
    getCredentialsQuery().ifPresent(cq -> cm.put(CREDS_QUERY, cq));
    return cm;
  }
}