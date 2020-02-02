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

import static org.infrastructurebuilder.data.IBDataException.cet;
import static org.infrastructurebuilder.util.IBUtils.nullSafeDateComparator;
import static org.infrastructurebuilder.util.IBUtils.nullSafeUUIDComparator;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.infrastructurebuilder.util.artifacts.Checksum;
import org.infrastructurebuilder.util.artifacts.ChecksumBuilder;
import org.infrastructurebuilder.util.artifacts.ChecksumEnabled;
import org.infrastructurebuilder.util.artifacts.GAV;
import org.infrastructurebuilder.util.artifacts.impl.DefaultGAV;

/**
 * This is the top-level interface that describes a DataSet.
 *
 * A DataSet is a group of common metadata that holds a set of DataStreams
 *
 * Under nearly every circumstance, one might think of a DataSet as an archive
 * (a jar or zip file), since in version 1.0.0 of the model, an archive has only
 * a single DataSet metadata file
 *
 * @author mykel.alvis
 *
 */
public interface IBDataSetIdentifier extends ChecksumEnabled {

  //
  // Comparator Work
  //
  public final static Comparator<IBDataSetIdentifier> IBDataSetIdentifierComparator = Comparator
      // Check UUID
      .comparing(IBDataSetIdentifier::getUuid, nullSafeUUIDComparator)
      // Check Date
      .thenComparing(IBDataSetIdentifier::getCreationDate, nullSafeDateComparator);

  String getGroupId();

  String getArtifactId();

  String getVersion();

  default GAV getGAV() {
    return new DefaultGAV(getGroupId(), getArtifactId(), getVersion());
  }

  UUID getUuid();

  Optional<String> getName();

  Optional<String> getDescription();

  Date getCreationDate();

  Metadata getMetadata();

  /**
   * Optional representation of where this dataset <b><i>currently exists</i></b>.
   *
   * @return {@code URL#toExternalForm()} of the path to the root of the dataset if available
   */
  default Optional<String> getPath() {
    return getPathAsURL().map(URL::toExternalForm);
  }

  Optional<Path> getPathAsPath();

  default Optional<URL> getPathAsURL() {
    try {
      return getPathAsPath().map(u -> cet.withReturningTranslation(() -> u.toUri().toURL()));
    } catch (Throwable t) {
      return java.util.Optional.empty();
    }
  }

  @Override
  default Checksum asChecksum() {
    return ChecksumBuilder.newInstance() //
        .addChecksumEnabled(getGAV())//
        .addString(getName())//
        .addString(getDescription())//
        .addDate(getCreationDate()) //
        .addString(Optional.ofNullable(getMetadata()).map(s -> s.toString())) //
        // fin
        .asChecksum();

  }

}