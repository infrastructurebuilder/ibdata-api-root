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

import static java.util.Objects.requireNonNull;
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
import java.util.Optional;
import java.util.UUID;

import org.infrastructurebuilder.util.IBUtils;
import org.infrastructurebuilder.util.artifacts.Checksum;
import org.infrastructurebuilder.util.artifacts.ChecksumBuilder;
import org.infrastructurebuilder.util.artifacts.ChecksumEnabled;;

/**
 * This is the top-level interface that describes an arbitrary stream of data
 *
 * An IBDataStreamIdentifier describes a specific set of persisted, or at least
 * persistable, bytes.
 *
 * Numerous generally-optional metadata values are supplied
 *
 * @see IBDataStream
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
   * <p>
   * This is really a URI string
   * <p>
   * Note that this might be a JDBC URL, or some other target string, so it can't
   * be an ACTUAL java.net.URL (yet)
   *
   * @return Optional source of the underlying stream.
   */
  Optional<String> getUrl();

  /**
   * @return name supplied at creation time
   */
  Optional<String> getName();

  /**
   *
   * @return description supplied at creation time
   */
  Optional<String> getDescription();

  /**
   * Stored value of {@code asChecksum().toString()}
   *
   * @return 128 hex characters
   * @see Checksum
   */
  String getSha512();

  /**
   * checksum of the <b><i><u>underlying file</u></i></b> (used to calculate the
   * UUID in getId()).
   *
   * This contains a checksum for the bytes of the referenced stream, not the
   * metadata. See getMetadataChecksum() to get checksums of all elements
   *
   * This is expected to be a non-null value UNLESS the underlying code handles an
   * actual stream. In that case the value needs to be calculated, and could be
   * null prior to final procesing
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
   * The "creation date", which is VERY CLOSE to when the bytes for this stream
   * were acquired.
   *
   * @return {@link Date} accepted moment when this stream was read from the
   *         source and optionally subsequently verified
   */
  Date getCreationDate();

  /**
   * XML field containing the metadata supplied for this stream.
   * <p>
   * No extra metadata is supplied by the default ingester, although subtypes
   * could easily introduce or require additional metadata.
   * <p>
   * The DataSet has the capability of aggregating metadata. You should probably
   * use that.
   * <p>
   *
   * @return Metadata instance describing the metadata supplied at stream creation
   *         time.
   */
  Metadata getMetadata();

  /**
   * Required mime type of the contents of the stream.
   *
   * @return Mime type of the contents of the stream, defaulting to
   *         application/octect-stream
   *
   * @see TypeToExtensionMapper
   */
  String getMimeType();

  /**
   * Required String path to the URL of the stream (wherever it is) relative to
   * the parent dataset's path. See pathAsURL for a reasonable representation of
   * how to calculate the URL based on this path.
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
            String y = requireNonNull(parent).toExternalForm();
            boolean isArchive = (y.endsWith(".jar") || y.endsWith(".zip"));
            return new StringBuilder()
                // archive identifier
                .append(isArchive ? "jar:" : "")
                // actual path
                .append(y)
                // URLS are paths into jar/zip files (at present)
                .append(isArchive ? "!" : "")
                // underlying (this) path
                .append(path).toString();
          });
      return v;

    }).orElse(null));

  }

  /**
   * Should archives be expanded. This is not part of a persisted dataset metadata
   *
   * @return
   */
  default boolean isExpandArchives() {
    return false;
  }

  /**
   * Calculate actual {@link Path} if possible.
   * <p>
   * Implementations must provide an actual {@link Path} to the data where
   * possible.
   *
   * @return
   */
  default Optional<Path> getPathIfAvailable() {
    return empty();
  }

  /**
   * actual byte length of the inputstream if known
   *
   * @return
   */
  default Optional<Long> getInputStreamLength() {
    return ofNullable(getOriginalLength()).map(Long::parseLong);
  }

  /**
   * Number of "rows" (of structured data) if known
   *
   * @return
   */
  default Optional<Long> getNumRows() {
    return ofNullable(getOriginalRowCount()).map(Long::parseLong);
  }

  /**
   * Nullable persisted value for string length of file stream
   *
   * @return
   */
  String getOriginalLength();

  /**
   * Nullable persisted value for string count of "records" (or lines or whatever)
   *
   * @return
   */
  String getOriginalRowCount();

  /**
   * Optional reference to an {@link IBSchema} that defines the schema of this
   * stream
   * <p>
   * Note that the references schema is one that the system uses for translation,
   * but that {@code getMimeType} defines the actual type of the bytestream.
   *
   * @return
   */
  default Optional<UUID> getReferencedSchemaId() {
    return empty();
  }

  /**
   * Return the {@link IBSchema) that this stream references if an
   * {@link IBDataEngine} is available
   *
   * @return
   */
  default Optional<IBSchema> getSchema() {
    return getReferencedSchemaId().flatMap(id -> {
      return getEngine().flatMap(e -> e.fetchSchemaById(id));
    });
  }

  /**
   * Fetch the enclosing {@link IBDataSet}
   * <p>
   * This will usually be available within the system supplied by implementations
   *
   * @return
   */
  default Optional<IBDataSet> getParent() {
    return empty();
  }

  /**
   * Fetch the {@link IBDataEngine} instance used by the parent
   * <p>
   * Providing the reference directly is not recommended. Override
   * {@code getParent} instead.
   *
   * @return {@link IBDataEngine} instance if available
   */
  default Optional<IBDataEngine> getEngine() {
    return getParent().flatMap(IBDataSet::getEngine);
  }

  /**
   * Get the temporary id assigned to this stream during ingestion
   * <p>
   * During ingestion, an actual UUID is not available from {@code getId} until
   * finalization occurs. Thus, any reference to a stream "in-flight" must be
   * based on a temporary id that is not persisted with the stream.
   * <p>
   * Implementations must create local storage for and override this method
   *
   * @return
   */
  default Optional<String> getTemporaryId() {
    return ofNullable(getId()).map(UUID::toString);
  }

  /**
   * Fetch the provenance record if available
   * <p>
   *
   * @return
   */
  default Optional<IBDataProvenance> getProvenance() {
    return empty();
  }

  /**
   * Fetch the {@link IBDataStructuredDataMetadata} for this stream if available
   * <p>
   * The structured metadata values must be calculated during some processing
   * operation to exist, so many of those values will not be available prior to
   * transformation. However, underlying instances are encouraged to produce the
   * structured metadata as they transform a stream.
   *
   * @return
   */
  default Optional<IBDataStructuredDataMetadata> getStructuredDataMetadata() {
    return empty();
  }

}