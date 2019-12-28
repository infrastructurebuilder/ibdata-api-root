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

import static org.infrastructurebuilder.IBConstants.APPLICATION_OCTET_STREAM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.infrastructurebuilder.util.BasicCredentials;
import org.infrastructurebuilder.util.artifacts.Checksum;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.TestingPathSupplier;
import org.infrastructurebuilder.util.files.DefaultIBChecksumPathType;
import org.infrastructurebuilder.util.files.IBChecksumPathType;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public class IBDataSourceTest {
  public final static Logger log = LoggerFactory.getLogger(IBDataSourceTest.class);
  public static final TestingPathSupplier wps = new TestingPathSupplier();

  private IBDataSource i;

  @Before
  public void setUp() throws Exception {
    Path p = wps.getTestClasses().resolve("test.jar");
    i = new IBDataSource() {

      @Override
      public Logger getLog() {
        return log;
      }

      @Override
      public List<IBChecksumPathType> get() {
        return Arrays.asList(DefaultIBChecksumPathType.from(p, new Checksum(p), APPLICATION_OCTET_STREAM));
      }

      @Override
      public String getSourceURL() {
        return null;
      }

      @Override
      public Optional<String> getName() {
        return Optional.empty();
      }

      @Override
      public Optional<Document> getMetadata() {
        return Optional.empty();
      }

      @Override
      public String getId() {
        return null;
      }

      @Override
      public Optional<String> getDescription() {
        return Optional.empty();
      }

      @Override
      public Optional<BasicCredentials> getCredentials() {
        return Optional.empty();
      }

      @Override
      public Optional<Checksum> getChecksum() {
        return Optional.empty();
      }

      @Override
      public IBDataSource configure(ConfigMap config) {
        return this;
      }
      @Override
      public Optional<String> getMimeType() {
        return Optional.of(APPLICATION_OCTET_STREAM);
      }
    };
  }

  @Test
  public void testIsExpand() {
    assertFalse(i.isExpandArchives());
  }
  @Test
  public void testGetMimeType() {
    assertEquals(APPLICATION_OCTET_STREAM, i.getMimeType().get());
  }

}
