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

import static java.util.stream.Collectors.toList;
import static org.infrastructurebuilder.data.IBDataConstants.IBDATA;
import static org.infrastructurebuilder.data.IBMetadataUtilsTest.TEST_INPUT_0_11_XML;
import static org.infrastructurebuilder.data.IBMetadataUtilsTest.TEST_INPUT_0_11_XML_CHECKSUM;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.infrastructurebuilder.IBConstants;
import org.infrastructurebuilder.data.model.DataSet;
import org.infrastructurebuilder.data.model.DataStream;
import org.infrastructurebuilder.util.artifacts.GAV;
import org.infrastructurebuilder.util.artifacts.impl.DefaultGAV;
import org.infrastructurebuilder.util.config.TestingPathSupplier;
import org.infrastructurebuilder.util.files.TypeToExtensionMapper;
import org.junit.Before;

public class AbstractModelTest {

  public static final String O11_2 = "183d3030-6dae-4f33-acde-79eacbaa8c2d.txt";
  public static final String O11_1 = "0dfb7bc9-73aa-4f7e-b735-cbccfa052733.txt";
  public static final String SOURCE_URL = "http://www.example.com";
  public static final String VERSION = "1.0.0";
  public static final String ARTIFACT = "Y";
  public static final String GROUP = "X";
  public static final String DESC = "desc";
  public static final String NAME = "name";
  public static final String METADATA_CHECKSUM = "fa2ab560f631546a2f5d1aa6f4c1ec9d00d988010bf9b80d1a195f1537b8e15ae3cf94bdb52c1774d969ab0b6452b91f03531b062aac410deec2c75a2d580ac2";
  //  protected static final String STREAM_METADATA_CHECKSUM = "d709f80dc9d4d9fda60631dfa8390a746d4816b5076884ae7bb1fdd5d21489773a44eb0cef038f4b7c27b6c20ba2a0e4bf37ccbab265d47c02806aeda1a03777";
  public static final String STREAM_CHECKSUM =TEST_INPUT_0_11_XML_CHECKSUM ;
  public static final String STREAM_ID="98a51489-93fd-372d-86c2-4b7d0268608a";
  public static final String STREAM_METADATA_CHECKSUM = "3b2c63ccb53069e8b0472ba50053fcae7d1cc84ef774ff2b01c8a0658637901b7d91e71534243b5d29ee246e925efb985b4dbd7330ab1ab251d1e1b8848b9c49";

  protected final TestingPathSupplier wps = new TestingPathSupplier();
  protected DataSet finalData;
  protected Date now;
  protected DataStream stream;
  protected GAV gav;
  protected Path full_0_11;
  protected Path ibd;
  protected List<Path> o11Paths;

  public final static class FakeTypeToExtensionMapper implements TypeToExtensionMapper {
    @Override
    public String getExtensionForType(String type) {
      return "bin";
    }
  }



  public final static class FakeIBDataStreamSupplier extends DataStream implements Supplier<IBDataStream> {

    private IBDataStream stream;

    public FakeIBDataStreamSupplier(DataStream s, Path dataStream) {
      super(s);
      stream = new FakeIBDataStream(s, dataStream);
    }

    @Override
    public IBDataStream get() {
      return stream;
    }

    @Override
    public UUID getId() {
      return stream.getId();
    }

  }

  public final static class FakeIBDataSet extends DataSet implements IBDataSet {

    private final List<FakeIBDataStreamSupplier> suppliers = new ArrayList<>();
    private final List<IBSchema> schemas = new ArrayList<>();

    public FakeIBDataSet(DataSet set, List<Path> paths) {
      super(set);
      for (int i = 0; i < set.getStreams().size(); ++i) {
        suppliers.add(new FakeIBDataStreamSupplier(set.getStreams().get(i), paths.get(i)));
      }
    }

    @Override
    public List<Supplier<IBDataStream>> getStreamSuppliers() {
      return suppliers.stream().collect(toList());
    }

    @Override
    public List<IBSchema> getSchemaSuppliers() {
      // TODO Auto-generated method stub
      return schemas;
    }
  }

  @Before
  public void setUp() throws Exception {
    now = new Date(1570968733117L);
    finalData = new DataSet();
    finalData.setGroupId(GROUP);
    finalData.setArtifactId(ARTIFACT);
    finalData.setVersion(VERSION);
    gav = new DefaultGAV(GROUP, ARTIFACT, VERSION);
    finalData.setModelVersion("1.0");
    finalData.setDataSetDescription(DESC);
    finalData.setDataSetName(NAME);



    stream = new DataStream();
    stream.setCreationDate(now);
    stream.setDataStreamDescription(DESC);
    stream.setDataStreamName(NAME);
    Xpp3Dom metadata = new Xpp3Dom("metadata");
    try (InputStream is = getClass().getResourceAsStream(TEST_INPUT_0_11_XML)) {
      metadata = Xpp3DomBuilder.build(is, "utf-8");
    }
    stream.setMetadata(metadata);
    stream.setMimeType(IBConstants.APPLICATION_OCTET_STREAM);
    stream.setSha512(TEST_INPUT_0_11_XML_CHECKSUM);
    stream.setSourceURL(SOURCE_URL);
    Path tStream = wps.get().resolve(UUID.randomUUID().toString()).toAbsolutePath();
    try (InputStream is = getClass().getResourceAsStream(TEST_INPUT_0_11_XML)) {
      Files.copy(is, tStream);
    }
    stream.setPath(tStream.toString());
    stream.setReferencedSchema(UUID.randomUUID().toString());
    tStream.toFile().deleteOnExit();
    stream.setUuid(stream.getChecksum().asUUID().get().toString());

    full_0_11 = wps.get();
    ibd = full_0_11.resolve(IBDATA);
    Path ibd_o11_1 = full_0_11.resolve(O11_1);
    Path ibd_o11_2 = full_0_11.resolve(O11_2);
    Files.createDirectories(ibd);
    Path t = wps.getTestClasses();
    Path o11_1 = t.resolve(O11_1);
    Path o11_2 = t.resolve(O11_2);
    Files.copy(o11_1, full_0_11.resolve(O11_1));
    Files.copy(o11_2, full_0_11.resolve(O11_2));
    o11Paths = Arrays.asList(ibd_o11_1.toAbsolutePath(), ibd_o11_2.toAbsolutePath());
  }

}