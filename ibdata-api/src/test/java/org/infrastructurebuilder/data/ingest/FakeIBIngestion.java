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

import java.util.SortedMap;

import org.infrastructurebuilder.data.model.DataSet;
import org.infrastructurebuilder.util.config.ConfigMap;

public class FakeIBIngestion implements IBIngestion {

  private final DataSet ds;

  public FakeIBIngestion(DataSet d) {
    this.ds = d;
  }
  @Override
  public DataSet asDataSet() {
    return this.ds;
  }

  @Override
  public SortedMap<String, IBDataSchemaIngestionConfig> asSchemaIngestion() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getId() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public DefaultIBDataSetIdentifier getDataSet() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getIngester() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getSchemaIngester() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getFinalizer() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ConfigMap getFinalizerConfig() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isExpand(String tempId) {
    // TODO Auto-generated method stub
    return false;
  }

}
