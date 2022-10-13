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
package org.infrastructurebuilder.data.transform.line;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.isDirectory;
import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static org.infrastructurebuilder.exceptions.IBException.cet;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.function.Supplier;

import org.infrastructurebuilder.data.DefaultIBDataStream;
import org.infrastructurebuilder.data.DefaultIBDataStreamSupplier;
import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.data.IBDataStream;
import org.infrastructurebuilder.data.IBDataStreamIdentifier;
import org.infrastructurebuilder.data.IBDataStreamRecordFinalizer;
import org.infrastructurebuilder.data.IBDataStructuredDataMetadata;
import org.infrastructurebuilder.data.model.DataStreamStructuredMetadata;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.slf4j.Logger;

abstract public class AbstractIBDataStreamRecordFinalizer<T, O> implements IBDataStreamRecordFinalizer<T> {
  private final ConfigMap config;
  private final String id;
  private final Path workingPath;
  private final Optional<O> writer;
  private final Logger log;
  private boolean closed = false;
  protected DataStreamStructuredMetadata smd = null;

  public AbstractIBDataStreamRecordFinalizer(String id, Path workingPath, Logger l, ConfigMap map,
      Optional<O> optionalWriter) {
    this.log = requireNonNull(l);
    this.config = map;
    this.workingPath = requireNonNull(workingPath);
    Path k = this.workingPath.getParent();
    if (!isDirectory(k) || !exists(k))
      cet.translate(() -> createDirectories(k));
    this.id = requireNonNull(id);
    this.writer = requireNonNull(optionalWriter);
  }

  public Logger getLog() {
    return log;
  }

  protected ConfigMap getConfig() {
    return config;
  }

  @Override
  public String getId() {
    return id;
  }

  public Path getWorkingPath() {
    return workingPath;
  }

  @Override
  public InputStream get() {
    return cet.returns(() -> Files.newInputStream(getWorkingPath(), StandardOpenOption.READ));
  }

  @Override
  public Supplier<IBDataStream> finalizeRecord(IBDataStreamIdentifier ds) {
    DefaultIBDataStream stream = new DefaultIBDataStream(ds, getWorkingPath(), getStructuredMetadata());
    return new DefaultIBDataStreamSupplier(stream);
  }

  @Override
  public void close() throws Exception {
    writer.ifPresent(w -> {
      if (!this.closed && w instanceof Closeable) {
        cet.withTranslation(() -> ((Closeable) w).close());
        this.closed = true;
      }
      if (!this.closed && w instanceof AutoCloseable) {
        cet.withTranslation(() -> ((AutoCloseable) w).close());
        this.closed = true;
      }
    });
  }

  abstract protected void writeThrows(T recordToWrite) throws Throwable;

  protected O getWriter() {
    return writer.orElseThrow(() -> new IBDataException("Tried to get a writer but no writer was configured"));
  }

  @Override
  public Optional<Throwable> writeRecord(T recordToWrite) {
    try {
      this.smd = updateStructuredMetadata(this.smd, recordToWrite);
      writeThrows(recordToWrite);
      return empty();
    } catch (Throwable e) {
      return of(new IBDataException(recordToWrite.toString(),e));
    }
  }

  // Override to create/set this.smd in order to collect structured metadata
  protected DataStreamStructuredMetadata updateStructuredMetadata(DataStreamStructuredMetadata current, T recordToWrite) {
    return null;
  };

  protected Optional<IBDataStructuredDataMetadata> getStructuredMetadata() {
    return ofNullable(this.smd);
  }

  @Override
  public OutputStream getWriterTarget() throws IOException {
    return Files.newOutputStream(getWorkingPath(), CREATE_NEW);
  }

}
