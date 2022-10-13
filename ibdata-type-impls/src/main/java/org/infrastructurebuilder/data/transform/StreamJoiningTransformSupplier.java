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
import javax.xml.transform.Transformer;

import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.data.IBDataSet;
import org.infrastructurebuilder.data.IBDataStream;
import org.infrastructurebuilder.data.transform.base.IBDataTransformationResult;
import org.infrastructurebuilder.data.transform.base.IBDataTransformer;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.core.LoggerSupplier;
import org.infrastructurebuilder.util.core.PathSupplier;
import org.slf4j.Logger;

@Named(StreamJoiningTransformSupplier.STREAM_JOIN)
public class StreamJoiningTransformSupplier extends AbstractIBDataTransformerSupplier {

  public static final String STREAM_JOIN = "stream-join";

  @Inject
  public StreamJoiningTransformSupplier(@Named(IBDATA_WORKING_PATH_SUPPLIER) PathSupplier wps, LoggerSupplier log) {
    this(wps, log, null);
  }

  private StreamJoiningTransformSupplier(PathSupplier wps, LoggerSupplier l, ConfigMapSupplier cms) {
    super(wps, l, cms);
  }

  @Override
  public StreamJoiningTransformSupplier configure(ConfigMapSupplier cms) {
    return new StreamJoiningTransformSupplier(getWps(),() -> getLog(), cms);
  }

  @Override
  protected IBDataTransformer getConfiguredTransformerInstance(Path workingPath) {
    return new StreamJoiningTransformer(workingPath, getLog());
  }

  private class StreamJoiningTransformer extends AbstractIBDataTransformer {

    public StreamJoiningTransformer(Path p, Logger l) {
      this(p, l , new ConfigMap());
    }

    public StreamJoiningTransformer(Path p, Logger l,  ConfigMap config) {
      super(p, l, config);
    }

    @Override
    public String getHint() {
      return STREAM_JOIN;
    }

    @Override
    public IBDataTransformationResult transform(Transformer transformer, IBDataSet ds, List<IBDataStream> suppliedStreams,  boolean failOnError) {
      throw new IBDataException("Stream Joiner Not implemented ");
      //      return new DefaultIBDataTransformationResult(null);
    }

  }

}
