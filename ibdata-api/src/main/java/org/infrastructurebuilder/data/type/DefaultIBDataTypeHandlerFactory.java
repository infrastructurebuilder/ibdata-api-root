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
package org.infrastructurebuilder.data.type;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.infrastructurebuilder.util.artifacts.Weighted;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.config.IBRuntimeUtils;

@Named
@Singleton
public class DefaultIBDataTypeHandlerFactory implements IBDataTypeHandler {
  private final IBRuntimeUtils ibr;
  private final List<IBDataTypeTranslator<?>> sortedList;
  private final List<IBDataType> dataTypes;

  @Inject
  public DefaultIBDataTypeHandlerFactory(IBRuntimeUtils ibr, List<IBDataType> dataTypes,
      List<IBDataTypeTranslator<?>> translators) {
    this(requireNonNull(dataTypes).stream().sorted(Weighted.comparator()).collect(Collectors.toList()),
        translators.stream().sorted(Weighted.comparator()).collect(toList()), requireNonNull(ibr));
  }

  private DefaultIBDataTypeHandlerFactory(List<IBDataType> dataTypes, List<IBDataTypeTranslator<?>> translators,
      IBRuntimeUtils ibr) {
    this.ibr = ibr;
    this.dataTypes = dataTypes;
    this.sortedList = translators;
  }

  @Override
  public IBDataTypeHandler configure(ConfigMapSupplier cms) {
    List<IBDataTypeTranslator<?>> t = this.sortedList.stream().map(t1 -> t1.configure(cms)).collect(toList());
    List<IBDataType> d = dataTypes.stream().map(v -> v.configure(cms)).collect(toList());
    this.ibr.getLog().info("Loaded " + dataTypes.size() + " IBData types");
    StringJoiner sj = new StringJoiner(",");
    d.stream().map(IBDataType::getType).forEach(sj::add);
    this.ibr.getLog().debug(" IBDataTypes > " + sj.toString());
    return new DefaultIBDataTypeHandlerFactory(d, t, ibr);
  }

  @Override
  public Optional<IBDataType> getTypeFor(String typeName) {
    return dataTypes.stream().filter(t -> t.getType().equals(typeName)).findFirst();
  }

  @Override
  public Optional<IBDataTypeTranslator<?>> getTranslatorFor(String otherType) {
    return sortedList.stream().filter(f -> f.getType().equals(otherType)).findFirst();
  }

}
