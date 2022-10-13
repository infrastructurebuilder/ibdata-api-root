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
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.infrastructurebuilder.util.core.Checksum;
import org.infrastructurebuilder.util.config.TestingPathSupplier;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DefaultSeekableInputFromInputStreamTest {

  private static final String CHECKSUM = "c369ba6d026298ec8b5d0915fe35582b89882b11b48a04d8ffff586ec78fa391291deb8b72dad8dbd84b07ef4d87aba332b0b08572b6113344958805226d7b4d";
  private final static TestingPathSupplier wps = new TestingPathSupplier();

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    wps.finalize();
  }

  private Path p;

  @Before
  public void setUp() throws Exception {
    p = wps.getTestClasses().resolve("ba.csv");
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void test() throws IOException {
    DefaultSeekableInputFromInputStream q = null;
    try (InputStream ins = Files.newInputStream(p)) {
      q = new DefaultSeekableInputFromInputStream(wps.get(), ins);
      assertEquals(284042, q.length());
      q.seek(0L);
      assertEquals(CHECKSUM, q.asChecksum().toString());
      assertEquals(new Checksum(p), new Checksum(q.getSourcePath()));

    } finally {
      q.close();

    }
  }

}
