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

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.infrastructurebuilder.util.BasicCredentials;
import org.infrastructurebuilder.util.artifacts.Checksum;
import org.infrastructurebuilder.util.files.IBResource;

public interface WGetter {

  Optional<List<IBResource>> collectCacheAndCopyToChecksumNamedFile(boolean deleteExistingCacheIfPresent,
      Optional<BasicCredentials> creds, Path outputPath, String sourceString, Optional<Checksum> checksum,
      Optional<String> type,  int retries, int readTimeOut, boolean skipCache, boolean expandArchives);

  /**
   * Convenience method for use outside of this class. Allows us to use the same sort of collector for lists
   * of expanded archives in multiple occasions.  Maybe we move this to it's own component next
   *
   * @param tempPath path to write temp files
   * @param source Path of the archive we're expanding
   * @param Original source URL as a string if available
   * @return List of expanded read, typed, and renamed files, not including the original.
   */

  List<IBResource> expand(Path tempPath, IBResource source, Optional<String> oSource);

}