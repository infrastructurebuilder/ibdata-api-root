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

import static org.infrastructurebuilder.data.AbstractModelTest.ARTIFACT;
import static org.infrastructurebuilder.data.AbstractModelTest.DESC;
import static org.infrastructurebuilder.data.AbstractModelTest.GROUP;
import static org.infrastructurebuilder.data.AbstractModelTest.NAME;
import static org.infrastructurebuilder.data.AbstractModelTest.VERSION;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.infrastructurebuilder.data.DefaultIBDataSet;
import org.infrastructurebuilder.data.IBDataConstants;
import org.infrastructurebuilder.data.IBDataSet;
import org.infrastructurebuilder.data.IBDataSetFinalizer;
import org.infrastructurebuilder.data.IBDataSetFinalizerSupplier;
import org.infrastructurebuilder.data.model.DataSet;
import org.infrastructurebuilder.data.util.files.DefaultTypeToExtensionMapper;
import org.infrastructurebuilder.util.core.impl.DefaultGAV;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.config.ConfigurableSupplier;
import org.infrastructurebuilder.util.config.DefaultConfigMapSupplier;
import org.infrastructurebuilder.util.config.TestingPathSupplier;
import org.infrastructurebuilder.util.files.IBChecksumPathType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultIBDataSetTransformationFinalizerSupplierTest {
  public final static Logger log = LoggerFactory.getLogger(DefaultIBDataSetTransformationFinalizerSupplierTest.class);
  public final static TestingPathSupplier wps = new TestingPathSupplier();

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    wps.finalize();
  }

  private ConfigMapSupplier cms;
  private IBDataSetFinalizerSupplier<?> fs;
  private Transformation x;
  private IBDataSet dsi;
  private DataSet finalData;
  private Date now;
  private DefaultGAV gav;
  private DefaultIBDataSet ds;

  @Before
  public void setUp() throws Exception {
    x = new Transformation();
    x.setId("id");
    x.setDescription(DESC);
    x.setName(NAME);
    x.setMetadata(new Xpp3Dom("metadata"));
    x.forceDefaults(GROUP, ARTIFACT, VERSION);

    cms = new DefaultConfigMapSupplier();
    fs = new DefaultIBDataSetTransformationFinalizerSupplier(wps, () -> log, new DefaultTypeToExtensionMapper());
    finalData = new DataSet();
    finalData.setUuid(UUID.randomUUID().toString());
    now = new Date(1570968733117L);
    finalData.setCreationDate(now);
    finalData.setGroupId(GROUP);
    finalData.setArtifactId(ARTIFACT);
    finalData.setVersion(VERSION);
    finalData.setMetadata(new Xpp3Dom("metadata"));
    gav = new DefaultGAV(GROUP, ARTIFACT, VERSION);
    finalData.setModelVersion("1.0");
    finalData.setDataSetDescription(DESC);
    finalData.setDataSetName(NAME);

    ds = new DefaultIBDataSet(finalData);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetAndFinalize() throws IOException {
    ConfigurableSupplier<?, ConfigMapSupplier> p2 = fs.configure(cms);
    IBDataSetFinalizer<Transformation> p = (IBDataSetFinalizer<Transformation>) p2.get();
    assertNotNull(p);

    IBChecksumPathType g = p.finalize(ds, x, Collections.emptyList(), Optional.empty());
    assertTrue(Files.exists(g.getPath().resolve(IBDataConstants.IBDATA).resolve(IBDataConstants.IBDATASET_XML)));
  }

}
