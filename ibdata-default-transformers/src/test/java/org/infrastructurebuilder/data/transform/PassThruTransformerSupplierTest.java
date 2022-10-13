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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.infrastructurebuilder.data.DefaultIBDataSet;
import org.infrastructurebuilder.data.IBDataSet;
import org.infrastructurebuilder.data.IBDataStream;
import org.infrastructurebuilder.data.IBDataStreamRecordFinalizer;
import org.infrastructurebuilder.data.model.DataSet;
import org.infrastructurebuilder.data.transform.line.StringIBDataStreamRecordFinalizerSupplier;
import org.infrastructurebuilder.util.core.impl.DefaultGAV;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.config.DefaultConfigMapSupplier;
import org.infrastructurebuilder.util.config.TestingPathSupplier;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PassThruTransformerSupplierTest {
  public final static Logger log = LoggerFactory.getLogger(PassThruTransformerSupplierTest.class);
  public final static TestingPathSupplier wps = new TestingPathSupplier();

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    wps.finalize();
  }

  private ConfigMapSupplier cms;
  private PassThruTransformerSupplier p;
  private IBDataStreamRecordFinalizer<?> finalizer;
  private StringIBDataStreamRecordFinalizerSupplier finalizerSupplier;
  private List<IBDataStream> suppliedStreams = Collections.emptyList();
  private IBDataSet ds;
  private DataSet finalData;
  private Date now;
  private DefaultGAV gav;
  private Transformer x1;
  private Transformation x;

  @Before
  public void setUp() throws Exception {
    x1 = new Transformer();
    x1.setId("id");
    x1.setHint(PassThruTransformerSupplier.NAME);
    x1.setFailOnAnyError(true);
    x1.setSources(Collections.emptyList());
    x = new Transformation();
    x.setId("id");
    x.setDescription(DESC);
    x.setName(NAME);
    x.setMetadata(new Xpp3Dom("metadata"));
    x.forceDefaults(GROUP, ARTIFACT, VERSION);
    cms = new DefaultConfigMapSupplier();
    p = new PassThruTransformerSupplier(wps, () -> log);
    finalizerSupplier = new StringIBDataStreamRecordFinalizerSupplier(wps, () -> log);
    finalizer = finalizerSupplier.configure(cms).get();
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
  public void testConfigureConfigMapSupplier() {
    p = p.withFinalizer(finalizer).configure(cms);
    IBDataTransformer t = p.get();
    assertEquals(PassThruTransformerSupplier.NAME, t.getHint());
    assertFalse(t.respondsTo(null));
    IBDataTransformationResult q = t.transform(x1, ds, suppliedStreams, true);
    assertTrue(q.get().isPresent());
  }

}
