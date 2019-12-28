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
import static org.infrastructurebuilder.data.IBDataException.cet;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.infrastructurebuilder.IBConstants;
import org.infrastructurebuilder.data.model.DataStream;
import org.infrastructurebuilder.util.artifacts.Checksum;
import org.infrastructurebuilder.util.files.TypeToExtensionMapper;

@SuppressWarnings("serial")
public class FakeIBDataStream extends DataStream implements IBDataStream {

  private final Path localPath;
  private final Optional<Exception> throwMeOnClose;
  private final Optional<Exception> throwMeOnRelocate;
  private final List<InputStream> opened = new ArrayList<>();
  private final IBDataStructuredDataMetadata smd;

  public FakeIBDataStream(Path p, Optional<Exception> throwMeOnClose) {
    this(p, throwMeOnClose, empty());
  }

  public FakeIBDataStream(Path p, Optional<Exception> throwMeOnClose, Optional<Exception> throwMeOnRelocate) {
    this.localPath = p;
    this.throwMeOnClose = throwMeOnClose;
    this.throwMeOnRelocate = throwMeOnRelocate;
    this.setSha512(new Checksum(p).toString());
    this.setCreationDate(new Date());
    this.setDataStreamDescription("desc");
    this.setDataStreamName("name");
    this.setMetadata(new Xpp3Dom("metadata"));
    this.setPath(".");
    this.setMimeType(IBConstants.APPLICATION_OCTET_STREAM);
    this.smd = null;
  }

  public FakeIBDataStream(DataStream ds, Path p) {
    super(ds);
    this.localPath = p;

    Checksum c = new Checksum(p);
    setSha512(c.toString());
    this.throwMeOnClose = empty();
    this.throwMeOnRelocate = empty();
    this.smd = null;
  }

  public Path getLocalPath() {
    return localPath;
  }

  @Override
  public InputStream get() {
    InputStream ins = IBDataException.cet
        .withReturningTranslation(() -> Files.newInputStream(getLocalPath(), StandardOpenOption.READ));
    opened.add(ins);
    return ins;
  }

  @Override
  public void close() throws Exception {
    this.opened.forEach(s -> {
      try {
        s.close();
      } catch (IOException e) {
        throw new IBDataException("Throw in FakeIBDataStream close().  This should never happen",e);
      }
    });
    this.opened.clear();
    if (throwMeOnClose.isPresent())
      throw throwMeOnClose.get();
  }

  @Override
  public IBDataStream relocateTo(Path newWorkingPath, TypeToExtensionMapper t2e) {
    if (throwMeOnRelocate.isPresent())
      throw new IBDataException("throwMeOnRelocate", throwMeOnRelocate.get());
    Path p = newWorkingPath.resolve(UUID.randomUUID().toString() + t2e.getExtensionForType(getMimeType()));
    cet.withReturningTranslation(() -> Files.move(Paths.get(getPath()), p));
    this.setPath(p.toString());
    return this;
  }

  @Override
  public Optional<IBDataStructuredDataMetadata> getIBDataStructuredDataMetadata() {
    return Optional.ofNullable(this.smd);
  }

  @Override
  public Optional<Path> getPathIfAvailable() {
    return Optional.of(this.localPath);
  }
}
