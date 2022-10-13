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
package org.infrastructurebuilder.data.ingest;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.configuration.xml.XmlPlexusConfiguration;
import org.infrastructurebuilder.data.IBDataSourceSupplier;
import org.infrastructurebuilder.data.IBDataWorkingPathSupplier;
import org.infrastructurebuilder.data.ingest.base.DefaultIBDataSetIdentifier;
import org.infrastructurebuilder.data.ingest.base.DefaultIBDataStreamIdentifierConfigBean;
import org.infrastructurebuilder.maven.util.config.MavenConfigMapSupplier;
import org.infrastructurebuilder.util.core.TestingPathSupplier;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IBDataIngestMavenComponentTest {

  public final static Logger log = LoggerFactory.getLogger(IBDataIngestMavenComponentTest.class);
  private TestingPathSupplier wps;
  private Properties properties;
  private MavenProject mp;
  private Path target;
  private Model mm;
  private MavenConfigMapSupplier cms;
  private Ingestion config;
  private IBDataIngester ingester;
  private Map<String, IBDataSourceSupplier<?>> dataSourceSuppliers;
  private DefaultIBDataSetIdentifier ds, dsMulti;
  private ArrayList<DefaultIBDataStreamIdentifierConfigBean> streams;
  private DefaultIBDataStreamIdentifierConfigBean ds1;
  private String sourceURL;
  private DefaultIBDataStreamIdentifierConfigBean ds2, ds3, ds4;
  private IBDataWorkingPathSupplier lbps;

  @Before
  public void setUp() throws Exception {
    wps = new TestingPathSupplier();
    target = wps.getRoot();
    sourceURL = wps.getTestClasses().resolve("testfile.txt").toUri().toURL().toExternalForm();
    properties = new Properties();
    properties.setProperty("a", "b");
    properties.setProperty("c", "d");
    properties.setProperty("user.home", "override");
    mm = new Model();
    mm.setProperties(properties);
    final Build b = new Build();
    b.setDirectory(target.toString());
    mp = new MavenProject(mm);
    mp.setBuild(b);
    Path workingPath = wps.get();
    lbps = new IBDataWorkingPathSupplier();
    lbps.setPath(workingPath);
    cms = new MavenConfigMapSupplier(mp, null, null);
    //    dic = new IBDataIngestMavenComponent(lbps, log, new DefaultTypeToExtensionMapper(), cms);
    ds = new DefaultIBDataSetIdentifier();
    ds.setMetadata(new XmlPlexusConfiguration("metadata"));
    ds.setDescription("Descc");
    ds.setName("thename");

    dsMulti = new DefaultIBDataSetIdentifier();
    dsMulti.setMetadata(new XmlPlexusConfiguration("metadata"));
    dsMulti.setDescription("Multi");
    dsMulti.setName("theMulti");

    ds1 = new DefaultIBDataStreamIdentifierConfigBean();
    ds1.setId("temp1");
    ds1.setSha512(
        "5e787c500552b5c75adb60b0cfd1ba3db2083d5a93d4d00480e16bff3d8918790e94032da47756b522b6cd458943f6a4b7f0c3dbb9ff035e7221f3a14d22e1eb");
    //    ds1.set_metadata(IBDataSetIdentifier.emptyDocumentSupplier.get());
    ds1.setDescription("S1 desc");
    ds1.setName("somename");
    ds1.setMimeType("text/plain");
    ds1.setUrl(sourceURL);
    streams = new ArrayList<>(Arrays.asList(ds1));

    ds2 = new DefaultIBDataStreamIdentifierConfigBean();
    ds2.setId("temp2");
    ds2.setSha512(
        "b4ab0dbcf486d0c9fa98d9f1e3f19d392201150db79196f8ec54d4d05838038d0dea917f17f09e3a302f80d39ca36cca379bf0d002c97c479694e20e0db9a50d");
    //    ds2.set_metadata(IBDataSetIdentifier.emptyDocumentSupplier.get());
    ds2.setDescription("S2 desc");
    ds2.setName("somename");
    ds2.setMimeType("application/pdf");
    ds2.setUrl("https://file-examples.com/wp-content/uploads/2017/02/file-sample_100kB.doc");
    ds3 = new DefaultIBDataStreamIdentifierConfigBean();
    ds3.setId("temp3");
    ds3.setSha512(
        "09253eb87d097bdaa39f98cbbea3e6d83ee4641bca76c32c7eb1add17e9cb3117adb412d2e04ab251cca1fb19afa8b631d1e774b5dc8ae727f753fe2ffb5f288");
    //    ds3.set_metadata(IBDataSetIdentifier.emptyDocumentSupplier.get());
    ds3.setDescription("S3 desc");
    ds3.setName("somename");
    ds3.setMimeType("application/pdf");
    ds3.setUrl("https://file-examples.com/wp-content/uploads/2017/10/file-sample_150kB.pdf");
    ds4 = new DefaultIBDataStreamIdentifierConfigBean();
    ds4.setId("temp4");
    ds4.setSha512(
        "5e33512e482ac4512cc8ffc3a579ee762c48f60dde3cea614b253b70fd619f129c6f0df48c8599f442b813c51e11a87dd452cf4e5bf0a8aefa195ae414cdaa41");
    //    ds4.set_metadata(IBDataSetIdentifier.emptyDocumentSupplier.get());
    ds4.setDescription("S4 desc");
    ds4.setName("somename");
    ds4.setMimeType("application/pdf");
    ds4.setUrl("https://file-examples.com/wp-content/uploads/2017/02/file_example_XLSX_5000.xlsx");
    config = new Ingestion();
  }

  @Ignore
  @Test
  public void testIngestFile() {
    ds.setStreams(streams);
    config.setDataSet(ds);
    DefaultIBDataIngesterSupplier s = new DefaultIBDataIngesterSupplier(wps, () -> log);
    ingester = s.get();
    //    DefaultIBDataEngine e = new DefaultIBDataEngine(Collections.emptyMap());
    dataSourceSuppliers = Collections.emptyMap();
    //    IBChecksumPathType p = dic.ingest(config, ingester);
    //    assertNotNull(p);
  }

  @Ignore
  @Test
  public void testIngestDownload() {
    ds.setStreams(new ArrayList<>(Arrays.asList(ds3)));
    config.setDataSet(ds);
    DefaultIBDataIngesterSupplier s = new DefaultIBDataIngesterSupplier(wps, () -> log);
    ingester = s.get();
    dataSourceSuppliers = Collections.emptyMap();
  }

  @Ignore
  @Test
  public void testIngestMultiDownload() throws IOException {
    dsMulti.setStreams(Arrays.asList(ds4, ds3));
    config.setDataSet(dsMulti);
    DefaultIBDataIngesterSupplier s = new DefaultIBDataIngesterSupplier(wps, () -> log);
    ingester = s.get();
    //    DefaultIBDataEngine e = new DefaultIBDataEngine(Collections.emptyMap());
    dataSourceSuppliers = Collections.emptyMap();

    //    IBChecksumPathType qq = dic.ingest(config, ingester);
    //
    //    assertNotNull(qq);
    //    assertEquals(IBDataIngester.APPLICATION_IBDATA_ARCHIVE, qq.getType());
    //    assertEquals(3,Files.list(qq.getPath()).collect(Collectors.toSet()).size());
  }
}
