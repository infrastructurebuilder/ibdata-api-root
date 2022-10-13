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

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.infrastructurebuilder.util.constants.IBConstants;
import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.data.util.files.DefaultTypeToExtensionMapper;
import org.infrastructurebuilder.util.BasicCredentials;
import org.infrastructurebuilder.util.DefaultBasicCredentials;
import org.infrastructurebuilder.util.core.IBUtils;
import org.infrastructurebuilder.util.core.Checksum;
import org.infrastructurebuilder.util.config.TestingPathSupplier;
import org.infrastructurebuilder.util.files.IBChecksumPathType;
import org.infrastructurebuilder.util.readdetect.impl.DefaultWGetterSupplier;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultWGetterSupplierTest {

  private static final String HTTP_WWW_EXAMPLE_COM_INDEX_HTML = "http://www.example.com/index.html";
  private static final String WWW_IANA_ORG = "/www.iana.org/";
  private static final Optional<Checksum> ZIP_CHECKSUM = Optional.of(new Checksum(
      "f545eeb9c46e29f5d8e29639840457de5d1bdbc34e16cbe5c1ca4b7efcbf294da0a3df41485c041cee1a25d8f0afec246cd02be1298dee9ab770a7cfed73dc71"));
  private static final Optional<Checksum> CHECKSUM = Optional.of(new Checksum(
      "d06b93c883f8126a04589937a884032df031b05518eed9d433efb6447834df2596aebd500d69b8283e5702d988ed49655ae654c1683c7a4ae58bfa6b92f2b73a"));
  private final static Logger log = LoggerFactory.getLogger(DefaultWGetterSupplierTest.class);
  private static TestingPathSupplier wps;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    wps = new TestingPathSupplier();
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    wps.finalize();
  }

  private DefaultWGetterSupplier ws;

  @Before
  public void setUp() throws Exception {
    this.ws = new DefaultWGetterSupplier(() -> log, new DefaultTypeToExtensionMapper(), wps, wps,
        new FakeArchiverManager());
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test(expected = IBDataException.class)
  public void testRetries() throws IOException {
    this.ws.get().collectCacheAndCopyToChecksumNamedFile(true, empty(), wps.get(), HTTP_WWW_EXAMPLE_COM_INDEX_HTML,
        CHECKSUM, empty(), 0, 1000, true, false);
  }

  @Test
  public void testGet() throws IOException {
    WGetter w = this.ws.get();
    Path outputPath = wps.get();

    String src = HTTP_WWW_EXAMPLE_COM_INDEX_HTML; // wps.getTestClasses().resolve("rick.jpg").toUri().toURL().toExternalForm();
    Optional<IBChecksumPathType> q = w
        .collectCacheAndCopyToChecksumNamedFile(true, empty(), outputPath, src, CHECKSUM, empty(), 5, 1000, true, false)
        .map(l -> l.get(0));
    assertTrue(q.isPresent());
    assertEquals(CHECKSUM.get().toString(), q.get().getChecksum().toString());
    assertEquals(IBConstants.TEXT_HTML, q.get().getType());
    String v = IBUtils.readToString(q.get().get());
    assertTrue(v.contains(WWW_IANA_ORG));

    // Do it again
    q = w.collectCacheAndCopyToChecksumNamedFile(false, empty(), outputPath, src, CHECKSUM, empty(), 5, 1000, false,
        false).map(l -> l.get(0));
    assertTrue(q.isPresent());
    assertEquals(CHECKSUM.get().toString(), q.get().getChecksum().toString());
    assertEquals(IBConstants.TEXT_HTML, q.get().getType());
    v = IBUtils.readToString(q.get().get());
    assertTrue(v.contains(WWW_IANA_ORG));

  }

  @Test
  public void testGetWithCreds() throws IOException {
    WGetter w = this.ws.get();
    Path outputPath = wps.get();

    String src = HTTP_WWW_EXAMPLE_COM_INDEX_HTML; // wps.getTestClasses().resolve("rick.jpg").toUri().toURL().toExternalForm();
    BasicCredentials creds = new DefaultBasicCredentials("A", of("B"));
    Optional<IBChecksumPathType> q;
    q = w
        .collectCacheAndCopyToChecksumNamedFile(false, of(creds), outputPath, src, CHECKSUM, empty(), 5, 0, true, false)
        .map(l -> l.get(0));
    assertTrue(q.isPresent());
    assertEquals(CHECKSUM.get().toString(), q.get().getChecksum().toString());
    assertEquals(IBConstants.TEXT_HTML, q.get().getType());
    String v = IBUtils.readToString(q.get().get());
    assertTrue(v.contains(WWW_IANA_ORG));

    // Do it again
    q = w.collectCacheAndCopyToChecksumNamedFile(true, empty(), outputPath, src, CHECKSUM, empty(), 5, 1000, false,
        false).map(l -> l.get(0));
    assertTrue(q.isPresent());
    assertEquals(CHECKSUM.get().toString(), q.get().getChecksum().toString());
    assertEquals(IBConstants.TEXT_HTML, q.get().getType());
    v = IBUtils.readToString(q.get().get());
    assertTrue(v.contains(WWW_IANA_ORG));

  }

  @Test
  public void testZip() throws IOException {
    WGetter w = this.ws.get();
    Path outputPath = wps.get();
    String src = "https://file-examples.com/wp-content/uploads/2017/02/zip_2MB.zip";
//    String src = wps.getTestClasses().resolve("test.zip").toUri().toURL().toExternalForm();
    Optional<List<IBChecksumPathType>> v = w.collectCacheAndCopyToChecksumNamedFile(false, empty(), outputPath, src,
        ZIP_CHECKSUM, empty(), 5, 0, true, true);
    assertTrue(v.isPresent());
    List<IBChecksumPathType> l = v.get();
    assertEquals(IBConstants.APPLICATION_ZIP, l.get(0).getType());
    assertEquals(IBConstants.APPLICATION_MSWORD, l.get(1).getType());
    assertEquals(1027072, Files.size(l.get(1).getPath()));
  }

}
