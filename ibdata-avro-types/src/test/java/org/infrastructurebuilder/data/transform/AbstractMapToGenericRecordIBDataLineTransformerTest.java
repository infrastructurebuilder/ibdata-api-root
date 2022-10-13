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
package org.infrastructurebuilder.data.transform;

import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Parser;
import org.apache.avro.generic.GenericRecord;
import org.infrastructurebuilder.data.transform.line.AbstractMapToAvroGenericRecordIBDataLineTransformer;
import org.infrastructurebuilder.data.transform.line.DefaultMapToGenericRecordIBDataLineTransformerSupplier;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.TestingPathSupplier;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractMapToGenericRecordIBDataLineTransformerTest {
  static final String BA_AVSC = "ba.avsc";

  static final String LOAD1_PROPERTIES = "load1.properties";

  public final static Logger log = LoggerFactory
      .getLogger(AbstractMapToGenericRecordIBDataLineTransformerTest.class);

  protected static final String DATE_PATTERN = "yyyy-MM-dd";
  protected static final String TIME_PATTERN = "HH:mm";
  protected static final String TS_PATTERN = DateTimeFormatter.ISO_INSTANT.toString();

  private AbstractMapToAvroGenericRecordIBDataLineTransformer test;
  private Schema s;
  private Map<String, Object> testData = new HashMap<>();
  private TestingPathSupplier wps = new TestingPathSupplier();
  private Path workingPath;

  @Before
  public void setUp() throws Throwable {
    Path schemaP = wps.getTestClasses().resolve(BA_AVSC);
    testData.put("first_name", "c1");
    testData.put("last_name", "c2");
    testData.put("gender", "f");
    testData.put("country", "usa");
    testData.put("age", "323");
    testData.put("date_of_birth", "11-10-99");
    testData.put("id", "1");
    testData.put("index", "1");
    testData.put("A", "B");
    Object[] a = Arrays.asList("A", "B", 1, 2, 3).toArray(new Object[0]);
    Properties p1 = new Properties();
    try (InputStream in = getClass().getResourceAsStream("/" + LOAD1_PROPERTIES)) {
      p1.load(in);
    }
    p1.setProperty(DefaultMapToGenericRecordIBDataLineTransformerSupplier.SCHEMA_PARAM, schemaP.toAbsolutePath().toString());

    try (InputStream ins = getClass().getResourceAsStream("/" + BA_AVSC)) {
      Parser p = new Schema.Parser();
      s = p.parse(ins);
    }

    workingPath = wps.get();

    test = new DefaultMapToGenericRecordIBDataLineTransformerSupplier.DefaultMapSSToGenericRecordIBDataLineTransformer(
        workingPath, new ConfigMap(p1), log);
  }

  @Test
  public void test() {
    GenericRecord r = (GenericRecord) test.apply(testData);
    assertNotNull(r);
  }


}
