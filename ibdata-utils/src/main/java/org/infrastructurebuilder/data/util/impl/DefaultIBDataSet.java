package org.infrastructurebuilder.data.util.impl;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.infrastructurebuilder.data.IBDataSchemaIdentifier;
import org.infrastructurebuilder.data.IBDataSchemaSupplier;
import org.infrastructurebuilder.data.IBDataSet;
import org.infrastructurebuilder.data.IBDataStreamSupplier;
import org.infrastructurebuilder.data.Metadata;
import org.infrastructurebuilder.data.model.DataSet;
import org.infrastructurebuilder.data.model.IBDataModelUtils;
import org.infrastructurebuilder.util.core.Checksum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultIBDataSet implements IBDataSet {
  private final static Logger log = LoggerFactory.getLogger(DefaultIBDataSet.class);

  private final DataSet ds;

  private List<DefaultDataStream> streams;

  private final Metadata metadata;

  private final Checksum checksum;

  private final Path localPath;

  private List<Supplier<IBDataSchemaSupplier>> schemaSuppliers;

  public DefaultIBDataSet(DataSet ds) {
    this.ds = requireNonNull(ds);
    this.streams = ds.getStreams().stream().map(DefaultDataStream::new).collect(toList());
    this.metadata = new Metadata(ds.getMetadata());
    this.checksum = IBDataModelUtils.dataSetIdentifierChecksum.apply(this);
    this.localPath = null;
    this.schemaSuppliers = new ArrayList<>();
  }

  @Override
  public String getGroupId() {
    return ds.getGroupId();
  }

  @Override
  public String getArtifactId() {
    return ds.getArtifactId();
  }

  @Override
  public String getVersion() {
    return ds.getVersion();
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
  public Instant getCreationDate() {
    return Instant.parse(ds.getCreationDate());
  }

  @Override
  public Metadata getMetadata() {
    return this.metadata;
  }

  @Override
  public Optional<Path> getLocalPath() {
    return Optional.ofNullable(this.localPath);
  }

  @Override
  public Checksum asChecksum() {
    return this.checksum;
  }

  @Override
  public List<IBDataStreamSupplier> getStreamSuppliers() {
    List<IBDataStreamSupplier> retVal = new ArrayList<>();
    this.streams.stream().forEach(s -> retVal.add(() -> s));
    return retVal;
  }

  @Override
  public List<Supplier<IBDataSchemaSupplier>> getSchemaSuppliers() {
    return this.schemaSuppliers;
  }
}
