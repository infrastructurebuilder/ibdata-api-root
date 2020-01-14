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

import java.util.Date;
import java.util.List;
import java.util.SortedMap;

import org.codehaus.plexus.configuration.xml.XmlPlexusConfiguration;
import org.infrastructurebuilder.data.ingest.IBDataSchemaIngestionConfig;
import org.infrastructurebuilder.data.model.DataSet;
import org.infrastructurebuilder.util.config.ConfigMap;

public class FakeIBTransformation implements IBTransformation {

  private DataSet ds;
  private String id;
  private String name;
  private String desc;
  private XmlPlexusConfiguration md;

  public FakeIBTransformation(DataSet ds) {
    this.ds = ds;
  }
  // TODO Auto-generated constructor stub

  public FakeIBTransformation() {
    this(null, "some name", "some description", null);
  }

  public FakeIBTransformation(String id, String name, String desc, XmlPlexusConfiguration xmlPlexusConfiguration) {
    this.id = id;
    this.name = name;
    this.desc = desc;
    this.md = xmlPlexusConfiguration;
    this.ds = new DataSet();
    ds.setUuid(id);
    ds.setName(name);
    ds.setDescription(desc);
    ds.setMetadata(xmlPlexusConfiguration);
    ds.setCreationDate(new Date());
  }

  @Override
  public DataSet asDataSet() {
    return new DataSet();
  }

  @Override
  public SortedMap<String, IBDataSchemaIngestionConfig> asSchemaIngestion() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDescription() {
    return desc;
  }

  @Override
  public String getId() {
    // TODO Auto-generated method stub
    return id;
  }

  @Override
  public List<Transformer> getTransformers() {
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
  public void forceDefaults(String groupId, String artifactId, String version) {
    // TODO Auto-generated method stub

  }

}
