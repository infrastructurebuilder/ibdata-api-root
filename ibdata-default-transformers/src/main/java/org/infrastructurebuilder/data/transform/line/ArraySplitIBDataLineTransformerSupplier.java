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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.inject.Named;

import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.core.LoggerSupplier;
import org.infrastructurebuilder.util.core.PathSupplier;
import org.slf4j.Logger;

@Named(ArraySplitIBDataLineTransformerSupplier.REGEX_ARRAY_SPLIT) // ArrayJoinIBDataLineTransformerSupplier
public class ArraySplitIBDataLineTransformerSupplier extends AbstractIBDataRecordTransformerSupplier<String, String[]> {
  public static final String REGEX_ARRAY_SPLIT = "regex-array-split";
  public static final List<String> ACCEPTABLE_TYPES = Arrays.asList(String.class.getCanonicalName());

  @javax.inject.Inject
  public ArraySplitIBDataLineTransformerSupplier(
      @Named(IBDATA_WORKING_PATH_SUPPLIER) PathSupplier wps, LoggerSupplier l) {
    this(wps, null, l);
  }

  private ArraySplitIBDataLineTransformerSupplier(PathSupplier wps, ConfigMapSupplier cms, LoggerSupplier l) {
    super(wps, cms, l);
  }

  @Override
  public ArraySplitIBDataLineTransformerSupplier configure(ConfigMapSupplier cms) {
    return new ArraySplitIBDataLineTransformerSupplier(getWps(), cms, () -> getLogger());
  }

  @Override
  protected IBDataRecordTransformer<String, String[]> getUnconfiguredTransformerInstance(Path workingPath) {
    return new ArraySplitIBDataLineTransformer(workingPath, getLogger());
  }

  private class ArraySplitIBDataLineTransformer extends AbstractIBDataRecordTransformer<String, String[]> {

    public static final String REGEX = "regex";
    public static final String DEFAULT_SPLIT_REGEX = ",";
    private final String splitRegex;

    ArraySplitIBDataLineTransformer(Path workingPath, Logger l) {
      this(workingPath, new ConfigMap(), l);
    }

    /**
     * @param ps
     * @param config
     */
    ArraySplitIBDataLineTransformer(Path ps, ConfigMap config, Logger l) {
      super(ps, config, l);
      this.splitRegex = Pattern.quote( getConfiguration(REGEX, DEFAULT_SPLIT_REGEX));
    }

    @Override
    public IBDataRecordTransformer<String, String[]> configure(ConfigMap cms) {
      return new ArraySplitIBDataLineTransformer(getWorkingPath(), cms, getLogger());
    }

    @Override
    public String[] apply(String t) {
      return Optional.ofNullable(t).map(s -> s.split(splitRegex)).orElse(null);
    }

    @Override
    public String getHint() {
      return REGEX_ARRAY_SPLIT;
    }

    @Override
    public Class<String> getInboundClass() {
      return String.class;
    }

    @Override
    public Class<String[]> getOutboundClass() {
      return String[].class;
    }

  }

  @Override
  public String getHint() {
    return REGEX_ARRAY_SPLIT;
  }
}