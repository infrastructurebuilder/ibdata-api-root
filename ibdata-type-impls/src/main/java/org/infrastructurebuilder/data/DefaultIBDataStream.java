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
import static org.infrastructurebuilder.util.core.IBUtils.nullSafeObjectToString;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Vector;
import java.util.function.Supplier;

import org.infrastructurebuilder.data.model.DataStream;
import org.infrastructurebuilder.exceptions.IBException;
import org.infrastructurebuilder.util.core.Checksum;
import org.infrastructurebuilder.util.core.TypeToExtensionMapper;
import org.infrastructurebuilder.util.files.BasicIBChecksumPathType;
import org.infrastructurebuilder.util.files.IBChecksumPathType;

public class DefaultIBDataStream extends DefaultIBDataStreamIdentifier implements IBDataStream {

  private final List<InputStream> createdInputStreamsForThisInstance = new Vector<>();

  private final Supplier<InputStream> ss;
  private final IBChecksumPathType cpt;
  private Checksum calculatedChecksum = null;

  // public DefaultIBDataStream(IBDataStreamIdentifier ds, UUID id, Date now,
  // Supplier<InputStream> ins) {
  // super(id, ds.getURL(), ds.getName(), ds.getDescription(), ds.getChecksum(),
  // now, ds.getMetadataAsDocument(),
  // ds.getMimeType(), Optional.ofNullable(ds.getPath()));
  // this.ss = requireNonNull(ins);
  // }

  public final static DefaultIBDataStream from(DataStream ds, Supplier<Path> pathToRoot) {
    return new DefaultIBDataStream(ds,
        new BasicIBChecksumPathType(pathToRoot.get().resolve(ds.getPath()), ds.getChecksum(), ds.getMimeType()));
  }

  public DefaultIBDataStream(IBDataStreamIdentifier identifier, Path ins) {
    this(identifier, ins, empty());
  }

  public DefaultIBDataStream(IBDataStreamIdentifier identifier, Path ins, Optional<IBDataStructuredDataMetadata> sdmd) {
    super(identifier.getId(), identifier.getURL(), identifier.getName(), identifier.getDescription(),
        identifier.getChecksum(), identifier.getCreationDate(), identifier.getMetadataAsDocument(),
        identifier.getMimeType(), ofNullable(identifier.getPath()), ofNullable(identifier.getOriginalLength()),
        ofNullable(identifier.getOriginalRowCount()));
    sdmd.ifPresent(s -> this.setStructuredDataMetadata(s));
    this.cpt = new BasicIBChecksumPathType(Objects.requireNonNull(ins), identifier.getChecksum(),
        identifier.getMimeType());
    this.ss = this.cpt;
  }

  public DefaultIBDataStream(IBDataStreamIdentifier identifier, IBChecksumPathType ins) {
    super(identifier.getId(), identifier.getURL(), identifier.getName(), identifier.getDescription(),
        requireNonNull(ins).getChecksum(), identifier.getCreationDate(), identifier.getMetadataAsDocument(),
        requireNonNull(ins).getType(),
        nullSafeObjectToString.apply(ofNullable(ins.getPath()).map(Path::getFileName).orElse(null)),
        ofNullable(identifier.getOriginalLength()), ofNullable(identifier.getOriginalRowCount()));
    this.cpt = ins;
    this.calculatedChecksum = this.cpt.getChecksum();
    this.ss = requireNonNull(ins);
  }

  @Override
  public InputStream get() {
    InputStream str = cet.withReturningTranslation(() -> this.ss.get());
    createdInputStreamsForThisInstance.add(str);
    return str;

  }

  @Override
  public Checksum getChecksum() {
    if (calculatedChecksum == null) {
      try (InputStream ins = get()) {
        calculatedChecksum = new Checksum(ins);
      } catch (IOException | IBException e) {
        throw new IBDataException("Error calculating checksum for " + getId());
      }
    }
    return this.calculatedChecksum;
  }

  @Override
  public void close() throws Exception {
    this.createdInputStreamsForThisInstance.forEach(stream -> {
      try {
        stream.close();
      } catch (IOException e) {
        // If they won't close, we'll have to leak that
      }
    });
  }

  @Override
  public IBDataStream relocateTo(Path newWorkingPath, TypeToExtensionMapper t2e) {
    IBChecksumPathType newCpt;
    Path target;
    newCpt = this.cpt;
    String newTargetName = newCpt.getChecksum().asUUID().get().toString() + t2e.getExtensionForType(newCpt.getType());
    target = newWorkingPath.resolve(newTargetName);
    return new DefaultIBDataStream(this, cet.withReturningTranslation(() -> newCpt.moveTo(target)));
  }

  @Override
  public Optional<IBDataStructuredDataMetadata> getIBDataStructuredDataMetadata() {
    return empty();
  }

  @Override
  public Optional<Path> getPathIfAvailable() {
    return Optional.ofNullable(this.cpt).map(IBChecksumPathType::getPath);

  }
}
