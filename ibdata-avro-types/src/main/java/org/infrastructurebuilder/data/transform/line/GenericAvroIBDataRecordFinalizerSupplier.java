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
import static org.infrastructurebuilder.data.IBDataStructuredDataMetadataType.FLOAT;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.avro.AvroRuntimeException;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.IndexedRecord;
import org.infrastructurebuilder.util.constants.IBConstants;
import org.infrastructurebuilder.data.IBDataAvroUtils;
import org.infrastructurebuilder.data.IBDataDataStreamRecordFinalizerSupplier;
import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.data.IBDataStreamRecordFinalizer;
import org.infrastructurebuilder.data.IBDataStructuredDataMetadata;
import org.infrastructurebuilder.data.IBDataStructuredDataMetadataType;
import org.infrastructurebuilder.data.model.DataStreamStructuredMetadata;
import org.infrastructurebuilder.data.model.StructuredFieldMetadata;
import org.infrastructurebuilder.util.LoggerSupplier;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.config.PathSupplier;
import org.slf4j.Logger;

@Named(GenericAvroIBDataRecordFinalizerSupplier.NAME)
public class GenericAvroIBDataRecordFinalizerSupplier
    extends AbstractIBDataStreamRecordFinalizerSupplier<GenericRecord> {

  public static final String NAME = "avro-generic";
  private static final List<Class<?>> ACCEPTABLE_TYPES = Arrays.asList(IndexedRecord.class);

  @Inject
  public GenericAvroIBDataRecordFinalizerSupplier(@Named(IBDATA_WORKING_PATH_SUPPLIER) PathSupplier wps,
      LoggerSupplier l) {
    this(wps, l, null);
  }

  private GenericAvroIBDataRecordFinalizerSupplier(PathSupplier ps, LoggerSupplier l, ConfigMapSupplier cms) {
    super(ps, l, cms);
  }

  @Override
  public IBDataDataStreamRecordFinalizerSupplier<GenericRecord> configure(ConfigMapSupplier cms) {
    return new GenericAvroIBDataRecordFinalizerSupplier(getWps(), () -> getLog(), cms);
  }

  @Override
  public IBDataStreamRecordFinalizer<GenericRecord> get() {
    // The working path needs to be stable and pre-existent
    return new GenericAvroIBDataStreamRecordFinalizer(NAME, getWps().get().resolve(UUID.randomUUID().toString()),
        getLog(), getCms().get());
  }

  public final class GenericAvroIBDataStreamRecordFinalizer
      extends AbstractIBDataStreamRecordFinalizer<GenericRecord, DataFileWriter<GenericRecord>> {

    private final int numberOfRowsToSkip;

    public GenericAvroIBDataStreamRecordFinalizer(String id, Path workingPath, Logger l, ConfigMap map) {
      super(id, workingPath, l, map, Optional.of(IBDataAvroUtils.fromMapAndWP.apply(workingPath, map)));
      this.numberOfRowsToSkip = Integer.parseInt(map.getOrDefault(NUMBER_OF_ROWS_TO_SKIP_PARAM, "0"));
    }

    @Override
    public int getNumberOfRowsToSkip() {
      return this.numberOfRowsToSkip;
    }

    @Override
    protected void writeThrows(GenericRecord recordToWrite) throws Throwable {
      getWriter().append(recordToWrite);
    }

    @Override
    public Optional<String> produces() {
      return Optional.of(IBConstants.AVRO_BINARY);
    }

    @Override
    public Optional<List<Class<?>>> accepts() {
      return Optional.of(ACCEPTABLE_TYPES);
    }

    @Override
    protected DataStreamStructuredMetadata updateStructuredMetadata(DataStreamStructuredMetadata current,
        GenericRecord recordToWrite) {
      Field[] fields = recordToWrite.getSchema().getFields().toArray(new Field[0]);
      if (current == null) {
        current = new DataStreamStructuredMetadata();
        for (int i = 0; i < fields.length; ++i) {
          StructuredFieldMetadata fm = new StructuredFieldMetadata();
          fm.setIndex(i);
          IBDataStructuredDataMetadataType mdt = getMetadataTypeFromField(fields[i]);
          fm.setMetadataType(mdt.name());
          fm.setNullable(new Boolean(fields[i].schema().isNullable()).toString());
          try {
            fm.setEnumerations(fields[i].schema().getEnumSymbols());
          } catch (AvroRuntimeException a) {
            fm.setEnumerations(null);
          }
          current.addField(fm);
        }
      }
      for (int i = 0; i < fields.length; ++i) {
        int j = i;
        StructuredFieldMetadata element = current.getFields().get(i);
        element.getType().ifPresent(mdt -> {
          BigInteger v2;
          Object val = recordToWrite.get(j);
          BigInteger max2;
          BigInteger min2;
          long ll;
          switch (mdt) {
          case DOUBLE:
          case FLOAT:
            BigDecimal v = new BigDecimal((double) val);
            BigDecimal min = element.getMinRealValue().orElse(v);
            if (v.compareTo(min) <= 0)
              element.setMin(v.toString());
            BigDecimal max = element.getMaxRealValue().orElse(v);
            if (v.compareTo(max) >= 0)
              element.setMax(v.toString());
            break;
          case INT:
          case LONG:
          case STRING:
          case BYTES:
            switch (mdt) {
            case BYTES:
              ll = ((byte[]) val).length;
              break;
            case INT:
              val = ((Integer)val).longValue();
            case LONG:
              ll = (long) val;
              break;
            case STRING:
              ll = (long)( val.toString()).length();
              break;
            default:
              throw new IBDataException("Impossible");
            }
            v2 = BigInteger.valueOf(ll);
            min2 = element.getMinIntValue().orElse(v2);
            max2 = element.getMaxIntValue().orElse(v2);
            if (v2.compareTo(min2) <= 0)
              element.setMin(v2.toString());
            if (v2.compareTo(max2) >= 0)
              element.setMax(v2.toString());
            break;
          case DATE:
            break;
          case TIMESTAMP:
            break;
          default:
            break;
          }
        });
      }
      return current;
    }

    private IBDataStructuredDataMetadataType getMetadataTypeFromField(Field field) {
      IBDataStructuredDataMetadataType t;
      Schema s = field.schema();
      if (s.isUnion()) {
        s = s.getTypes().get(s.getTypes().size() - 1);
      }
      switch (s.getType()) {
      case ENUM:
        return IBDataStructuredDataMetadataType.INT;
      case BOOLEAN:
      case BYTES:
      case DOUBLE:
      case FLOAT:
      case INT:
      case LONG:
      case STRING:
        String thisTypeString = s.getType().getName().toUpperCase();
        return IBDataStructuredDataMetadataType.valueOf(thisTypeString);
      //      case MAP:
      //        break;
      //      case NULL:
      //        break;
      //      case RECORD:
      //        break;
      //      case ARRAY:
      //        break;
      //      case FIXED:
      //        break;
      //      case UNION:
      //        break;
      default:
        return null;
      }
    }

  }

}
