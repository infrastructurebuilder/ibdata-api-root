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
package org.infrastructurebuilder.data.archive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.infrastructurebuilder.data.model.DataSchema;
import org.infrastructurebuilder.util.config.TestingPathSupplier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ModelloReflectionReaderTest {
  private static TestingPathSupplier wps = new TestingPathSupplier();

  private ModelloReflectionReader<DataSchema> dsr;

  private Path path;

  @Before
  public void setUp() throws Exception {
    path = wps.getTestClasses().resolve("TestDataSchema.xml");
    dsr = new ModelloReflectionReader<>(DataSchema.class);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testReadFromModel() throws XmlPullParserException, IOException {
    DataSchema ds = null;
    try (Reader reader = Files.newBufferedReader(path)) {
      ds = dsr.readFromModel(reader);
    }
    assertNotNull(ds);
    assertEquals("description", ds.getDescription().get());
  }
}
