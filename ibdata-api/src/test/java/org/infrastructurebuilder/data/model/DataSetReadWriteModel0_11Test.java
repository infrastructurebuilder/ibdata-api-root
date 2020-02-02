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
package org.infrastructurebuilder.data.model;

import static org.infrastructurebuilder.data.IBMetadataUtilsTest.TEST_INPUT_0_11_XML;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.infrastructurebuilder.data.IBDataEngine;
import org.infrastructurebuilder.data.IBDataSet;
import org.infrastructurebuilder.data.IBDataStream;
import org.infrastructurebuilder.data.IBSchema;
import org.infrastructurebuilder.data.Metadata;
import org.infrastructurebuilder.data.model.io.xpp3.IBDataSourceModelXpp3Reader;
import org.infrastructurebuilder.data.model.io.xpp3.IBDataSourceModelXpp3Writer;
import org.infrastructurebuilder.util.artifacts.Checksum;
import org.junit.Before;
import org.junit.Test;

public class DataSetReadWriteModel0_11Test {

  private DataStream ds;
  private DataSet set;
  private Metadata d;

  @Before
  public void setUp() throws Exception {

    Map<String, String> meta = new HashMap<>();

    //    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    //    DocumentBuilder builder = factory.newDocumentBuilder();
    //    d = builder.newDocument();
    d = new Metadata();
    meta.entrySet().forEach(entry -> {
      Metadata d2 = new Metadata(entry.getKey());
      d2.setValue(entry.getValue());
      d.addChild(d2);
      //      Element e = d.createElement(entry.getKey());
      //      e.setTextContent(Objects.requireNonNull(entry.getValue(), "setText value for " + entry.getKey() + " is null"));
      //      d.appendChild(e);
    });
    set = new DataSet();
    set.setCreationDate(new Date());
    set.setDescription("setDescription");
    set.setModelEncoding("UTF-8");
    IBDataEngine ibDataEngine = new IBDataEngine() {

      @Override
      public List<UUID> getAvailableDataStreamIds() {
        return Collections.emptyList();
      }

      @Override
      public Optional<IBDataSet> fetchDataSetById(UUID id) {
        return Optional.empty();
      }

      @Override
      public Optional<IBDataStream> fetchDataStreamById(UUID id) {
        return Optional.empty();
      }

      @Override
      public Optional<IBDataStream> fetchDataStreamByMetadataPatternMatcher(Map<String, Pattern> patternMap) {
        return Optional.empty();
      }

      @Override
      public int prepopulate() {
        return 0;
      }

      @Override
      public Optional<IBSchema> fetchSchemaById(UUID id) {
        return Optional.empty();
      }

      @Override
      public void reset() {

      }

    };
    ibDataEngine.fetchDataStreamByMetadataPatternMatcherFromStrings(Collections.emptyMap());
    set.setModelVersion(ibDataEngine.getEngineAPIVersion().toString());
    set.setUuid(UUID.randomUUID().toString());
    ds = new DataStream();
    ds.setDescription("description 1");
    ds.setCreationDate(new Date());
    ds.setName("one");
    ds.setSha512(new Checksum().toString());
    ds.setUrl("https://www.google.com");
    ds.setUuid(UUID.randomUUID().toString());
    ds.setMetadata(d);
    set.addStream(ds);
    ds.setCreationDate(new Date());
    ds.setName("two");
    ds.setDescription("description two");
    set.addStream(ds);

    StringWriter sw = new StringWriter();
    new IBDataSourceModelXpp3Writer().write(sw, set);

    String k = sw.toString();
    String v = k;
  }

  @Test
  public void testReaderEx() throws IOException, XmlPullParserException {
    IBDataSourceModelXpp3Reader reader;

    reader = new IBDataSourceModelXpp3Reader();
    try (InputStream in = getClass().getResourceAsStream(TEST_INPUT_0_11_XML)) {
      DataSet read = reader.read(in, true);
      assertEquals("310dc0e2-109d-4237-9729-e266176e1c7a", read.getUuid().toString());
    }
  }

  @Test
  public void testReader() throws IOException, XmlPullParserException {
    IBDataSourceModelXpp3Reader reader = new IBDataSourceModelXpp3Reader();
    try (InputStream in = getClass().getResourceAsStream(TEST_INPUT_0_11_XML)) {
      DataSet read = reader.read(in, true);
      assertEquals("310dc0e2-109d-4237-9729-e266176e1c7a", read.getUuid().toString());
    }
  }

}
