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

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.infrastructurebuilder.data.AbstractIBDataSourceSupplier;
import org.infrastructurebuilder.data.IBDataSource;
import org.infrastructurebuilder.util.core.PathSupplier;
import org.infrastructurebuilder.util.core.TypeToExtensionMapper;
import org.slf4j.Logger;

abstract public class AbstractIBDataSourceSupplierMapper implements IBDataSourceSupplierMapper {

  private final Logger log;
  private final TypeToExtensionMapper mapper;
  private final PathSupplier wps;

  public AbstractIBDataSourceSupplierMapper(Logger log, TypeToExtensionMapper mapper, PathSupplier wps) {
    this.log = requireNonNull(log);
    this.mapper = requireNonNull(mapper);
    this.wps = requireNonNull(wps);
  }

  @Override
  public boolean respondsTo(DefaultIBDataStreamIdentifierConfigBean v) {
    String u = Optional.ofNullable(v).flatMap(DefaultIBDataStreamIdentifierConfigBean::getURL).orElse("");
    return getHeaders().stream().anyMatch(h -> u.startsWith(h));
  }

  abstract public List<String> getHeaders();

  public Logger getLog() {
    return log;
  }

  public TypeToExtensionMapper getMapper() {
    return mapper;
  }

  @Override
  public Path getWorkingPath() {
    return wps.get();
  }

  public class DefaultIBDataSourceSupplier extends AbstractIBDataSourceSupplier {

    public DefaultIBDataSourceSupplier(String temporaryId, IBDataSource src, Path workingPath) {
      super(temporaryId, src, workingPath);
    }

  }

}