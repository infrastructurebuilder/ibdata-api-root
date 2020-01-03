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

import java.util.UUID;

import org.codehaus.plexus.configuration.xml.XmlPlexusConfiguration;
import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.data.IBMetadataUtils;
import org.infrastructurebuilder.data.model.DataSchema;
import org.infrastructurebuilder.util.artifacts.Checksum;;

public class DefaultIBDataSchemaIdentifierConfigBean extends DataSchema {
  private static final long serialVersionUID = 1929807860629085740L;

  private DefaultSchemaQueryBean schemaQuery = new DefaultSchemaQueryBean();

  public DefaultIBDataSchemaIdentifierConfigBean() {
    super();
  }

  public DefaultIBDataSchemaIdentifierConfigBean(DataSchema o) {
    super(o);
  }

  DefaultIBDataSchemaIdentifierConfigBean copy() {
    return new DefaultIBDataSchemaIdentifierConfigBean(this);
  }

  @Override
  public UUID getUuid() {
    return ofNullable(getSha512()).map(Checksum::new).flatMap(Checksum::get).orElse(null);
  }

  public void setId(String id) {
    this.setTemporaryId(id);
  }

  public void setMetadata(XmlPlexusConfiguration metadata) {
    super.setMetadata(IBMetadataUtils.translateToXpp3Dom.apply(metadata));
  }

  public void setSchemaQuery(DefaultSchemaQueryBean schemaQuery) {
    this.schemaQuery = schemaQuery;
  }

  public void setSha512(String checksum) {
    if (checksum != null && checksum.length() != 128)
      throw new IBDataException("Sha512s are 128 hex characters");
    super.setSha512(checksum);
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

}
