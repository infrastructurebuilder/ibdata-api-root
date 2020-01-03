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
import static java.util.Optional.ofNullable;
import static org.infrastructurebuilder.data.IBDataException.cet;
import static org.infrastructurebuilder.util.IBUtils.nullSafeDateComparator;
import static org.infrastructurebuilder.util.IBUtils.nullSafeURLMapper;
import static org.infrastructurebuilder.util.IBUtils.nullSafeUUIDComparator;

import java.net.URL;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.infrastructurebuilder.util.IBUtils;
import org.infrastructurebuilder.util.artifacts.Checksum;
import org.infrastructurebuilder.util.artifacts.ChecksumBuilder;
import org.infrastructurebuilder.util.artifacts.ChecksumEnabled;
;

/**
 * This is the top-level interface that describes a stream of data (i.e. a
 * single InputStream/File/what have you)
 *
 * @author mykel.alvis
 *
 */
public interface IBDataStreamIdentifier extends ChecksumEnabled {
  public final static Comparator<IBDataStreamIdentifier> ibDataStreamComparator = Comparator
      // Check UUID
      .comparing(IBDataStreamIdentifier::getId, nullSafeUUIDComparator)
      // Check Date
      .thenComparing(IBDataStreamIdentifier::getCreationDate, nullSafeDateComparator);

  /**
   * <i>Usually</i> this will return the "data stream id", which a UUID generated
   * from the bytes of a Checksum of the contents of the stream in question. This
   * IS OCCASIONALLY NULL, but only temporarily. It may not be available, as there
   * might not have been a computed checksum for something that hasn't been
   * calculated yet.
   *
   * @return A UUID from the Checksum of the contents of the stream or null. Null
   *         simply means there has not been a calculation on the contents yet.
   */
  UUID getId();

  /**
   * The source of this stream. Optional, but HIGHLY important
   *
   * Note that this might be a JDBC URL as well, so it can't be an ACTUAL
   * java.net.URL (yet)
   *
   * @return Optional URL of the underlying stream
   */
  Optional<String> getUrl();

  /**
   * @return Optional Name supplied at creation time
   */
  Optional<String> getName();

  /**
   *
   * @return Optional description supplied at creation time
   */
  Optional<String> getDescription();

  /**
   * Mapper for field in the model. This allows us to extract some logic from the
   * modello model. Users should not rely on this.
   *
   * @return
   */
  String getSha512();

  /**
   * This is a checksum of the underlying file (used to calculate the UUID in
   * getId()). It only contains a checksum for the file, not the metadata. See
   * getMetadataChecksum() to get checksums of all elements
   *
   * This is expected to be a non-null value unless the underlying code handles an
   * actual stream. In that case the value needs to be calculated.
   *
   * @return Checksum of the contents of the underlying file or throw
   *         NullPointerException
   */
  default Checksum getChecksum() {
    return ofNullable(getSha512()).filter(s -> s.length() == 128) // Length of a sha512
        .map(org.infrastructurebuilder.util.artifacts.Checksum::new)
        .orElseThrow(() -> new org.infrastructurebuilder.data.IBDataException("No sha512 available"));
  }

  /**
   * The "creation date", which is VERY CLOSE to when this file was downloaded.
   *
   * @return Date accepted moment when this stream was read from the source and
   *         optionally subsequently verified
   */
  Date getCreationDate();

  /**
   * Xpp3Dom instance containing the metadata supplied for THIS stream.
   *
   * No extra metadata is supplied by the default ingester, although subtypes
   * could easily introduce or require additional metadata.
   *
   * The DataSet has the capability of aggregating metadata. You should probably
   * use that.
   *
   * Use getMetadataAsDocument for W3c Document
   *
   * @return Xpp3Dom instance describing the metadata supplied at creation time.
   */
  Xpp3Dom getMetadata();

  /**
   * Non-nullable mime type of the contents of the stream.
   *
   * @return Mime type of the contents of the stream, defaulting to
   *         application/octect-stream
   */
  String getMimeType();

  /**
   * REQUIRED Path to the URL of the stream (wherever it is) relative to the
   * parent dataset's path. See pathAsURL for a reasonable representation of how
   * to calculate the URL based on this path.
   *
   * @return Path relative to the path supplied in the enclosing DataSet.
   *
   */
  String getPath();

  /**
   * The proper method for calculating metadata checksum
   *
   * @return Checksum instance consisting of a checksum of all relevant entries
   */
  default Checksum getMetadataChecksum() {
    return ChecksumBuilder.newInstance()
        // Date
        .addDate(getCreationDate())
        // Desc
        .addString(getDescription())
        // metadata
        .addChecksum(IBMetadataUtils.asChecksum.apply(getMetadata()))
        // Mime type
        .addString(getMimeType())
        // Name
        .addString(getName())
        // Numrows
        .addLong(getNumRows())
        // orig len
        .addString(getOriginalLength())
        // orig row count
        .addString(getOriginalRowCount())
        // URL
        .addString(getUrl())
        //
        .asChecksum();
  }

  @Override
  default Checksum asChecksum() {
    return getChecksum();
  }

  /**
   * This is tricky. The parent URL must exist to be able to get the child URL
   * (obvs).
   *
   * @param parent non-null URL From IBDataSetIdentifier.pathAsURL().get()
   *
   *               The current version probably won't work on Windows because they
   *               REALLY needed to have a different path separator than the rest
   *               of the computing world.
   *
   * @return Optional URL mapped to a string
   */

  default Optional<URL> pathAsURL(IBDataSetIdentifier pDataSet) {
    return nullSafeURLMapper.apply(ofNullable(getPath()).flatMap(path -> {
      Optional<String> v = pDataSet.getPath()
          .map(pPath -> cet.withReturningTranslation(() -> IBUtils.translateToWorkableArchiveURL(pPath)))
          .map(parent -> {
            String y = Objects.requireNonNull(parent).toExternalForm();
            boolean isArchive = (y.endsWith(".jar") || y.endsWith(".zip"));
            StringBuilder x = new StringBuilder();
            x.append(isArchive ? "zip:" : "");
            x.append(y);
            // URLS are paths into jar/zip files (at present)
            x.append(isArchive ? "!" : "");
            return x.append(path).toString();
          });
      return v;

    }).orElse(null));

  }

  default boolean isExpandArchives() {
    return false;
  }

  default Optional<Path> getPathIfAvailable() {
    return empty();
  }

  /**
   * @return actual byte length of the inputstream if known
   */
  default Optional<Long> getInputStreamLength() {
    return ofNullable(getOriginalLength()).map(Long::parseLong);
  }

  default Optional<Long> getNumRows() {
    return ofNullable(getOriginalRowCount()).map(Long::parseLong);
  }

  /**
   * Nullable value for string length of file stream
   *
   * @return
   */
  String getOriginalLength();

  /**
   * Nullable value for string count of "records" (or lines or whatever)
   *
   * @return
   */
  String getOriginalRowCount();

  Optional<UUID> getReferencedSchemaId();

  /**
   * Return the id IBSchema that this stream references.
   *
   * @return
   */
  default Optional<IBSchema> getSchema() {
    return getReferencedSchemaId().flatMap(id -> {
      return getParent().flatMap(IBDataSet::getEngine).flatMap(e -> e.fetchSchemaById(id));
    });
  }

  Optional<IBDataProvenance> getProvenance();

  Optional<IBDataStructuredDataMetadata> getStructuredDataMetadata();

  /**
   * Certain instances will not have an available parent
   *
   * @return
   */
  default Optional<IBDataSet> getParent() {
    return empty();
  }

  default Optional<IBDataEngine> getEngine() {
    return empty();
  }

  Optional<String> getTemporaryId();

}