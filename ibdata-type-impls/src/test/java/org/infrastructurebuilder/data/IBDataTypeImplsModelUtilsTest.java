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

import static org.infrastructurebuilder.data.IBDataConstants.IBDATA;
import static org.infrastructurebuilder.data.IBDataConstants.IBDATASET_XML;
import static org.infrastructurebuilder.data.IBDataConstants.IBDATA_IBDATASET_XML;
import static org.infrastructurebuilder.data.IBDataTypeImplsModelUtils.getZipFSProvider;
import static org.infrastructurebuilder.data.IBDataTypeImplsModelUtils.mapDataSetToDefaultIBDataSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;

import org.infrastructurebuilder.util.core.IBUtils;
import org.infrastructurebuilder.util.core.TestingPathSupplier;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class IBDataTypeImplsModelUtilsTest {

  private final static TestingPathSupplier wps = new TestingPathSupplier();
  private Path testJar;
  private Path testDir;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
    testJar = wps.getTestClasses().resolve("test.jar");
    testDir = wps.getTestClasses().resolve("test2").toAbsolutePath();
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testMapDataSetToDefaultIBDataSetFromJar() throws MalformedURLException {
    URL url = IBUtils
        .translateToWorkableArchiveURL("jar:" + testJar.toUri().toURL().toExternalForm() + "!" + IBDATA_IBDATASET_XML);
    Optional<DefaultIBDataSet> q = mapDataSetToDefaultIBDataSet.apply(url);
    assertTrue(q.isPresent());
    assertEquals("99139ebc-4c01-3c93-89c1-2219c7e4ebf6", q.get().getId().toString());
  }

  @Test
  public void testMapDataSetToDefaultIBDataSetFromDir() throws MalformedURLException {
    Path m = wps.getTestClasses();
    Path n = m.resolve("test2");
    Path p = testDir.resolve(IBDATA).resolve(IBDATASET_XML);
    URL testDirURL = testDir.toUri().toURL();
    URL url = p.toUri().toURL();
    Optional<DefaultIBDataSet> q = mapDataSetToDefaultIBDataSet.apply(url);
    assertTrue(q.isPresent());
    assertEquals("99139ebc-4c01-3c93-89c1-2219c7e4ebf6", q.get().getId().toString());
  }

  @Test
  public void testGetZipFSProvider() {
    assertTrue(getZipFSProvider().isPresent());
  }

}
