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

import java.nio.file.Path;

abstract public class AbstractIBDataSourceSupplier implements IBDataSourceSupplier {

  protected final IBDataSource src;
  protected final String id;
  private final Path workingPath;

  public AbstractIBDataSourceSupplier(String id, IBDataSource src, Path workingPath) {
    this.id = requireNonNull(id);
    this.src = requireNonNull(src);
    this.workingPath = requireNonNull(workingPath);
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public IBDataSource get() {
    return this.src;
  }

  @Override
  public int compareTo(IBDataSourceSupplier o) {
    return this.get().getId().compareTo(o.get().getId());
  }

}