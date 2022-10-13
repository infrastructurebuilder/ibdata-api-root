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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
import org.apache.avro.generic.GenericRecordBuilder;
import org.apache.avro.generic.IndexedRecord;
import org.infrastructurebuilder.data.Formatters;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.slf4j.Logger;

abstract public class AbstractMapToAvroGenericRecordIBDataLineTransformer
    extends AbstractAvroGenericIBDataLineTransformer<Map<String, Object>> {

  private final List<String> alreadyWarned = new ArrayList<>();

  protected AbstractMapToAvroGenericRecordIBDataLineTransformer(Path workingPath, ConfigMap config, Logger l) {
    super(workingPath, config, l);
  }

  @Override
  public IndexedRecord apply(Map<String, Object> t) {
    Objects.requireNonNull(t);
    Schema s = getSchema();
    GenericRecordBuilder rb = new GenericRecordBuilder(s);
    //    final GenericRecord r = new GenericData.Record(s);
    t.keySet().forEach(k -> {
      Field f = s.getField(k);
      if (f != null) {
        rb.set(f, t.get(k));
        //      else {
        //
        //      }
        //      Field v = s.getField(k);
        //      if (v != null) {
        //        r.put(k, managedValue(v.schema(), k, t.get(k), getFormatters()));
      } else {
        if (!alreadyWarned.contains(k)) {
          getLogger().warn("*** Field '" + k + "' not known in schema!  ");
          alreadyWarned.add(k);
        }
      }
    }); // FIXME mebbe we need to catch some of the RuntimeException instances
    return rb.build();
  }

  abstract public Schema getSchema();

  abstract Locale getLocale();

  abstract Formatters getFormatters();

}
