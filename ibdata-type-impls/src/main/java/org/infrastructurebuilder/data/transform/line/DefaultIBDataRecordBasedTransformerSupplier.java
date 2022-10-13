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

import static org.infrastructurebuilder.data.IBDataConstants.IBDATA_WORKING_PATH_SUPPLIER;

import java.nio.file.Path;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.infrastructurebuilder.data.IBDataStreamRecordFinalizer;
import org.infrastructurebuilder.data.transform.AbstractIBDataTransformerSupplier;
import org.infrastructurebuilder.data.transform.base.IBDataTransformer;
import org.infrastructurebuilder.data.transform.base.line.AbstractIBDataRecordBasedTransformer;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.core.LoggerSupplier;
import org.infrastructurebuilder.util.core.PathSupplier;
import org.slf4j.Logger;

@SuppressWarnings("rawtypes")
@Named(DefaultIBDataRecordBasedTransformerSupplier.RECORD_BASED_TRANSFORMER_SUPPLIER)
public class DefaultIBDataRecordBasedTransformerSupplier extends AbstractIBDataTransformerSupplier {

  public static final String RECORD_BASED_TRANSFORMER_SUPPLIER = "record-based";
  private final Map<String, IBDataRecordTransformerSupplier> dataLineSuppliers;
  private final IBDataStreamRecordFinalizer finalizer;

  @Inject
  public DefaultIBDataRecordBasedTransformerSupplier(@Named(IBDATA_WORKING_PATH_SUPPLIER) PathSupplier wps,
      LoggerSupplier l, Map<String, IBDataRecordTransformerSupplier> dataLineTransformerSuppliers) {
    this(wps, l, null, dataLineTransformerSuppliers, null);
  }

  protected DefaultIBDataRecordBasedTransformerSupplier(PathSupplier wps,
      //
      LoggerSupplier l,
      // All config all the time
      ConfigMapSupplier cms,
      // All the available data line suppliers
      Map<String, IBDataRecordTransformerSupplier> dataLineTransformerSuppliers,
      IBDataStreamRecordFinalizer finalizer) {
    super(wps, l, cms);
    this.dataLineSuppliers = dataLineTransformerSuppliers;
    this.finalizer = finalizer;
  }

  @Override
  protected IBDataTransformer getConfiguredTransformerInstance(Path workingPath) {
    return new DefaultIBDataRecordBasedTransformer(workingPath, getLog(), getConfig(), this.dataLineSuppliers,
        this.finalizer);
  }

  @Override
  public DefaultIBDataRecordBasedTransformerSupplier configure(ConfigMapSupplier cms) {
    return new DefaultIBDataRecordBasedTransformerSupplier(getWps(), () -> getLog(), cms, this.dataLineSuppliers,
        this.finalizer);
  }

  @Override
  public DefaultIBDataRecordBasedTransformerSupplier withFinalizer(IBDataStreamRecordFinalizer finalizer) {
    return new DefaultIBDataRecordBasedTransformerSupplier(getWps(), () -> getLog(), null, dataLineSuppliers,
        finalizer);
  }

  public static class DefaultIBDataRecordBasedTransformer extends AbstractIBDataRecordBasedTransformer {

    protected DefaultIBDataRecordBasedTransformer(Path workingPath, Logger l, ConfigMap config,
        Map<String, IBDataRecordTransformerSupplier> dataRecTransformerSuppliers,
        // finalizer
        IBDataStreamRecordFinalizer finalizer) {
      super(workingPath, l, config, dataRecTransformerSuppliers, finalizer);
    }

    @Override
    public String getHint() {
      return RECORD_BASED_TRANSFORMER_SUPPLIER;
    }

  }

}
