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
package org.infrastructurebuilder.data.sql;

import static java.util.Objects.requireNonNull;
import static org.apache.avro.Schema.Type.NULL;

import java.util.List;

import org.apache.avro.LogicalTypes;
import org.apache.avro.LogicalTypes.Decimal;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.infrastructurebuilder.data.IBDataException;

public interface IBDataAvroToLiquibaseUtils {
  public static final String TYPE = "type";
  public static final String NAME = "name";
  public static final String COLUMN = "column";

  public static String getLBTypeFromAvroSchemaType(Field field, String name, Schema schema) {
    switch (requireNonNull(schema).getType()) {
    case RECORD:
      throw new IBDataException("Subrecord processing should not happen here");
    case ENUM:
      return "varchar(255)";
    // case ARRAY:
    // throw new IBDataException("Array processing not completed");
    // case MAP:
    // throw new IBDataException("Map processing not completed");
    case UNION:
      //
      List<Schema> l = schema.getTypes();
      if (l.size() != 2 && l.get(0).getType() != NULL)
        throw new IBDataException("Currently a union can only be used to indicate nullability");
      return getLBTypeFromAvroSchemaType(field, name, l.get(1));
    case BOOLEAN:
      return "boolean";
    case BYTES:
    case FIXED:
      if (schema.getLogicalType() != null)
        switch (schema.getLogicalType().getName()) {
        case "decimal":
          LogicalTypes.Decimal _dt = (Decimal) schema.getLogicalType();
          return String.format("DECIMAL(%d,%d)", _dt.getPrecision(), _dt.getScale());
        default:
          throw new IBDataException("Unknown logical type " + schema.getLogicalType().getName());
        }
      return "blob";
    case DOUBLE:
      return "double";
    case FLOAT:
      return "float";
    case INT:
      if (schema.getLogicalType() != null)
        switch (schema.getLogicalType().getName()) {
        case "date":
          return "date";
        case "time-millis":
          return "time";
        default:
          throw new IBDataException("Unknown logical type " + schema.getLogicalType().getName());

        }
      return "int";
    case LONG:
      if (schema.getLogicalType() != null)
        switch (schema.getLogicalType().getName()) {
        case "timestamp-millis":
          return "timestamp";
        default:
          throw new IBDataException("Unknown logical type " + schema.getLogicalType().getName());

        }
      return "bigint";
    // case NULL:
    // return null;
    case STRING:
      if (schema.getLogicalType() != null)
        switch (schema.getLogicalType().getName()) {
        case "uuid":
          return "uuid";
        default:
          throw new IBDataException("Unknown logical type " + schema.getLogicalType().getName());
        }
      return "varchar(255)";
    default:
      throw new IBDataException("Cannot process type" + schema.getType().name());
    }
  }

  public static Xpp3Dom addField(Field field) {
    requireNonNull(field, "field for IBDataAvroToLiquibcaseUtils.addField");
    Xpp3Dom d = new Xpp3Dom(COLUMN);
    d.setAttribute(NAME, field.name());
    d.setAttribute(TYPE, getLBTypeFromAvroSchemaType(field, field.name(), field.schema()));
    return d;
  }
}
