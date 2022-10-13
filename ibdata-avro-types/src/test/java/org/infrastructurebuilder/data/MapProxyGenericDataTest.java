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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;

import org.apache.avro.generic.MapProxyGenericData;
import org.apache.avro.generic.MapProxyGenericData.IBDataJRMPDateConversion;
import org.apache.avro.generic.MapProxyGenericData.IBDataJRMPTimeConversion;
import org.apache.avro.generic.MapProxyGenericData.IBDataJRMPTimestampConversion;
import org.infrastructurebuilder.data.Formatters;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapProxyGenericDataTest {
  private static final String FORMAT2 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  private static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

  public final static Logger log = LoggerFactory.getLogger(MapProxyGenericDataTest.class);

  private static final String INST = "2011-12-03T10:15:30.231";
  private final static String INST2 = Instant.now().toString();

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private MapProxyGenericData g;
  private Formatters f;

  @Before
  public void setUp() throws Exception {
    f = new Formatters();
    g = new MapProxyGenericData(f);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testMapProxyGenericData() {
    IBDataJRMPDateConversion k = new MapProxyGenericData.IBDataJRMPDateConversion(f.getDateFormatter());
    assertEquals(String.class, k.getPreconversionType());
    assertEquals(new Integer(22930), k.toInt("10-12-32", null, null));
    IBDataJRMPTimeConversion l = new MapProxyGenericData.IBDataJRMPTimeConversion(f.getTimeFormatter());
    assertEquals(new Integer(81120000), l.toInt("22:32", null, null));
    DateTimeFormatter prf = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    Instant i2 = Instant.parse(INST2);
    assertNotNull(i2);
    log.info("INST2 = '" + INST2 + "'");
    log.info("FMT  =   " + FORMAT2 + "");
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern(FORMAT);
    TemporalAccessor va23 = fmt.parse(INST);
    for (ChronoField fld : ChronoField.values()) {
      if (va23.isSupported(fld)) {
//        log.info("Supports " + fld);
      }
    }
    long ed = va23.getLong(ChronoField.EPOCH_DAY);
    long md = va23.get(ChronoField.MILLI_OF_DAY) ;
    long edM = ed * 60L * 60L * 24 * 1000 + md;
    Instant ii = Instant.ofEpochMilli(edM);
    log.info("Instant back out is " + ii.toString());
    IBDataJRMPTimestampConversion m = new MapProxyGenericData.IBDataJRMPTimestampConversion(
        DateTimeFormatter.ofPattern(FORMAT) );

    assertEquals(Instant.ofEpochMilli(edM), Instant.ofEpochMilli(m.toLong(INST, null, null)));
    assertEquals(String.class, k.getPreconversionType());
    assertEquals(String.class, l.getPreconversionType());
    assertEquals(String.class, m.getPreconversionType());
  }

}
