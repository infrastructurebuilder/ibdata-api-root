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
import static org.infrastructurebuilder.data.IBDataConstants.IBDATA_WORKING_PATH_SUPPLIER;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.sisu.Typed;
import org.infrastructurebuilder.data.AbstractIBDataSetFinalizer;
import org.infrastructurebuilder.data.AbstractIBDataSetFinalizerSupplier;
import org.infrastructurebuilder.data.IBDataSet;
import org.infrastructurebuilder.data.IBDataSetFinalizerSupplier;
import org.infrastructurebuilder.data.IBDataStream;
import org.infrastructurebuilder.data.model.IBDataModelUtils;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.core.LoggerSupplier;
import org.infrastructurebuilder.util.core.PathSupplier;
import org.infrastructurebuilder.util.core.TypeToExtensionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named(DefaultIBDataSetIngestionFinalizerSupplier.DEFAULT_INGEST)
@Typed(IBDataSetFinalizerSupplier.class)
public class DefaultIBDataSetIngestionFinalizerSupplier extends AbstractIBDataSetFinalizerSupplier<Ingestion> {

  public static final String DEFAULT_INGEST = "default-ingest";
  public final static Logger logger = LoggerFactory.getLogger(DefaultIBDataSetIngestionFinalizerSupplier.class);

  @Inject
  public DefaultIBDataSetIngestionFinalizerSupplier(@Named(IBDATA_WORKING_PATH_SUPPLIER) PathSupplier wps,
      LoggerSupplier l, TypeToExtensionMapper t2e) {
    this(l.get(), wps, null, t2e);
  }

  private DefaultIBDataSetIngestionFinalizerSupplier(Logger logger, PathSupplier workingPath, ConfigMapSupplier cms,
      TypeToExtensionMapper t2e) {
    super(logger, workingPath, cms, t2e);
  }

  @Override
  public DefaultIBDataSetIngestionFinalizerSupplier getConfiguredSupplier(ConfigMapSupplier cms) {
    return new DefaultIBDataSetIngestionFinalizerSupplier(getLog(), getWps(), cms, getTypeToExtensionMapper());
  }

  @Override
  protected IngestionIBDataSetFinalizer getInstance() {
    return new IngestionIBDataSetFinalizer(requireNonNull(getConfig(), "Config supplier is null").get(),
        getWps().get());
  }

  private class IngestionIBDataSetFinalizer extends AbstractIBDataSetFinalizer<Ingestion> {

    public IngestionIBDataSetFinalizer(ConfigMap config, Path workingPath) {
      super(config, workingPath);
    }

    @Override
    public IBChecksumPathType finalize(IBDataSet dsi2, Ingestion target, List<Supplier<IBDataStream>> ibdssList, Optional<String> basedir)
        throws IOException {
      // dsi2 is always null (currently). There is no "previous dataset" to contend
      // with in ingestion
      return IBDataModelUtils.forceToFinalizedPath(new Date(), getWorkingPath(), target.asDataSet(), ibdssList,
          getTypeToExtensionMapper(), basedir);
    }

  }

}
