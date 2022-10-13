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

import java.lang.reflect.Array;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.core.LoggerSupplier;
import org.infrastructurebuilder.util.core.PathSupplier;
import org.slf4j.Logger;

import com.opencsv.CSVParserBuilder;
import com.opencsv.ICSVParser;

@Named(OpenCSVToNameMapIBDataLineTransformerSupplier.STRING_TO_NAME_MAP)
public class OpenCSVToNameMapIBDataLineTransformerSupplier
    extends AbstractIBDataRecordTransformerSupplier<String, Map<String, String>> {
  public static final String STRING_TO_NAME_MAP = "opencsv-to-name-map";

  @javax.inject.Inject
  public OpenCSVToNameMapIBDataLineTransformerSupplier(@Named(IBDATA_WORKING_PATH_SUPPLIER) PathSupplier wps,
      LoggerSupplier l) {
    this(wps, null, l);
  }

  private OpenCSVToNameMapIBDataLineTransformerSupplier(PathSupplier wps, ConfigMapSupplier cms, LoggerSupplier l) {
    super(wps, cms, l);
  }

  @Override
  public OpenCSVToNameMapIBDataLineTransformerSupplier configure(ConfigMapSupplier cms) {
    return new OpenCSVToNameMapIBDataLineTransformerSupplier(getWps(), cms, () -> getLogger());
  }

  @Override
  protected IBDataRecordTransformer<String, Map<String, String>> getUnconfiguredTransformerInstance(Path workingPath) {
    return new StringToNameMapIBDataLineTransformer(workingPath, getLogger());
  }

  @Override
  public String getHint() {
    return STRING_TO_NAME_MAP;
  }

  private class StringToNameMapIBDataLineTransformer
      extends AbstractIBDataRecordTransformer<String, Map<String, String>> {

    private final List<String> ACCEPTABLE_TYPES = Arrays.asList(Array.class.getCanonicalName());
    private final ICSVParser parser;

    /**
     * @param ps
     * @param config
     */
    @SuppressWarnings("unchecked")
    protected StringToNameMapIBDataLineTransformer(Path ps, ConfigMap config, Logger l) {
      super(ps, config, l);

      if (config != null) {
        CSVParserBuilder builder = new CSVParserBuilder();
        parser = builder.build();
      } else {
        parser = null;
      }

      //      try {
      //        this.format = (List<String>) getOptionalObjectConfiguration(FIELD_KEY)
      //            .orElseThrow(() -> new IBDataException("No " + FIELD_KEY + " found in config"));
      //      } catch (ClassCastException e) {
      //        throw new IBDataException("Type of " + FIELD_KEY + " was not List<String>", e);
      //      }
    }

    /**
     * @param ps
     */
    protected StringToNameMapIBDataLineTransformer(Path ps, Logger l) {
      this(ps, null, l);
    }

    @Override
    public Map<String, String> apply(String a) {
      //      m = parser.p
      Map<String, String> m = new HashMap<>();
      return m;
    }

    @Override
    public String getHint() {
      return STRING_TO_NAME_MAP;
    }

    @Override
    public IBDataRecordTransformer<String, Map<String, String>> configure(ConfigMap cms) {
      return new StringToNameMapIBDataLineTransformer(getWorkingPath(), cms, getLogger());
    }

    @Override
    public Class<String> getInboundClass() {
      return String.class;
    }

    @Override
    public Class<Map<String, String>> getOutboundClass() {
      Map<String, String> c = new HashMap<>();
      return (Class<Map<String, String>>) c.getClass();
    }
  }

}
