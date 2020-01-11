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

import static java.util.stream.Collectors.toList;
import static org.infrastructurebuilder.data.IBDataConstants.RECORD_SPLITTER;
import static org.infrastructurebuilder.data.IBDataConstants.TRANSFORMERSLIST;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.config.DefaultConfigMapSupplier;

public class RecordTransformer extends Transformer {

  private List<Record> records = new ArrayList<>();
  private String recordFinalizer;
  private ConfigMap recordFinalizerConfig = new ConfigMap();

  public RecordTransformer() {
    this(null, null);
  }

  protected RecordTransformer(RecordTransformer recordTransformer, IBTransformation t) {
    super(recordTransformer, t);
    if (recordTransformer != null) {
      this.recordFinalizer = recordTransformer.recordFinalizer;
      this.records = recordTransformer.records.stream().collect(toList());
    }
  }

  public void setRecords(List<Record> records) {
    this.records = records;
  }

  public void setRecordFinalizer(String recordFinalizer) {
    this.recordFinalizer = recordFinalizer;
  }

  public void setRecordFinalizerConfig(ConfigMap recordFinalizerConfig) {
    this.recordFinalizerConfig = recordFinalizerConfig;
  }

  public String getRecordFinalizer() {
    return recordFinalizer;
  }

  public List<Record> getRecords() {
    return records;
  }

  public ConfigMapSupplier getRecordFinalizerConfig(ConfigMapSupplier defaults) {
    return new DefaultConfigMapSupplier(getConfigurationAsConfigMapSupplier(defaults))
        .overrideConfiguration(this.recordFinalizerConfig);
  }

  public ConfigMapSupplier getConfigurationAsConfigMapSupplier(ConfigMapSupplier defaults) {
    final DefaultConfigMapSupplier cmap = new DefaultConfigMapSupplier(
        super.getConfigurationAsConfigMapSupplier(defaults));
    final StringJoiner sj = new StringJoiner(RECORD_SPLITTER);
    records.forEach(record -> {
      cmap.addConfiguration(record.configurationAsMap()); // safe because addConfiguration returns "this"
      sj.add(record.joinKey());
    });
    cmap.addValue(TRANSFORMERSLIST, sj.toString());
    return cmap;
  }

  @Override
  public RecordTransformer copy(IBTransformation t) {
    return new RecordTransformer(this, t);
  }

}
