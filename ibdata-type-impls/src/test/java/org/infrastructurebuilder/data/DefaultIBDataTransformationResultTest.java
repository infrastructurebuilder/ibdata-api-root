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

import java.nio.file.Path;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.infrastructurebuilder.data.model.DataSet;
import org.infrastructurebuilder.util.core.TestingPathSupplier;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DefaultIBDataTransformationResultTest {

  private static TestingPathSupplier wps;
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    wps = new TestingPathSupplier();
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    wps.finalize();
  }

  private Path wp;
  private IBDataSet createdDataSet;
  private DefaultIBDataTransformationResult d;
  private DataSet ds;

  @Before
  public void setUp() throws Exception {
    wp = wps.get();
    ds = new DataSet();
    ds.setUuid(UUID.randomUUID().toString());
    ds.setCreationDate(new Date());
    ds.setDataSetName("name");
    ds.setDataSetDescription("desc");
    ds.setMetadata(new Xpp3Dom("metadata"));
    ds.setGroupId("A");
    ds.setArtifactId("B");
    ds.setVersion("1.0.0");
    createdDataSet = new DefaultIBDataSet(ds);
    d = new DefaultIBDataTransformationResult(createdDataSet, wp);
  }

  @After
  public void tearDown() throws Exception {
    wps.finalize();
  }

//  @Test(expected=IBDataException.class)
//  public final void alwaysfailIfYouGetADataSetFrmDefaultIBDataSet() {
//    createdDataSet.asDataSet();
//  }

  @Test
  public void testGetErrors() {
    assertEquals(Collections.emptyList(), d.getErrors());
  }

  @Test
  public void testGet() {
    assertEquals(createdDataSet, d.get().get());
  }

  @Test
  public void testGetWorkingPathSupplier() {
    assertEquals(wp, d.getWorkingPathSupplier().get());
  }

}
