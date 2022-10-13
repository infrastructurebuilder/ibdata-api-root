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
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.avro.generic.GenericRecord;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.infrastructurebuilder.util.constants.IBConstants;
import org.infrastructurebuilder.data.model.DataStream;
import org.infrastructurebuilder.util.config.TestingPathSupplier;
import org.infrastructurebuilder.util.files.ThrowingInputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DefaultAvroGenericRecordStreamSupplierTest {
  public static final String CHECKSUM = "3b2c63ccb53069e8b0472ba50053fcae7d1cc84ef774ff2b01c8a0658637901b7d91e71534243b5d29ee246e925efb985b4dbd7330ab1ab251d1e1b8848b9c49";

  private final static TestingPathSupplier wps = new TestingPathSupplier();

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    wps.finalize();
  }

  private DefaultAvroGenericRecordStreamSupplier d;
  private DefaultIBDataStream identifier;
  private DataStream id;

  @Before
  public void setUp() throws Exception {
    id = new DataStream();
    id.setUuid(UUID.randomUUID().toString());
    id.setCreationDate(new Date());
    id.setSha512(CHECKSUM);
    id.setMetadata(new Xpp3Dom("metadata"));
    identifier = new DefaultIBDataStream(id, wps.getTestClasses().resolve("ba.avro"));
    d = new DefaultAvroGenericRecordStreamSupplier();
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testFrom() {
    Optional<Stream<GenericRecord>> q = d.from(identifier);
    assertTrue(q.isPresent());
    List<GenericRecord> w = q.get().collect(Collectors.toList());
    assertEquals(5000L, w.size());
  }

  @Test
  public void testGetRespondTypes() {
    assertTrue(d.getRespondTypes().contains(IBConstants.AVRO_BINARY));
    assertEquals(1, d.getRespondTypes().size());
  }

  @Test(expected = IBDataException.class)
  public void testThrownException() {
    ThrowingInputStream ins = new ThrowingInputStream(IOException.class);
    DefaultAvroGenericRecordStreamSupplier.genericStreamFromInputStream.apply(ins);
  }

}
