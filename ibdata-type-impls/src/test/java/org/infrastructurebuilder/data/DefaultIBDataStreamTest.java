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

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.infrastructurebuilder.util.files.DefaultIBChecksumPathType.copyToDeletedOnExitTempChecksumAndPath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import org.infrastructurebuilder.data.model.DataStream;
import org.infrastructurebuilder.data.model.IBMetadataUtils;
import org.infrastructurebuilder.exceptions.IBException;
import org.infrastructurebuilder.util.core.Checksum;
import org.infrastructurebuilder.util.core.IBUtils;
import org.infrastructurebuilder.util.core.TestingPathSupplier;
import org.infrastructurebuilder.util.extensionmapper.basic.DefaultTypeToExtensionMapper;
import org.infrastructurebuilder.util.files.IBChecksumPathType;
import org.infrastructurebuilder.util.readdetect.ThrowingIBChecksumType;
import org.infrastructurebuilder.util.readdetect.ThrowingInputStream;
import org.joor.Reflect;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public class DefaultIBDataStreamTest {
  public final static Logger log = LoggerFactory.getLogger(DefaultIBDataStreamTest.class);
  public static final String CHECKSUM = "3b2c63ccb53069e8b0472ba50053fcae7d1cc84ef774ff2b01c8a0658637901b7d91e71534243b5d29ee246e925efb985b4dbd7330ab1ab251d1e1b8848b9c49";

  private static final String JPG = "image/jpeg";
  private static final String DESC = "Rickrolled";
  private static final String NAME = "name";

  /**
   * Move to IBUtils in next release
   * @param ins
   * @param target
   * @return
   */
  public static Checksum copyAndDigest(final InputStream ins, final Path target) {
    try (OutputStream outs = Files.newOutputStream(target, StandardOpenOption.TRUNCATE_EXISTING,
        StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
      return IBUtils.copyAndDigest(ins, outs);
    } catch (IOException | NoSuchAlgorithmException e) {
      throw new IBException(e);
    }
  }

  private Path path, path2;
  private DefaultIBDataStreamIdentifier identifier, identifier2;
  private DefaultIBDataStream ib1;
  private Checksum checksum, checksum2;
  private Document metadata;
  private String mimeType;
  private InputStream rick, lines;
  private Path p1;
  private DefaultIBDataStream ib2;
  private DataStream ds;
  private IBChecksumPathType cType;
  private DataStream ds2;
  private final static Date now = new Date();
  private static TestingPathSupplier wps = new TestingPathSupplier();

  @Before
  public void setUp() throws Exception {
    p1 = wps.get();
    path = p1.resolve(UUID.randomUUID().toString() + ".jpg");
    path2 = p1.resolve(UUID.randomUUID().toString() + ".txt");
    metadata = IBMetadataUtils.emptyDocumentSupplier.get();
    mimeType = JPG;
    rick = getClass().getResourceAsStream("/rick.jpg");
    checksum = copyAndDigest(rick, path);
    cType = copyToDeletedOnExitTempChecksumAndPath(wps.get(), "a", "b",
        getClass().getResourceAsStream("/lines.txt"));
    identifier = new DefaultIBDataStreamIdentifier(checksum.asUUID().get(), of(p1.toUri().toURL().toExternalForm()),
        of(NAME), of(DESC), checksum, now, metadata, mimeType, of(path.toString()),empty(), empty());
    ib1 = new DefaultIBDataStream(identifier, path);
    ib2 = new DefaultIBDataStream(identifier, new ThrowingIBChecksumType());

    ds = new DataStream();
    ds.setPath(path.toString());
    ds.setSha512(CHECKSUM);
    ds.setUuid(UUID.randomUUID().toString());
    ds.setMimeType(JPG);
    ds.setCreationDate(now);
    ds2 = new DataStream();
    ds2.setPath(path.toString());
    ds2.setSha512(CHECKSUM);
    ds2.setUuid(UUID.randomUUID().toString());
    ds2.setMimeType(cType.getType());
    ds2.setCreationDate(now);
  }

  @Test(expected = IBException.class)
  public void testCopyAndDigest() {
    copyAndDigest(new ThrowingInputStream(IOException.class), path);
  }

  //  @Test
  //  public void testDataStreamIdentifierConstructor2() {
  //    IBDataSource source = null;
  //    assertNotNull(new DefaultIBDataStreamIdentifier(source, now, of(path.toString())));
  //  }

  @Test
  public void testDataStreamIdentifierCopyConstructor() {
    assertNotNull(new DefaultIBDataStreamIdentifier(identifier));
  }

  @Test
  public void testGetChecksum() {
    assertEquals(checksum, ib1.getChecksum());
    assertEquals(checksum, ib1.getChecksum()); // Twice for cached copy
  }

  @Test(expected = IBDataException.class)
  public void testGetChecksumWithFailingInputStream() {
    Supplier<InputStream> ins = () -> new ThrowingInputStream(IOException.class);
    Reflect.on(ib2).set("ss", ins).set("calculatedChecksum", null);
    ib2.getChecksum(); // Setup to fail
  }

  @Test
  public void testGet() {
    Path p2 = p1.resolve(UUID.randomUUID().toString());
    Checksum d = copyAndDigest(ib1.get(), p2);
    assertEquals(checksum, d);
  }

  @Test
  public void testClose() throws Exception {
    List<InputStream> s = Reflect.on(ib1).get("createdInputStreamsForThisInstance");
    s.add(new ThrowingInputStream(IOException.class));
    ib1.close();
  }

  @Test
  public void testRelocateTo() throws Exception {
    Path p2 = wps.get();
    IBDataStream v = ib1.relocateTo(p2, new DefaultTypeToExtensionMapper());
    assertNotNull(v);
  }

  @Test
  public void testGetId() {
    assertEquals(checksum.asUUID().get(), ib1.getId());
  }

  @Test
  public void testGetURL() throws MalformedURLException {
    assertTrue(ib1.getURL().get().startsWith(p1.toUri().toURL().toExternalForm()));
  }

  @Test
  public void testGetName() {
    assertEquals(NAME, ib1.getName().get());
  }

  @Test
  public void testGetDescription() {
    assertEquals(DESC, ib1.getDescription().get());
  }

  @Test
  public void testGetCreationDate() {
    assertEquals(now, ib1.getCreationDate());
  }

  @Test
  public void testGetMetadata() {
    assertEquals(metadata, ib1.getMetadata());
  }

  @Test
  public void testGetMimeType() {
    assertEquals(JPG, ib1.getMimeType());
  }

  @Test
  public void testGetPath() {
    assertEquals(path.toString(), ib1.getPath());
  }

  @Test
  public void testFrom() {
    DefaultIBDataStream v = DefaultIBDataStream.from(ds, () -> path);
  }

  @Test
  public void testStringStreamInappropriateSupplier() {
    DefaultIBDataStream v = DefaultIBDataStream.from(ds, () -> path);
    DefaultStringStreamSupplier dss = new DefaultStringStreamSupplier(() -> log);
    assertFalse(dss.from(v).isPresent());
  }

  @Test
  public void testStringStreamAppropriateSupplier() throws IOException {
    DefaultIBDataStream v = DefaultIBDataStream.from(ds2, () -> path2);
    InputStream i = v.get();
    i.close();
    i = v.get();
    i.close();
    DefaultStringStreamSupplier dss = new DefaultStringStreamSupplier(() -> log);
    assertTrue(dss.from(v).isPresent());
  }

}
