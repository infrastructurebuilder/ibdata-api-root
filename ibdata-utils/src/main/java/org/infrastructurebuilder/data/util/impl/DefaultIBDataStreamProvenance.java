package org.infrastructurebuilder.data.util.impl;

import static java.util.Optional.ofNullable;

import java.util.List;
import java.util.Optional;

import org.infrastructurebuilder.data.IBDataProvenance;
import org.infrastructurebuilder.data.model.DataStreamProvenance;
import org.infrastructurebuilder.data.model.Dependency;
import org.infrastructurebuilder.util.core.DefaultGAV;
import org.infrastructurebuilder.util.core.GAV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultIBDataStreamProvenance implements IBDataProvenance {

  private final static Logger log = LoggerFactory.getLogger(DefaultIBDataStreamProvenance.class);
  private final DataStreamProvenance dp;

  public DefaultIBDataStreamProvenance(DataStreamProvenance dp) {
    this.dp = dp;
  }

  private Optional<DataStreamProvenance> _dp() {
    return ofNullable(dp);
  }

  @Override
  public Optional<GAV> getOriginalSourceArtifact() {
    return _dp().map(q -> q.getOriginator()).map(Dependency::asGAV);
  }

  @Override
  public Optional<String> getOriginalURL() {
    return _dp().map(q -> q.getUrl());
  }

  @Override
  public Optional<List<String>> getTransformationList() {
    return _dp().map(DataStreamProvenance::getTransformations);
  }
}
