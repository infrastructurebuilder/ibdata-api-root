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
package org.infrastructurebuilder.data.transform;

import static java.util.Objects.requireNonNull;
import static org.infrastructurebuilder.data.IBDataConstants.IBDATA_WORKING_PATH_SUPPLIER;
import static org.infrastructurebuilder.data.IBDataModelUtils.forceToFinalizedPath;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.sisu.Nullable;
import org.infrastructurebuilder.data.AbstractIBDataSetFinalizer;
import org.infrastructurebuilder.data.AbstractIBDataSetFinalizerSupplier;
import org.infrastructurebuilder.data.IBDataSet;
import org.infrastructurebuilder.data.IBDataStream;
import org.infrastructurebuilder.data.model.DataSet;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.core.LoggerSupplier;
import org.infrastructurebuilder.util.core.PathSupplier;
import org.infrastructurebuilder.util.core.TypeToExtensionMapper;
import org.infrastructurebuilder.util.files.IBChecksumPathType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named(DefaultIBDataSetTransformationFinalizerSupplier.NAME)
public class DefaultIBDataSetTransformationFinalizerSupplier
    extends AbstractIBDataSetFinalizerSupplier<Transformation> {

  static final String NAME = "default-transform";
  public final static Logger logger = LoggerFactory.getLogger(DefaultIBDataSetTransformationFinalizerSupplier.class);

  @Inject
  public DefaultIBDataSetTransformationFinalizerSupplier(@Named(IBDATA_WORKING_PATH_SUPPLIER) PathSupplier wps,
      @Nullable @Named("maven-log") LoggerSupplier l, TypeToExtensionMapper t2e) {
    this(l.get(), wps, null, t2e);
  }

  private DefaultIBDataSetTransformationFinalizerSupplier(Logger logger, PathSupplier workingPath,
      ConfigMapSupplier cms, TypeToExtensionMapper t2e) {
    super(logger, workingPath, cms, t2e);
  }

  @Override
  public DefaultIBDataSetTransformationFinalizerSupplier getConfiguredSupplier(ConfigMapSupplier cms) {
    return new DefaultIBDataSetTransformationFinalizerSupplier(getLog(), getWps(), cms, getTypeToExtensionMapper());
  }

  @Override
  protected TransformationIBDataSetFinalizer getInstance() {
    return new TransformationIBDataSetFinalizer(requireNonNull(getConfig(), "Config supplier is null").get(),
        getWps().get());
  }

  private class TransformationIBDataSetFinalizer extends AbstractIBDataSetFinalizer<Transformation> {

    public TransformationIBDataSetFinalizer(ConfigMap config, Path workingPath) {
      super(config, workingPath.resolve(UUID.randomUUID().toString()));
    }

    @Override
    public IBChecksumPathType finalize(IBDataSet inboundDataSet, Transformation target,
        List<Supplier<IBDataStream>> ibdssList, Optional<String> basedir) throws IOException {
      DataSet targetDataSet = target.asDataSet();
      targetDataSet.setPath(inboundDataSet.getPath());

      return forceToFinalizedPath(new Date(), getWorkingPath(), targetDataSet, ibdssList, getTypeToExtensionMapper(), basedir);
    }

  }

}
