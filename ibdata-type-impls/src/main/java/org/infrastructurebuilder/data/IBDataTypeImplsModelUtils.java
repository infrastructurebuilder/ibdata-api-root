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

import static java.util.Optional.ofNullable;
import static org.infrastructurebuilder.data.IBDataException.cet;
import static org.infrastructurebuilder.data.IBDataModelUtils.mapInputStreamToDataSet;

import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.spi.FileSystemProvider;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import org.infrastructurebuilder.data.model.DataSet;
import org.infrastructurebuilder.util.core.IBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface IBDataTypeImplsModelUtils {
  public final static Logger log = LoggerFactory.getLogger(IBDataTypeImplsModelUtils.class);
//  public final static Function<IBDataSetIdentifier, DataSet> dataSetIdentifierToDataSet = id -> {
//    DataSet retVal = new DataSet();
//    return retVal;
//  };
//

  // FIXME Relocate to IBDataEngine
  public static Optional<FileSystemProvider> getZipFSProvider() {
    FileSystemProvider retVal = null;
    for (FileSystemProvider provider : FileSystemProvider.installedProviders()) {
      if ("jar".equals(provider.getScheme()))
        retVal = provider;
    }
    return Optional.ofNullable(retVal);
  }

  /**
   * Map the URL pointing to a dataset XML file to a dataset if possible
   */
  public static final Function<URL, Optional<DefaultIBDataSet>> mapDataSetToDefaultIBDataSet = (url) -> {
    try {
      Objects.requireNonNull(url, "Mapping URL");
      log.info("Mapping " + url.toExternalForm());
      final String[] uset = new String[2];
      String uu = url.toExternalForm();
      if (uu.contains("!")) {
        String[] xx = uu.split("!");
        uset[0] = xx[0];
        uset[1] = xx[1];
      } else {
        uset[0] = uu;
        uset[1] = "/";
      }
      Path pathToIBDataXml, datasetRoot;
      URL left;
      if (uset[0].startsWith("jar") || uset[0].startsWith("zip")) {
        log.info("Reading from archive " + uset[0]);
        uset[0] += "!/"; // FIXME somehow we need to make this work without "/" althought that's correct

        left = IBUtils.translateToWorkableArchiveURL(uset[0]); // URL of the "root"

        FileSystem optFs = ofNullable(left)
            // // Map to URI
            .map(u -> cet.withReturningTranslation(() -> u.toURI()))
            .map(uri -> cet.withReturningTranslation(() -> FileSystems.newFileSystem(uri, Collections.emptyMap())))
            .orElseThrow(() -> new IBDataException("No filesystem for " + uset[0]));
        datasetRoot = optFs.getPath(optFs.getSeparator());
        pathToIBDataXml = optFs.getPath(uset[1]);
      } else if (uset[0].startsWith("file")) {
        log.info("Reading from file location " + uset[0]);
        pathToIBDataXml = cet.withReturningTranslation(() -> Paths.get(url.toURI()));
        datasetRoot = pathToIBDataXml.getParent().getParent();
        left = cet.withReturningTranslation(() -> datasetRoot.toUri().toURL());
      } else
        throw new IBDataException("Unrecognized URL for mapping to data set " + uset[0]);

      log.info("Dataset root is " + datasetRoot + " [" + left.toExternalForm() + "]");

      Optional<? extends DataSet> optDataSet = Optional.of(pathToIBDataXml)
          // // To Stream
          .map(u -> cet.withReturningTranslation(() -> Files.newInputStream(u)))
          // // to ExtendedDataSet
          .map(mapInputStreamToDataSet).map(ds -> {
            ds.setPath(left.toExternalForm());
            return ds;
          });

      // optDataSet contains the DataSet instance that was read from the source XML
      // WITH ITs ROOT SET!

      return optDataSet
          .map(ds -> DefaultIBDataSet.readWithSuppliers(ds, () -> datasetRoot).setUnderlyingPath(datasetRoot));
    } catch (IBDataException e) {
      // TODO Error checking
      return Optional.empty();
    }

  };

}
