package org.infrastructurebuilder.data.util.impl;

import static java.util.Objects.requireNonNull;

import java.io.InputStream;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.infrastructurebuilder.data.IBDataStream;
import org.infrastructurebuilder.data.Metadata;
import org.infrastructurebuilder.data.model.DataStream;
import org.infrastructurebuilder.data.util.IBDataSetFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultDataStream implements IBDataStream {

  private final static Logger log = LoggerFactory.getLogger(DefaultDataStream.class);
  private final DataStream ds;
  private final DefaultIBDataStreamProvenance provenance;

  public DefaultDataStream(DataStream ds) {
    this.ds = requireNonNull(ds);
    this.provenance = Optional.ofNullable(ds.getProvenance()).map(DefaultIBDataStreamProvenance::new).orElse(null);
  }

  @Override
  public InputStream get() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void close() throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public UUID getId() {
    return UUID.fromString(ds.getUuid());
  }

  @Override
  public Optional<String> getUrl() {
    return Optional.ofNullable(ds.getUrl());
  }

  @Override
  public String getName() {
    return ds.getName();
  }

  @Override
  public Optional<String> getDescription() {
    return Optional.ofNullable(ds.getDescription());
  }

  @Override
  public String getSha512() {
    return ds.getSha512();
  }

  @Override
  public Instant getCreationDate() {
    return Instant.parse(ds.getCreationDate());
  }

  @Override
  public Metadata getMetadata() {
    return new Metadata(ds.getMetadata());
  }

  @Override
  public String getMimeType() {
    return ds.getMimeType();
  }

  @Override
  public String getPath() {
    return ds.getPath();
  }

  @Override
  public Long getOriginalLength() {
    return ds.getOriginalLength();
  }

  @Override
  public Optional<Long> getOriginalRowCount() {
    Long l = ds.getOriginalRowCount();
    return Optional.ofNullable(l < 1 ? null : l);
  }

  @Override
  public IBDataStream relocateTo(Path newWorkingPath) {
    return IBDataSetFactory.getInstance().relocate(this, newWorkingPath);
  }

}
