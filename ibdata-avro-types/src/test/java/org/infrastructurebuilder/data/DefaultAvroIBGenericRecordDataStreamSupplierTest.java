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

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.infrastructurebuilder.data.model.DataStream;
import org.infrastructurebuilder.util.config.TestingPathSupplier;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DefaultAvroIBGenericRecordDataStreamSupplierTest {
  public static final String CHECKSUM = "3b2c63ccb53069e8b0472ba50053fcae7d1cc84ef774ff2b01c8a0658637901b7d91e71534243b5d29ee246e925efb985b4dbd7330ab1ab251d1e1b8848b9c49";

  public static final String LOAD1 = "ba.avro";
  public final static TestingPathSupplier wps = new TestingPathSupplier();

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    wps.finalize();
  }

  private DefaultAvroIBGenericRecordDataStreamSupplier q;
  private Path targetPath;
  private IBDataStream stream;
  private boolean parallel;
  private Schema schema;
  private InputStream ins;
  private DataStream id;

  @Before
  public void setUp() throws Exception {
    targetPath = wps.get();
    ins = getClass().getResourceAsStream(LOAD1);
    id = new DataStream();
    id.setUuid(UUID.randomUUID().toString());
    id.setCreationDate(new Date());
    id.setSha512(CHECKSUM);
    id.setMetadata(new Xpp3Dom("metadata"));
    stream = new DefaultIBDataStream(id, wps.getTestClasses().resolve(LOAD1));
    schema = IBDataAvroUtils.avroSchemaFromString.apply(wps.getTestClasses().resolve("ba.avsc").toAbsolutePath().toString());
    q = new DefaultAvroIBGenericRecordDataStreamSupplier(targetPath, stream, parallel, schema);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGet() {
    Stream<GenericRecord> p = q.get();
    List<GenericRecord> l2 = p.collect(Collectors.toList());
    assertEquals(5000, l2.size());
  }

}
