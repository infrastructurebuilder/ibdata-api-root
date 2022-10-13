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
package org.infrastructurebuilder.data;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.infrastructurebuilder.data.transform.base.IBDataTransformationError;
import org.infrastructurebuilder.data.transform.base.IBDataTransformationResult;
import org.infrastructurebuilder.util.core.PathSupplier;

/**
 * An object containing the result of a transformation and any errors.
 * @author mykel.alvis
 *
 * @param <T>
 */
public class DefaultIBDataTransformationResult implements IBDataTransformationResult {

  private Optional<IBDataSet> dataSet;
  private List<IBDataTransformationError> errors;
  private final Path workingPath;

  public DefaultIBDataTransformationResult(IBDataSet createdDataSet, Path workingPath) {
    this(ofNullable(createdDataSet), new ArrayList<>(), workingPath);
  }

  public DefaultIBDataTransformationResult(Optional<IBDataSet> createdSet, List<IBDataTransformationError> errors, Path workingPath) {
    this.dataSet = requireNonNull(createdSet);
    this.errors = requireNonNull(errors);
    this.workingPath = requireNonNull(workingPath);
  }

  @Override
  public List<IBDataTransformationError> getErrors() {
    return errors;
  }

  @Override
  public Optional<IBDataSet> get() {
    return dataSet;
  }

  @Override
  public PathSupplier getWorkingPathSupplier() {
    return () -> this.workingPath;
  }

}
