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
import java.util.Optional;

import javax.inject.Named;

import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.core.LoggerSupplier;
import org.infrastructurebuilder.util.core.PathSupplier;
import org.slf4j.Logger;

@Named(ArrayToNumberedColumIBDataLineTransformerSupplier.ARRAY_TO_NUMBERED_COL)
public class ArrayToNumberedColumIBDataLineTransformerSupplier
    extends AbstractIBDataRecordTransformerSupplier<String[], Map<String, String>> {
  public static final String ARRAY_TO_NUMBERED_COL = "array-to-numbered-column";

  @javax.inject.Inject
  public ArrayToNumberedColumIBDataLineTransformerSupplier(@Named(IBDATA_WORKING_PATH_SUPPLIER) PathSupplier wps,
      LoggerSupplier l) {
    this(wps, null, l);
  }

  private ArrayToNumberedColumIBDataLineTransformerSupplier(PathSupplier wps, ConfigMapSupplier cms, LoggerSupplier l) {
    super(wps, cms, l);
  }

  @Override
  public ArrayToNumberedColumIBDataLineTransformerSupplier configure(ConfigMapSupplier cms) {
    return new ArrayToNumberedColumIBDataLineTransformerSupplier(getWps(), cms, () -> getLogger());
  }

  @Override
  protected IBDataRecordTransformer<String[], Map<String, String>> getUnconfiguredTransformerInstance(
      Path workingPath) {
    return new ArrayToNumberedColumIBDataLineTransformer(workingPath, getLogger());
  }

  @Override
  public String getHint() {
    return ARRAY_TO_NUMBERED_COL;
  }

  private class ArrayToNumberedColumIBDataLineTransformer
      extends AbstractIBDataRecordTransformer<String[], Map<String, String>> {

    public static final String FORMAT_KEY = "format";
    public final static String FORMAT = "COLUMN%00d";
    private final List<Class<?>> ACCEPTABLE_TYPES = Arrays.asList(Array.class);
    private final String format;

    /**
     * @param ps
     * @param config
     */
    protected ArrayToNumberedColumIBDataLineTransformer(Path ps, ConfigMap config, Logger l) {
      super(ps, config, l);
      this.format = getConfiguration(FORMAT_KEY, FORMAT);
    }

    /**
     * @param ps
     */
    protected ArrayToNumberedColumIBDataLineTransformer(Path ps, Logger l) {
      super(ps, l);
      this.format = null;
    }

    @Override
    public Map<String, String> apply(String[] a) {
      Map<String, String> m = new HashMap<>();
      for (int i = 0; i < a.length; ++i) {
        m.put(String.format(format, i), a[i]);
      }
      return m;
    }

    @Override
    public String getHint() {
      return ARRAY_TO_NUMBERED_COL;
    }

    @Override
    public IBDataRecordTransformer<String[], Map<String, String>> configure(ConfigMap cms) {
      return new ArrayToNumberedColumIBDataLineTransformer(getWorkingPath(), cms, getLogger());
    }

    @Override
    public Optional<List<Class<?>>> accepts() {
      return Optional.of(ACCEPTABLE_TYPES);
    }

    @Override
    public Optional<Class<?>> produces() {
      return Optional.of(Map.class);
    }

    @Override
    public Class<String[]> getInboundClass() {
      return String[].class;
    }

    @Override
    public Class<Map<String, String>> getOutboundClass() {
      Map<String, String> c = new HashMap<>();
      return (Class<Map<String, String>>) c.getClass();
    }

  }

}
