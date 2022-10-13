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
import static org.infrastructurebuilder.data.IBDataAvroUtils.NO_SCHEMA_CONFIG_FOR_MAPPER;
import static org.infrastructurebuilder.data.IBDataAvroUtils.avroSchemaFromString;
import static org.infrastructurebuilder.data.IBDataConstants.IBDATA_WORKING_PATH_SUPPLIER;
import static org.infrastructurebuilder.data.IBDataConstants.LOCALE_LANGUAGE_PARAM;
import static org.infrastructurebuilder.data.IBDataConstants.LOCALE_REGION_PARAM;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.IndexedRecord;
import org.infrastructurebuilder.data.Formatters;
import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.util.LoggerSupplier;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.config.PathSupplier;
import org.slf4j.Logger;

@Named(DefaultMapToGenericRecordIBDataLineTransformerSupplier.NAME)
public class DefaultMapToGenericRecordIBDataLineTransformerSupplier
    extends AbstractIBDataRecordTransformerSupplier<Map<String, Object>, IndexedRecord> {
  public final static String NAME = "map-to-generic-avro";
  public static final List<String> ACCEPTABLE_TYPES = Arrays.asList(Map.class.getCanonicalName());
  public final static String SCHEMA_PARAM = "schema"; // Required **

  @Inject
  public DefaultMapToGenericRecordIBDataLineTransformerSupplier(@Named(IBDATA_WORKING_PATH_SUPPLIER) PathSupplier wps,
      LoggerSupplier l) {
    this(wps, null, l);
  }

  private DefaultMapToGenericRecordIBDataLineTransformerSupplier(PathSupplier wps, ConfigMapSupplier cms,
      LoggerSupplier l) {
    super(wps, cms, l);
  }

  @Override
  public String getHint() {
    return NAME;
  }

  @Override
  public DefaultMapToGenericRecordIBDataLineTransformerSupplier configure(ConfigMapSupplier cms) {
    return new DefaultMapToGenericRecordIBDataLineTransformerSupplier(getWps(), cms, () -> getLogger());
  }

  @Override
  protected IBDataRecordTransformer<Map<String, Object>, IndexedRecord> getUnconfiguredTransformerInstance(
      Path workingPath) {
    return new DefaultMapSSToGenericRecordIBDataLineTransformer(workingPath, getLogger());
  }

  public static class DefaultMapSSToGenericRecordIBDataLineTransformer
      extends AbstractMapToAvroGenericRecordIBDataLineTransformer {

    private final Schema schema;
    private final Formatters formatters;

    /**
     * @param workingPath
     * @param config
     */
    public DefaultMapSSToGenericRecordIBDataLineTransformer(Path workingPath, ConfigMap config, Logger l) {
      super(workingPath, config, l);

      if (config != null && !config.keySet().contains(SCHEMA_PARAM))
        throw new IBDataException(NO_SCHEMA_CONFIG_FOR_MAPPER);
      this.schema = config == null ? null
          : avroSchemaFromString.apply(ofNullable(getConfiguration(SCHEMA_PARAM))
              .orElseThrow(() -> new IBDataException(NO_SCHEMA_CONFIG_FOR_MAPPER + " (invalid?)")));
      this.formatters = new Formatters(Optional.ofNullable(getConfig()).orElse(new ConfigMap()));
    }

    /**
     * @param workingPath
     */
    public DefaultMapSSToGenericRecordIBDataLineTransformer(Path workingPath, Logger l) {
      this(workingPath, null, l);
    }

    @Override
    Formatters getFormatters() {
      return this.formatters;
    }

    @Override
    public Schema getSchema() {
      return this.schema;
    }

    @Override
    Locale getLocale() {
      return ofNullable(getConfiguration(LOCALE_LANGUAGE_PARAM)).map(language -> {
        Locale.Builder lb = new Locale.Builder().setLanguage(language);
        ofNullable(getConfiguration(LOCALE_REGION_PARAM)).ifPresent(region -> lb.setRegion(region));
        return lb.build();
      }).orElse(Locale.getDefault());
    }

    @Override
    public String getHint() {
      return NAME;
    }

    @Override
    public IBDataRecordTransformer<Map<String, Object>, IndexedRecord> configure(ConfigMap cms) {
      return new DefaultMapSSToGenericRecordIBDataLineTransformer(getWorkingPath(), cms, getLogger());
    }

    @Override
    public Class<Map<String, Object>> getInboundClass() {
      Class<?> clazz = Map.class;
      return (Class<Map<String, Object>>) clazz;
    }

    @Override
    public Class<IndexedRecord> getOutboundClass() {
      return IndexedRecord.class;
    }

  }

}