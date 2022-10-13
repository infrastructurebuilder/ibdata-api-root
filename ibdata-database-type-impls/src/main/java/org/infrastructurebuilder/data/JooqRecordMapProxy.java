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
package org.infrastructurebuilder.data;

import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.infrastructurebuilder.data.IBDataException;
import org.jooq.Field;
import org.jooq.Record;
import org.slf4j.Logger;

public class JooqRecordMapProxy implements Map<String, Object>, Supplier<GenericRecord> {
  private final Record record;
  private final List<String> names;
  private final Schema s;
  private final Logger log;

  public JooqRecordMapProxy(Record record, Schema s, Logger log) {
    this.record = requireNonNull(record);
    this.names = Arrays.asList(this.record.fields()).stream().map(Field::getName).collect(Collectors.toList());
    this.s = requireNonNull(s);
    this.log = requireNonNull(log);
  }

  @Override
  public int size() {
    return names.size();
  }

  @Override
  public boolean isEmpty() {
    return names.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return names.contains(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return values().contains(value);
  }

  @Override
  public Object get(Object key) {
    int i = names.indexOf(key);
    Object v = record.get(i);
    if (v instanceof java.sql.Date) {
      v = new Date(((java.sql.Date) v).getTime());
    }
    if (v instanceof java.util.Date) {
      v = Instant.ofEpochMilli(((java.util.Date) v).getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }
    return v;
  }

  @Override
  public Object put(String key, Object value) {
    throw new IBDataException("Update of Jooq Record not currently supported");
  }

  @Override
  public Object remove(Object key) {
    throw new IBDataException("Update of Jooq Record not currently supported");
  }

  @Override
  public void putAll(Map<? extends String, ? extends Object> m) {
    throw new IBDataException("Update of Jooq Record not currently supported");
  }

  @Override
  public void clear() {
    throw new IBDataException("Update of Jooq Record not currently supported");
  }

  @Override
  public Set<String> keySet() {
    return names.stream().collect(Collectors.toSet());
  }

  @Override
  public Collection<Object> values() {
    return names.stream().map(record::get).collect(Collectors.toList());
  }

  @Override
  public Set<Entry<String, Object>> entrySet() {
    Map<String, Object> m = new HashMap<>();
    names.forEach(n -> {
      m.put(n, get(n));
    });
    return m.entrySet();
  }

  public GenericRecord get() {
    final HashSet<String> alreadyWarned = new HashSet<>();

    List<String> fieldNames = s.getFields().stream().map(f -> f.name()).collect(Collectors.toList());
    GenericRecordBuilder rb = new GenericRecordBuilder(s);
    keySet().forEach(k -> {
      if (fieldNames.contains(k)) {
        Object v = get(k);
        rb.set(k, get(k));
        //        else {
        //
        //        }
        //        Field v = s.getField(k);
        //        if (v != null) {
        //          r.put(k, managedValue(v.schema(), k, t.get(k), getFormatters()));
      } else {
        if (!alreadyWarned.contains(k)) {
          log.warn("*** Field '" + k + "' not known in schema!  ");
          alreadyWarned.add(k);
        }
      }
    }); // FIXME mebbe we need to catch some of the RuntimeException instances
    return rb.build();

    //      final GenericRecord r = new GenericData.Record(s);
    //      keySet().forEach(k -> {
    //         org.apache.avro.Schema.Field v = s.getField(k);
    //        if (v != null) {
    //          r.put(k, managedValue(v.schema(), k, get(k), formatters));
    //        } else {
    //          if (alreadyWarned.add(k)) {
    //            log.warn("*** Field '" + k + "' not known in schema!  ");
    //            alreadyWarned.add(k);
    //          }
    //        }
    //      }); // FIXME mebbe we need to catch some of the RuntimeException instances
    //      return r;
  }

}
