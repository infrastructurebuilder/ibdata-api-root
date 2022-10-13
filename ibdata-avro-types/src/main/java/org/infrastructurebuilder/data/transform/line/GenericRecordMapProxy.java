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

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.avro.Schema.Field;
import org.apache.avro.generic.GenericRecord;

public class GenericRecordMapProxy implements Map<String, Object>, Supplier<GenericRecord> {
  private final GenericRecord r;
  private final List<String> fieldNames;

  public GenericRecordMapProxy(GenericRecord r) {
    this.r = Objects.requireNonNull(r);
    this.fieldNames = this.r.getSchema().getFields().stream().map(Field::name).collect(Collectors.toList());
  }

  @Override
  public GenericRecord get() {
    return this.r;
  }

  @Override
  public int size() {
    return fieldNames.size();
  }

  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  @Override
  public boolean containsKey(Object key) {
    return fieldNames.contains(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return fieldNames.stream().map(r::get).filter(v -> value.equals(v)).findAny().isPresent();
  }

  @Override
  public Object get(Object key) {
    return r.get(key.toString());
  }

  @Override
  public Object put(String key, Object value) {
    if (!fieldNames.contains(key))
      return null;
    Object old = r.get(key);
    r.put(key, value);
    return old;
  }

  @Override
  public Object remove(Object key) {
    // You can't do this
    return null;
  }

  @Override
  public void putAll(Map<? extends String, ? extends Object> m) {
    m.forEach((k, v) -> {
      this.put(k, v);
    });
  }

  @Override
  public void clear() {
    for (Field k : this.r.getSchema().getFields())
      this.put(k.name(), null);
  }

  @Override
  public Set<String> keySet() {
    return fieldNames.stream().collect(toSet());
  }

  @Override
  public Collection<Object> values() {
    return fieldNames.stream().map(r::get).collect(toList());
  }

  @Override
  public Set<Entry<String, Object>> entrySet() {
    // FIXME Incorrect AND inefficient.  Make local Entry.  This doesn't conform to actual Entry specs, because it doesn't reference backing "map"
    Map<String, Object> m = new HashMap<>();
    fieldNames.stream().forEach(k -> m.put(k, get(k)));
    return m.entrySet();
  }
}
