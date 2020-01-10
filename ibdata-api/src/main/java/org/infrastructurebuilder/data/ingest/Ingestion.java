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

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.Map;

import org.infrastructurebuilder.data.DataSetEnabled;
import org.infrastructurebuilder.data.model.DataSet;
import org.infrastructurebuilder.util.config.ConfigMap;

public class Ingestion implements DataSetEnabled {

  private String id = "default";
  private String schemaIngester = null; // "default";
  private String ingester = null; // "default";
  private DefaultIBDataSetIdentifier dataSet = new DefaultIBDataSetIdentifier();
  private String finalizer = null;
  private Map<String, String> finalizerConfig = new HashMap<>();

  public String getId() {
    return id;
  }

  public DefaultIBDataSetIdentifier getDataSet() {
    return dataSet;
  }

  public String getIngester() {
    return ofNullable(this.ingester).orElse(getId());
  }

  public String getSchemaIngester() {
    return ofNullable(this.schemaIngester).orElse(getId());
  }

  public String getFinalizer() {
    return ofNullable(finalizer).orElse("default-ingest");
  }

  public ConfigMap getFinalizerConfig() {
    return new ConfigMap(finalizerConfig.entrySet().stream().collect(toMap(k -> k.getKey(), v -> v.getValue())));
  }

  @Override
  public DataSet asDataSet() {
    return getDataSet().asDataSet();
  }

  public void setDataSet(DefaultIBDataSetIdentifier dataSet) {
    this.dataSet = dataSet;
  }

  boolean isExpand(String tempId) {
    return dataSet.getDataStreams().stream().filter(ds -> ds.getTemporaryId().get().equals(tempId)).findFirst()
        .map(ff -> ff.isExpandArchives()).orElse(false);
  }

  @Override
  public Map<String, IBDataSchemaIngestionConfig> asSchemaIngestion() {
    return getDataSet().asSchemaIngestion();
  }

}
