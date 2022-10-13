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

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.infrastructurebuilder.util.readdetect.IBResource;

/**
 * Returns a writen IBDataSet from the supplied finalizer config type
 * @author mykel.alvis
 *
 * @param <T>
 */
public interface IBDataSetFinalizer<T>  {

  /**
   * Finalize a dataset
   * @param dsi1 The datasets
   * @param target target to send to
   * @param suppliers List of the IBDataStream suppliers used here
   * @param basedir Optional string that is the absolute path of Maven's ${basedir}
   * @return an IBChecksumPathType
   * @throws IOException
   */
  IBResource finalize(IBDataSet dsi1, T target, List<Supplier<IBDataStream>> suppliers, Optional<String> basedir) throws IOException;

  Path getWorkingPath();

}
