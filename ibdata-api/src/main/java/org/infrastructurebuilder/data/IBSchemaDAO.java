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
package org.infrastructurebuilder.data;

import static java.util.Optional.empty;
import static org.infrastructurebuilder.IBConstants.DEFAULT;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.infrastructurebuilder.data.model.io.xpp3.PersistedIBSchemaXpp3Reader;

public interface IBSchemaDAO extends Supplier<Map<String, IBDataStreamSupplier>> {

  default IBSchema getSchema() {
    try (InputStream ins = get().get(getPrimaryAssetKeyName()).get().get()) {
      return getReader().read(ins);
    } catch (IOException | XmlPullParserException e) {
      throw new IBDataException(e);
    }
  }

  default public String getPrimaryAssetKeyName() {
    return DEFAULT;
  }

  default public Optional<String> getOriginalAssetKeyName() {
    return empty();
  }

  default Optional<IBSchemaSource<?>> getSource() {
    return empty();
  }

  default PersistedIBSchemaXpp3Reader getReader() {
    return new PersistedIBSchemaXpp3Reader();
  }
}
