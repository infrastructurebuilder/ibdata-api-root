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

import java.util.List;
import java.util.Optional;

import org.infrastructurebuilder.util.artifacts.GAV;

public interface IBDataProvenance {
  /**
   * @return GAV of artifact that initially created this stream
   */
  Optional<GAV> getOriginalSourceArtifact();

  /**
   * @return URL where this stream originated
   */
  Optional<String> getOriginalURL();

  /**
   * @return List of transformation hints applied to get this stream
   */
  Optional<List<String>> getTransformationList();

}
