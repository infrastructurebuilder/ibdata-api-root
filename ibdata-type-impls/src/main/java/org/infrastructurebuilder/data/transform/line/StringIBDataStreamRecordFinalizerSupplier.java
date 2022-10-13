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
import static org.infrastructurebuilder.data.IBDataException.cet;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import org.infrastructurebuilder.data.IBDataDataStreamRecordFinalizerSupplier;
import org.infrastructurebuilder.data.IBDataStreamRecordFinalizer;
import org.infrastructurebuilder.data.model.DataStreamStructuredMetadata;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.core.LoggerSupplier;
import org.infrastructurebuilder.util.core.PathSupplier;
import org.slf4j.Logger;

@Named(StringIBDataStreamRecordFinalizerSupplier.NAME)
public class StringIBDataStreamRecordFinalizerSupplier extends AbstractIBDataStreamRecordFinalizerSupplier<String> {

  public static final String NAME = "string";
  public static final List<Class<?>> ACCEPTABLE_TYPES = Arrays.asList(String.class);

  @Inject
  public StringIBDataStreamRecordFinalizerSupplier(@Named(IBDATA_WORKING_PATH_SUPPLIER) PathSupplier wps,
      LoggerSupplier l) {
    this(wps, l, null);
  }

  private StringIBDataStreamRecordFinalizerSupplier(PathSupplier ps, LoggerSupplier l, ConfigMapSupplier cms) {
    super(ps, l, cms);
  }

  @Override
  public IBDataDataStreamRecordFinalizerSupplier<String> configure(ConfigMapSupplier cms) {
    return new StringIBDataStreamRecordFinalizerSupplier(getWps(), () -> getLog(), cms);
  }

  @Override
  public IBDataStreamRecordFinalizer<String> get() {
    // The working path needs to be stable and pre-existent
    return new StringIBDataStreamRecordFinalizer(NAME, getWps().get().resolve(UUID.randomUUID().toString()), getLog(),
        getCms().get());
  }

  private class StringIBDataStreamRecordFinalizer extends AbstractIBDataStreamRecordFinalizer<String, BufferedWriter> {

    private final int numberOfRowsToSkip;

    public StringIBDataStreamRecordFinalizer(String id, Path workingPath, Logger l, ConfigMap map) {
      super(id, workingPath, l, map,
          Optional.of(cet.withReturningTranslation(() -> Files.newBufferedWriter(workingPath, CREATE_NEW))));
      this.numberOfRowsToSkip = Integer.parseInt(map.getOrDefault(NUMBER_OF_ROWS_TO_SKIP_PARAM, "0"));
    }

    @Override
    public int getNumberOfRowsToSkip() {
      return this.numberOfRowsToSkip;
    }

    @Override
    protected void writeThrows(String recordToWrite) throws Throwable {
      getWriter().write(recordToWrite);
      getWriter().write("\n");
    }

    @Override
    protected DataStreamStructuredMetadata updateStructuredMetadata(DataStreamStructuredMetadata current,
        String recordToWrite) {
      // TODO At least record the string length
      return null;
    }

    @Override
    public Optional<List<Class<?>>> accepts() {
      return Optional.of(ACCEPTABLE_TYPES);
    }
  }

}
