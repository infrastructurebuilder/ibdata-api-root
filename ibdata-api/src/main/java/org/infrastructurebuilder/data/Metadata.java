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
package org.infrastructurebuilder.data;

import static org.infrastructurebuilder.data.model.IBMetadataUtils.translateToMetadata;

import java.util.Objects;
import java.util.function.Supplier;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.infrastructurebuilder.util.core.Checksum;
import org.infrastructurebuilder.util.core.ChecksumBuilder;
import org.infrastructurebuilder.util.core.ChecksumEnabled;
import org.w3c.dom.Document;

public class Metadata extends Xpp3Dom implements ChecksumEnabled {

  public Metadata() {
    super("metadata");
  }

  public Metadata(Xpp3Dom newDom) {
    super(newDom);
  }

  public Metadata(Object o) {
    super(translateToMetadata.apply(o));
  }

  private static final long serialVersionUID = -7709150309904694691L;

  @Override
  public Checksum asChecksum() {
    return ChecksumBuilder.newInstance().addString(toString()).asChecksum();
  }

  public Supplier<Document> asDocumentSupplier() {
    return () -> null;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return Objects.equals(this,obj);
  }

}
