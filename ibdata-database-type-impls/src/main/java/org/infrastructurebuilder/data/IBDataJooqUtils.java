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

import static java.sql.JDBCType.valueOf;
import static java.util.Optional.ofNullable;
import static org.apache.avro.SchemaBuilder.builder;
import static org.apache.avro.SchemaBuilder.nullable;

import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
import org.apache.avro.Schema.Type;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.SchemaBuilder.ArrayBuilder;
import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.util.core.IBUtils;
import org.jooq.Catalog;
import org.jooq.DataType;
import org.jooq.EnumType;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.UDTRecord;
import org.jooq.tools.reflect.Reflect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class IBDataJooqUtils {
  private final static Logger log = LoggerFactory.getLogger(IBDataJooqUtils.class);

  public static Field getFieldFromType(String key, org.jooq.Field<?> field, DataType<?> dt, boolean isNullable) {
    log.trace("key = {}, field = {}, dt = {}, isNullable = {}", key, field, dt, isNullable);
    Schema schema, logicalField;
    Field f1;
    JDBCType jdbcType = valueOf(dt.getSQLType());
    Optional<String> comment = ofNullable(IBUtils.nullIfBlank.apply(field.getComment())); // FIXME Apply the comment?
    String castType = dt.getCastTypeName();
    if (dt.getCastTypeName().equals("boolean")) {
      f1 = new Field(key, isNullable ? nullable().booleanType() : builder().booleanType());
    } else if (dt.getCastTypeName().equals("bigint")) {
      f1 = new Field(key, isNullable ? nullable().longType() : builder().longType());
    } else if (dt.getCastTypeName().equals("decimal_integer")) {
      f1 = new Field(key, isNullable ? nullable().longType() : builder().longType());
    } else if (dt.getCastTypeName().equals("integer")) {
      f1 = new Field(key, isNullable ? nullable().intType() : builder().intType());
    } else if (dt.isString() || dt.isEnum() /* FIXME see isEnum() below in comments */) {
      f1 = new Field(key, isNullable ? nullable().stringType() : builder().stringType());
    } else
    // Non primitive-esqe types.
    if (dt.isArray()) {
      schema = SchemaBuilder.array()
          .items(getFieldFromType(key, field, Reflect.on(dt).get("elementType"), false).schema());
      if (isNullable) {
        schema = SchemaBuilder.builder().unionOf().nullType().and().type(schema).endUnion();
      }
      f1 = new Field(key, schema);
    } else if (dt.isDate()) {
      logicalField = Schema.create(Schema.Type.INT);
      schema = LogicalTypes.date().addToSchema(logicalField);
      f1 = new Field(key, isNullable ? nullable().type(schema) : schema);
    } else if (dt.isDateTime() || dt.isTimestamp()) {
      logicalField = Schema.create(Schema.Type.INT);
      schema = LogicalTypes.timeMillis().addToSchema(logicalField);
      f1 = new Field(key, isNullable ? nullable().type(schema) : schema);
    } else if (dt.isInterval()) {
      // Representation as a Long (in ms, so you can map to Duration::ofMillis)
      schema = Schema.create(Schema.Type.LONG);
      f1 = new Field(key, isNullable ? nullable().type(schema) : schema);
    } else if (dt.isTime()) {
      logicalField = Schema.create(Schema.Type.INT);
      schema = LogicalTypes.timeMillis().addToSchema(logicalField);
      f1 = new Field(key, isNullable ? nullable().type(schema) : schema);
    } else if (dt.isNumeric()) {
      // We could acquire additional numeric types instead of just long,
      // but AFAIK this code is unreachable
      // because only int and long are Numerics
      //      f1 = new Field(key, isNullable ? nullable().longType() : builder().longType());
      throw new IBDataException(
          "NUMERIC Type " + dt + "/" + castType + " of field '" + field.getName() + "' cannot be processed");
    } else if (dt.isUDT()) {
      UDTRecord<?> record = (UDTRecord<?>) dt;
      // FIXME Make UDTs work.  Somehow it could become a record
      throw new IBDataException("UDT Type " + dt + " of field '" + field.getName() + "' cannot be processed");

      // All enumeration types will be strings in the generated avro
      //    } else if (dt.isEnum()) {
      // Force all enumerations to be strings
      //      EnumType t = (EnumType) dt;
      //      Catalog q = t.getCatalog();
      //      Optional<org.jooq.Schema> s = Optional.ofNullable(t.getSchema());
      //      List<String> symbols = new ArrayList<>();
      //      schema = SchemaBuilder.enumeration(key).symbols(symbols.toArray(new String[0]));
      //      f1 = new Field(key, isNullable ? nullable().type(schema) : schema);
      // TODO If we can figure out how to get the symbols for the enumeration, this will work better
      //      f1 = new Field(key, isNullable ? nullable().stringType() : builder().stringType());
    } else if (dt.isLob()) {
      // FIXME Make LOBs work
      throw new IBDataException("xLOB Type " + dt + " of field '" + field.getName() + "' cannot be processed");
    } else
      throw new IBDataException("Type " + dt + " of field '" + field.getName() + "' cannot be processed");
    return f1;
  }

  public final static Schema schemaFromRecordResults(Logger log, String namespace, String name, String doc,
      Result<Record> records) {
    log.warn("Reading entire dataset (" + records.size() + " records) to determine schema.");

    List<String> names = new ArrayList<>();
    Set<String> nullFields = new HashSet<>();
    int i;
    int row = 0;
    String key;
    org.jooq.Field<?> field;
    Record r = null;
    for (Record r2d2 : records) {
      row++;
      r = r2d2; // For last-record processing
      if (r.size() == 0)
        throw new IBDataException("No fields in record");
      for (i = 0; i < r.size(); ++i) {
        field = r.field(i);
        key = field.getName();
        if (r.getValue(i) == null) {
          if (nullFields.add(key)) {
            log.debug("Field '" + key + "' contains null value at row " + row);
          }
        }
      }
    }
    // r was the last record read
    if (r == null)
      throw new IBDataException("No records read");
    Map<String, org.apache.avro.Schema.Field> fields = new HashMap<>();
    for (i = 0; i < r.size(); ++i) {
      field = r.field(i);
      key = field.getName();
      names.add(key);
      fields.put(key, getFieldFromType(key, field, field.getDataType(), nullFields.contains(key)));
    }

    List<Field> l = names.stream().map(fields::get).collect(Collectors.toList());
    return Schema.createRecord(name, doc, namespace, false, l);
  }

}
