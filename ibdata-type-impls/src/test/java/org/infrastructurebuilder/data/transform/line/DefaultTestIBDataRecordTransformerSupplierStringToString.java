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

import java.nio.file.Path;

import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.core.LoggerSupplier;
import org.infrastructurebuilder.util.core.PathSupplier;
import org.slf4j.Logger;

public class DefaultTestIBDataRecordTransformerSupplierStringToString
    extends AbstractIBDataRecordTransformerSupplier<String, String> {

  protected DefaultTestIBDataRecordTransformerSupplierStringToString(PathSupplier wps, ConfigMapSupplier cms, LoggerSupplier l) {
    super(wps, cms, l);
  }

  @Override
  public String getHint() {
    return DefaultTestIBDataRecordTransformerSupplierStringToString.class.getCanonicalName();
  }

  @Override
  public AbstractIBDataRecordTransformerSupplier<String, String> configure(ConfigMapSupplier cms) {
    return this;
  }

  @Override
  protected IBDataRecordTransformer<String, String> getUnconfiguredTransformerInstance(Path workingPath) {
    return new StringToStringRecordTransformer(getWps().get(), getLogger());
  }

  public static class StringToStringRecordTransformer extends AbstractIBDataRecordTransformer<String, String> {

    public StringToStringRecordTransformer(Path path, Logger l) {
      super(path, l);
    }

    private StringToStringRecordTransformer(Path workingPath, ConfigMap config, Logger l) {
      super(workingPath, config, l);
    }

    @Override
    public String getHint() {
      return StringToStringRecordTransformer.class.getCanonicalName();
    }

    @Override
    public IBDataRecordTransformer<String,String> configure(ConfigMap config) {
      return new StringToStringRecordTransformer(getWorkingPath(), config, getLogger());
    }

    @Override
    public String apply(String t) {
      return t;
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
