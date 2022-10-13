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

import static org.infrastructurebuilder.data.IBDataConstants.IBDATA_WORKING_PATH_SUPPLIER;

import java.nio.file.Path;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.infrastructurebuilder.data.DefaultIBDataTransformationResult;
import org.infrastructurebuilder.data.IBDataSet;
import org.infrastructurebuilder.data.IBDataStream;
import org.infrastructurebuilder.data.IBDataStreamRecordFinalizer;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.core.LoggerSupplier;
import org.infrastructurebuilder.util.core.PathSupplier;
import org.slf4j.Logger;

@Named(PassThruTransformerSupplier.NAME)
public class PassThruTransformerSupplier extends AbstractIBDataTransformerSupplier {

  public static final String NAME = "pass-thru";

  @Inject
  public PassThruTransformerSupplier(@Named(IBDATA_WORKING_PATH_SUPPLIER) PathSupplier wps, LoggerSupplier l) {
    this(wps, l, null);
  }

  public PassThruTransformerSupplier(PathSupplier wps, LoggerSupplier l, ConfigMapSupplier cms) {
    super(wps, l, cms);
  }

  @Override
  public PassThruTransformerSupplier configure(ConfigMapSupplier cms) {
    return new PassThruTransformerSupplier(getWps(), () -> getLog(), cms);
  }

  @Override
  protected IBDataTransformer getConfiguredTransformerInstance(Path wps2) {
    return new PassThruTransformer(wps2, getLog());
  }

  @Override
  public PassThruTransformerSupplier withFinalizer(IBDataStreamRecordFinalizer<?> finalizer) {
    return new PassThruTransformerSupplier(getWps(), () -> getLog());
  }

  private final class PassThruTransformer extends AbstractIBDataTransformer {

    public PassThruTransformer(Path wps2, Logger l) {
      super(wps2, l);
    }

    @Override
    public IBDataTransformationResult transform(Transformer transformer, IBDataSet ds,
        List<IBDataStream> suppliedStreams, boolean failOnError) {
      return new DefaultIBDataTransformationResult(ds, getWorkingPath());
    }

    @Override
    public String getHint() {
      return NAME;
    }
  }
}
