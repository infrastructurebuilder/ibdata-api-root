
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
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.inject.Named;

import org.infrastructurebuilder.data.IBDataConstants;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.core.LoggerSupplier;
import org.infrastructurebuilder.util.core.PathSupplier;
import org.slf4j.Logger;

@Named(RandomlyPickLineFilterSupplier.RANDMOM_LINE_FILTER)
public class RandomlyPickLineFilterSupplier extends AbstractIBDataRecordTransformerSupplier<Object, Object> {
  public static final String RANDMOM_LINE_FILTER = "random-line-filter";

  @javax.inject.Inject
  public RandomlyPickLineFilterSupplier(@Named(IBDATA_WORKING_PATH_SUPPLIER) PathSupplier wps, LoggerSupplier l) {
    this(wps, null, l);
  }

  private RandomlyPickLineFilterSupplier(PathSupplier wps, ConfigMapSupplier cms, LoggerSupplier l) {
    super(wps, cms, l);
  }

  @Override
  public RandomlyPickLineFilterSupplier configure(ConfigMapSupplier cms) {
    return new RandomlyPickLineFilterSupplier(getWps(), cms, () -> getLogger());
  }

  @Override
  protected IBDataRecordTransformer<Object, Object> getUnconfiguredTransformerInstance(Path workingPath) {
    return new RandomLineFilter(workingPath, getLogger());
  }

  @Override
  public String getHint() {
    return RANDMOM_LINE_FILTER;
  }

  private class RandomLineFilter extends AbstractIBDataRecordTransformer<Object, Object> {

    public static final String RANDOMVAL = "percentage";
    public static final String DEFAULT_RANDOM = ".5"; // Match pretty much anything
    private final List<String> ACCEPTABLE_TYPES = Arrays.asList(IBDataConstants.ANY_TYPE);
    private final float random;
    private final Random randomGen = new Random(Instant.now().toEpochMilli());

    /**
     * @param ps
     * @param config
     */
    public RandomLineFilter(Path ps, ConfigMap config, Logger l) {
      super(ps, config, l);
      this.random = Float.parseFloat((getConfiguration(RANDOMVAL, DEFAULT_RANDOM)));
    }

    /**
     * @param ps
     */
    public RandomLineFilter(Path ps, Logger l) {
      this(ps, new ConfigMap(), l);
    }

    @Override
    public IBDataRecordTransformer<Object, Object> configure(ConfigMap cms) {
      return new RandomLineFilter(getWorkingPath(), cms, getLogger());
    }

    @Override
    public Object apply(Object t) {

      return ofNullable(t).flatMap(s -> ofNullable(randomGen.nextFloat() < random ? s : null)).orElse(null);
    }

    @Override
    public String getHint() {
      return RANDMOM_LINE_FILTER;
    }

    @Override
    public Class<Object> getInboundClass() {
      return Object.class;
    }

    @Override
    public Class<Object> getOutboundClass() {
      return Object.class;
    }

  }

}