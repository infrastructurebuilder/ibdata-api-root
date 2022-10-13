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

import static org.infrastructurebuilder.data.IBDataAvroUtils.avroSchemaFromString;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericData.Record;
import org.apache.avro.generic.GenericRecord;
import org.infrastructurebuilder.IBException;
import org.infrastructurebuilder.data.transform.BA;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.TestingPathSupplier;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class IBDataAvroUtilsTest {
  private final static TestingPathSupplier wps = new TestingPathSupplier();
  private Schema schema;
  private Record r;
  private Map<String, Field> fields;
  private Schema schema2;
  private Map<String, Field> fields2;

  @Before
  public void setUp() throws Exception {
    Path p = wps.getTestClasses().resolve("ba.avsc");
    schema = avroSchemaFromString.apply(p.toAbsolutePath().toString());
    schema2 = BA.SCHEMA$;
    r = new GenericData.Record(schema);
    fields = schema.getFields().stream().collect(Collectors.toMap(Field::name, Function.identity()));
    fields2 = schema2.getFields().stream().collect(Collectors.toMap(Field::name, Function.identity()));
  }

  @AfterClass
  public static void afterClass() {
    wps.finalize();
  }

  @Test
  public void testFromSchemaAndPathAndTranslator() throws IOException {

    Path targetPath = wps.get();
    DataFileWriter<GenericRecord> d = IBDataAvroUtils.fromSchemaAndPathAndTranslator(
        targetPath.resolve(UUID.randomUUID().toString() + ".avro"), schema, Optional.empty());
    assertNotNull(d);
    d.close();
    d = IBDataAvroUtils.fromSchemaAndPathAndTranslator(targetPath.resolve(UUID.randomUUID().toString() + ".avro"),
        schema, Optional.of(new GenericData()));
    assertNotNull(d);
    d.close();

  }

  @Test(expected = IBException.class)
  public void testNotObvioyslyBrokenURLZip() {
    avroSchemaFromString.apply("zip:file:/nope.jar");
  }

  @Test(expected = IBDataException.class)
  public void testNotObvioyslyBrokenURLHttp() {
    avroSchemaFromString.apply("http://www.example.com");
  }

  @Test(expected = IBDataException.class)
  public void testNotObvioyslyBrokenURLHttps() {
    avroSchemaFromString.apply("https://www.example.com");
  }

  @Test(expected = IBException.class)
  public void testNotObvioyslyBrokenURLJar() {
    avroSchemaFromString.apply("jar:file:/nope.zip");
  }

  @Test(expected = IBDataException.class)
  public void testNotObvioyslyBrokenURLFile() {
    avroSchemaFromString.apply("file:/nopw.www.example.com");
  }

  @Test(expected = IBDataException.class)
  public void testNulled() {
    avroSchemaFromString.apply(null);
  }

  @Test(expected = IBDataException.class)
  public void testBrokenURL() {
    avroSchemaFromString.apply("noep:@3");
  }

  @Test(expected = IBDataException.class)
  public void testFromMapAndWpNulled() {
    IBDataAvroUtils.fromMapAndWP.apply(wps.getTestClasses(), new ConfigMap());
  }
}
