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

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import javax.inject.Named;

import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.core.LoggerSupplier;
import org.infrastructurebuilder.util.core.PathSupplier;
import org.slf4j.Logger;

@Named(StringTrimIBDataLineTransformerSupplier.STRING_TRIM)
public class StringTrimIBDataLineTransformerSupplier extends AbstractIBDataRecordTransformerSupplier<String, String> {
  public static final String STRING_TRIM = "string-trim";
  private static final List<String> ACCEPTABLE_TYPES = Arrays.asList(String.class.getCanonicalName());

  @javax.inject.Inject
  public StringTrimIBDataLineTransformerSupplier(PathSupplier wps, LoggerSupplier l) {
    this(wps, null, l);
  }

  private StringTrimIBDataLineTransformerSupplier(PathSupplier wps, ConfigMapSupplier cms, LoggerSupplier l) {
    super(wps, cms, l);
  }

  @Override
  public AbstractIBDataRecordTransformerSupplier<String, String> configure(ConfigMapSupplier cms) {
    return new StringTrimIBDataLineTransformerSupplier(getWps(), cms, () -> getLogger());
  }

  @Override
  protected IBDataRecordTransformer<String, String> getUnconfiguredTransformerInstance(Path workingPath) {
    return new StringTrimIBDataLineTransformer(workingPath, getLogger());
  }

  @Override
  public String getHint() {
    return STRING_TRIM;
  }

  private class StringTrimIBDataLineTransformer extends AbstractIBDataRecordTransformer<String, String> {

    /**
     * @param ps
     * @param config
     */
    protected StringTrimIBDataLineTransformer(Path ps, ConfigMap config, Logger l) {
      super(ps, config, l);
    }

    /**
     * @param ps
     */
    protected StringTrimIBDataLineTransformer(Path ps, Logger l) {
      super(ps, l);
    }

    @Override
    public String apply(String t) {
      return ofNullable(t).map(s -> s.trim()).orElse(null);
    }

    @Override
    public String getHint() {
      return STRING_TRIM;
    }

    @Override
    public IBDataRecordTransformer<String, String> configure(ConfigMap cms) {
      return new StringTrimIBDataLineTransformer(getWorkingPath(), cms, getLogger());
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

}
