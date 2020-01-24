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

import java.util.Map;
import java.util.Optional;

import org.infrastructurebuilder.util.BasicCredentials;
import org.infrastructurebuilder.util.IBLoggerEnabled;
import org.infrastructurebuilder.util.artifacts.Checksum;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.ConfigurableSupplier;
import org.infrastructurebuilder.util.files.IBResource;;

/**
 * An IBDataSource understands where a data stream originates and how to acquire
 * it. Furthermore, it actually acquires that datastream.
 *
 * An IBDataSource always returns {@link IBResource} which is a path to an
 * acquired file with type and a checksum. The contract for IBDataSource: 1. An
 * IBDataSource should produce the same value for a get() call every time. This
 * means cacheing the results 1. the Path supplied is to the same file every
 * time. 1. The list of {@link IBResource} is called for getMimeType(). This
 * should probably be removed.
 *
 *
 *
 * @author mykel.alvis
 *
 */
public interface IBSchemaSource<P>
    extends ConfigurableSupplier<Map<String,IBResource>, ConfigMap, P>, IBLoggerEnabled {
  /**
   * This is really a descriptive value, although it needs to be unique as well
   *
   * @return
   */
  String getId();

//  String getSourceURL();

  Optional<BasicCredentials> getCredentials();

  Optional<Checksum> getChecksum();

  Optional<Metadata> getMetadata();

  Optional<String> getName();

  Optional<String> getDescription();

  @Override
  IBSchemaSource<P> configure(ConfigMap config);

}
