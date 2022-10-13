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

import static java.util.Objects.requireNonNull;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.infrastructurebuilder.util.config.ConfigMap;
import org.slf4j.Logger;

abstract public class AbstractIBDataRecordTransformer<I, O> implements IBDataRecordTransformer<I, O> {

  private final Path workingPath;
  private final ConfigMap config;
  private final Logger logger;

  public AbstractIBDataRecordTransformer(Path ps, Logger l) {
    this(ps, null, l);
  }

  protected AbstractIBDataRecordTransformer(Path ps, ConfigMap config, Logger l) {
    this.workingPath = requireNonNull(ps);
    this.config = config;
    this.logger = requireNonNull(l);
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  protected Path getWorkingPath() {
    return workingPath;
  }

  protected ConfigMap getConfig() {
    return config;
  }

  protected String getConfiguration(String key) {
    return getConfig().getString(/* FIXME !!!!! getHint() + "." + */ key);
  }

  protected Object getObjectConfiguration(String key, Object defaultValue) {
    return getConfig().getOrDefault(key, defaultValue);
  }

  protected String getConfiguration(String key, String defaultValue) {
    return getConfig().getOrDefault(/* FIXME getHint() + "." + */ key, defaultValue);
  }

  protected Optional<String> getOptionalConfiguration(String key) {
    return ofNullable(getConfiguration(key, null));
  }

  protected Optional<Object> getOptionalObjectConfiguration(String key) {
    return ofNullable(getObjectConfiguration(key, null));
  }

  @SuppressWarnings("unchecked")
  protected Optional<I> getTypedObject(Object in) {
    try {
      return ofNullable((I) in);
    } catch (ClassCastException e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<Class<?>> produces() {
    return Optional.of(getOutboundClass());
  }

  @Override
  public Optional<List<Class<?>>> accepts() {
    return of(Arrays.asList(getInboundClass()));
  }

  @Override
  public boolean respondsTo(Object o) {
    return getTypedObject(o).isPresent();
  }

}