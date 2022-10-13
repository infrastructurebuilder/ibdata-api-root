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
package org.apache.avro.generic;

import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.concurrent.TimeUnit;

import org.apache.avro.LogicalType;
import org.apache.avro.Schema;
import org.apache.avro.data.TimeConversions;
import org.infrastructurebuilder.data.Formatters;

public class MapProxyGenericData extends GenericData {
  public final static long millisInAYear = 86400000L;

  public MapProxyGenericData(Formatters f) {
    super();
    requireNonNull(f, "Formatters for MapProxyGenericData");
    //    addLogicalTypePreConversion(new IBDataJRMPDateConversion(f.getDateFormatter()));
    //    addLogicalTypePreConversion(new IBDataJRMPTimeConversion(f.getTimeFormatter()));
    //    addLogicalTypePreConversion(new IBDataJRMPTimestampConversion(f.getTimestampFormatter()));
    addLogicalTypeConversion(new IBDataJRMPDateConversion(f.getDateFormatter()));
    addLogicalTypeConversion(new IBDataJRMPTimeConversion(f.getTimeFormatter()));
    addLogicalTypeConversion(new IBDataJRMPTimestampConversion(f.getTimestampFormatter()));
    addLogicalTypeConversion(new TimeConversions.DateConversion());
    addLogicalTypeConversion(new TimeConversions.TimeMillisConversion());
    addLogicalTypeConversion(new TimeConversions.TimeMicrosConversion());
    addLogicalTypeConversion(new TimeConversions.TimestampMillisConversion());
    addLogicalTypeConversion(new TimeConversions.TimestampMicrosConversion());

  }

  abstract public static class AbstractStringPreConversion<O> extends PreConversion<String, O> {
    @Override
    public Class<String> getPreconversionType() {
      return String.class;
    }
  }

  public static class IBDataJRMPDateConversion extends AbstractStringPreConversion<String> {
    private final DateTimeFormatter f;

    public IBDataJRMPDateConversion(DateTimeFormatter f) {
      super();
      this.f = requireNonNull(f);
    }

    public Integer toInt(String value, Schema schema, LogicalType type) {
      return (int) f.parse(value, LocalDate::from).toEpochDay();
    }

    public Class<String> getConvertedType() {
      return String.class;
    }

    @Override
    public String getLogicalTypeName() {
      return "date";
    }
  }

  public static class IBDataJRMPTimeConversion extends AbstractStringPreConversion<String> {
    private final DateTimeFormatter f;

    public IBDataJRMPTimeConversion(DateTimeFormatter f) {
      super();
      this.f = requireNonNull(f);
    }

    public Integer toInt(String value, Schema schema, LogicalType type) {
      return (int) TimeUnit.NANOSECONDS.toMillis(f.parse(value, LocalTime::from).toNanoOfDay());
    }

    @Override
    public Class<String> getConvertedType() {
      return String.class;
    }

    @Override
    public String getLogicalTypeName() {
      return "time-millis";
    }

  }

  public static class IBDataJRMPTimestampConversion extends AbstractStringPreConversion<Instant> {
    private final DateTimeFormatter f;

    public IBDataJRMPTimestampConversion(DateTimeFormatter f) {
      super();
      this.f = requireNonNull(f);
    }

    public Long toLong(String value, Schema schema, LogicalType type) {
      TemporalAccessor t = f.parse(value);
      return (t.isSupported(ChronoField.INSTANT_SECONDS)) ? Instant.from(t).toEpochMilli()
          : t.getLong(ChronoField.EPOCH_DAY) * millisInAYear + t.get(ChronoField.MILLI_OF_DAY);
    }

    @Override
    public Class<String> getPreconversionType() {
      return String.class;
    }

    @Override
    public Class<Instant> getConvertedType() {
      return Instant.class;
    }

    @Override
    public String getLogicalTypeName() {
      return "timestamp-millis";
    }

  }
}