/*
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
package org.infrastructurebuilder.data.transform.base;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static org.infrastructurebuilder.data.IBDataConstants.MAP_SPLITTER;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.infrastructurebuilder.util.config.ConfigMap;

public class Record {

  public static final String FIELD_MAP = "fieldMap";
  public static final String FIELD_KEY = "fields";
  private String id;
  private String hint;
  private ConfigMap config = new ConfigMap();

  public void setFields(List<String> fields) {
    this.config.put(FIELD_KEY, fields);
  }

  public void setFieldMap(Map<String, String> fieldMap) {
    this.config.put(FIELD_MAP, fieldMap);
  }

  public String getId() {
    return id;
  }

  public String getHint() {
    return ofNullable(hint).orElse(id);
  }

  public ConfigMap getConfig() {
    return config;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setHint(String hint) {
    this.hint = hint;
  }

  public void setConfig(ConfigMap config) {
    ConfigMap local = this.config;
    this.config = config;
    local.forEach((k,v) -> {
      this.config.put(k,v);
    });
    this.config.putAll(local);
  }

  public String joinKey() {
    return requireNonNull(getId()) + MAP_SPLITTER + requireNonNull(getHint());
  }

  public ConfigMap configurationAsMap() {
    ConfigMap map = new ConfigMap(getConfig().entrySet().stream().collect(Collectors.toMap(
        // Key is id + hint (possibly same) joined by a splittable item
        k -> /* FIXME joinKey() + "." + */ k.getKey(), v -> v.getValue())));
    // TODO Add fields and fieldsMap
    return map;
  }

}
