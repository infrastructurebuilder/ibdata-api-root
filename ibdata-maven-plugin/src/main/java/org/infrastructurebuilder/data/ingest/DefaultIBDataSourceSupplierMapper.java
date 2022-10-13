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

import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.UUID.randomUUID;
import static org.infrastructurebuilder.util.constants.IBConstants.IBDATA_PREFIX;
import static org.infrastructurebuilder.util.constants.IBConstants.IBDATA_SUFFIX;
import static org.infrastructurebuilder.data.IBDataConstants.IBDATA_WORKING_PATH_SUPPLIER;
import static org.infrastructurebuilder.data.IBDataException.cet;
import static org.infrastructurebuilder.util.files.DefaultIBChecksumPathType.copyToDeletedOnExitTempChecksumAndPath;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import org.codehaus.plexus.archiver.manager.ArchiverManager;
import org.infrastructurebuilder.data.AbstractIBDataSource;
import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.data.IBDataSource;
import org.infrastructurebuilder.data.IBDataSourceSupplier;
import org.infrastructurebuilder.data.IBDataStreamIdentifier;
import org.infrastructurebuilder.util.BasicCredentials;
import org.infrastructurebuilder.util.core.IBUtils;
import org.infrastructurebuilder.util.LoggerSupplier;
import org.infrastructurebuilder.util.core.Checksum;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.PathSupplier;
import org.infrastructurebuilder.util.files.IBChecksumPathType;
import org.infrastructurebuilder.util.files.TypeToExtensionMapper;
import org.slf4j.Logger;
import org.w3c.dom.Document;

@Named
public class DefaultIBDataSourceSupplierMapper extends AbstractIBDataSourceSupplierMapper {
  public final static List<String> HEADERS = Arrays.asList("http://", "https://", "file:", "zip:", "jar:");
  private final WGetterSupplier wgs;
  private final ArchiverManager archiverManager;

  @Inject
  public DefaultIBDataSourceSupplierMapper(LoggerSupplier l, TypeToExtensionMapper t2e, WGetterSupplier wgs,
      ArchiverManager am, @Named(IBDATA_WORKING_PATH_SUPPLIER) PathSupplier workingPathSupplier) {
    super(requireNonNull(l).get(), requireNonNull(t2e), workingPathSupplier);
    this.wgs = requireNonNull(wgs);
    this.archiverManager = requireNonNull(am);
  }

  public List<String> getHeaders() {
    return HEADERS;
  }

  @Override
  public IBDataSourceSupplier getSupplierFor(String temporaryId, IBDataStreamIdentifier v) {
    return new DefaultIBDataSourceSupplier(temporaryId,
        new DefaultIBDataSource(getLog(),
            v.getURL().orElseThrow(() -> new IBDataException("No url for " + temporaryId)), v.isExpandArchives(),
            v.getName(), v.getDescription(), ofNullable(v.getChecksum()), of(v.getMetadataAsDocument()),
            ofNullable(v.getMimeType()), wgs, this.archiverManager, getMapper()),
        getWorkingPath());
  }

  public class DefaultIBDataSource extends AbstractIBDataSource {

    private final Path targetPath;
    private final Optional<String> mimeType;
    private final WGetterSupplier wgs;
    private final ArchiverManager am;
    private final TypeToExtensionMapper mapper;
    private List<IBChecksumPathType> read;

    private DefaultIBDataSource(Logger log, String id, String sourceUrl, boolean expandArchives,
        Optional<BasicCredentials> creds, Optional<Checksum> checksum, Optional<Document> metadata,
        Optional<ConfigMap> additionalConfig, Path targetPath, Optional<String> name, Optional<String> description,
        Optional<String> mimeType, WGetterSupplier wgs, ArchiverManager am, TypeToExtensionMapper mapper) {

      super(log, id, sourceUrl, expandArchives, name, description, creds, checksum, metadata, additionalConfig);
      this.targetPath = targetPath;
      this.mimeType = mimeType;
      this.wgs = requireNonNull(wgs);
      this.am = requireNonNull(am);
      this.mapper = requireNonNull(mapper);
    }

    public DefaultIBDataSource(Logger log, String source, boolean expandArchives, Optional<String> name,
        Optional<String> description, Optional<Checksum> checksum, Optional<Document> metadata,
        Optional<String> targetType, WGetterSupplier wgs, ArchiverManager am, TypeToExtensionMapper mapper) {
      this(log, randomUUID().toString(), source, expandArchives, empty(), checksum, metadata, empty(), null, name,
          description, targetType, wgs, am, mapper);
    }

    @Override
    public IBDataSource withAdditionalConfig(ConfigMap config) {
      return new DefaultIBDataSource(getLog(), getId(), getSourceURL(), isExpandArchives(), getCredentials(),
          getChecksum(), getMetadata(), of(config), getWorkingPath(), getName(), getDescription(), getMimeType(),
          this.wgs, this.am, this.mapper);
    }

    @Override
    public Optional<String> getMimeType() {
      return this.mimeType;
    }

    @Override
    public List<IBChecksumPathType> get() {
      return ofNullable(targetPath).map(target -> {
        if (this.read == null) {
          List<IBChecksumPathType> localRead;
          URL src = IBUtils.translateToWorkableArchiveURL(source);
          WGetter wget = wgs.get();
          switch (src.getProtocol()) {
          case "http":
          case "https":
            localRead = wget.collectCacheAndCopyToChecksumNamedFile(false, getCredentials(),
                // Target directory
                targetPath,
                // URL
                src.toExternalForm(),
                // "Optional" checksum
                checksum,
                // Mime type from supplied value (not calculated value)
                this.mimeType,
                // Slightly insane defaults
                3, 0, false, isExpandArchives()).orElseThrow(() -> new IBDataException("Could not read "));
            break;
          case "file":
          case "zip":
          case "jar":
            try (InputStream ins = src.openStream()) {
              localRead = new ArrayList<>();
              IBChecksumPathType val = cet.withReturningTranslation(
                  () -> copyToDeletedOnExitTempChecksumAndPath(targetPath, IBDATA_PREFIX, IBDATA_SUFFIX, ins));
              localRead.add(val);
              if (isExpandArchives()) {
                localRead.addAll(wget.expand(targetPath, val, of(src.toExternalForm())));
              }

            } catch (IOException e) {
              throw new IBDataException(e);
            }
            break;
          default:
            throw new IBDataException("Default processor cannot handle protocol " + source);
          }
          read = localRead;
        }
        return read;
      }).orElse(Collections.emptyList());
    }

  }

}
