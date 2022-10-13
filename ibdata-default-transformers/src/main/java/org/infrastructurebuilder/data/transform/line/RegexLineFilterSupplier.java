
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

import static java.util.Optional.ofNullable;
import static org.infrastructurebuilder.data.IBDataConstants.IBDATA_WORKING_PATH_SUPPLIER;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Named;

import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.core.LoggerSupplier;
import org.infrastructurebuilder.util.core.PathSupplier;
import org.slf4j.Logger;

@Named(RegexLineFilterSupplier.REGEX_LINE_FILTER)
public class RegexLineFilterSupplier extends AbstractIBDataRecordTransformerSupplier<String, String> {
  public static final String REGEX_LINE_FILTER = "regex-line-filter";

  @javax.inject.Inject
  public RegexLineFilterSupplier(@Named(IBDATA_WORKING_PATH_SUPPLIER) PathSupplier wps, LoggerSupplier l) {
    this(wps, null, l);
  }

  private RegexLineFilterSupplier(PathSupplier wps, ConfigMapSupplier cms, LoggerSupplier l) {
    super(wps, cms, l);
  }

  @Override
  public RegexLineFilterSupplier configure(ConfigMapSupplier cms) {
    return new RegexLineFilterSupplier(getWps(), cms, () -> getLogger());
  }

  @Override
  protected IBDataRecordTransformer<String, String> getUnconfiguredTransformerInstance(Path workingPath) {
    return new RegexLineFilter(workingPath, getLogger());
  }

  private class RegexLineFilter extends AbstractIBDataRecordTransformer<String, String> {

    public static final String REGEX = "regex";
    public static final String DEFAULT_REGEX = ".*"; // Match pretty much anything
    private final List<String> ACCEPTABLE_TYPES = Arrays.asList(String.class.getCanonicalName());
    private final Pattern splitRegex;

    /**
     * @param ps
     * @param config
     */
    public RegexLineFilter(Path ps, ConfigMap config, Logger l) {
      super(ps, config, l);
      this.splitRegex = Pattern.compile(Pattern.quote(getConfiguration(REGEX, DEFAULT_REGEX)));
    }

    /**
     * @param ps
     */
    public RegexLineFilter(Path ps, Logger l) {
      this(ps, new ConfigMap(), l);
    }

    @Override
    public IBDataRecordTransformer<String, String> configure(ConfigMap cms) {
      return new RegexLineFilter(getWorkingPath(), cms, getLogger());
    }

    @Override
    public String apply(String t) {
      return ofNullable(t).flatMap(s -> ofNullable(splitRegex.matcher(s).matches() ? s : null)).orElse(null);
    }

    @Override
    public String getHint() {
      return REGEX_LINE_FILTER;
    }

    @Override
    public Class<String> getInboundClass() {
      return String.class;
    }

    @Override
    public Class<String> getOutboundClass() {
      return String.class;
    }
  }

  @Override
  public String getHint() {
    return REGEX_LINE_FILTER;
  }

}