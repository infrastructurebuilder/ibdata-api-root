/*
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

import java.util.Optional;

abstract public class AbstractIBDataDecoratedDAO<T> implements IBDataDecoratedDAO<T> {

  private String nameSpace;
  private String name;
  private String thisVersion;
  private String description;
  private Optional<String> source;
  private T typedValue;

  public AbstractIBDataDecoratedDAO(String nameSpace, String name, String thisVersion, String description,
      Optional<String> source, T typedValue) {
    this.nameSpace = requireNonNull(nameSpace);
    this.name = requireNonNull(name);
    this.thisVersion = requireNonNull(thisVersion);
    this.description = requireNonNull(description);
    this.source = requireNonNull(source);
    this.typedValue = requireNonNull(typedValue);
  }

  @Override
  public String getNameSpace() {
    return nameSpace;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getThisVersion() {
    return thisVersion;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public Optional<String> getSource() {
    return source;
  }

  @Override
  public T get() {
    return typedValue;
  }

}
