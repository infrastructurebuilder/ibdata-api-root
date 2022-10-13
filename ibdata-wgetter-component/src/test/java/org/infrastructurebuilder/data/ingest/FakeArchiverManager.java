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

import java.io.File;
import java.util.Locale;

import javax.annotation.Nonnull;

import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.manager.ArchiverManager;
import org.codehaus.plexus.archiver.manager.NoSuchArchiverException;
import org.codehaus.plexus.archiver.zip.PlexusIoZipFileResourceCollection;
import org.codehaus.plexus.archiver.zip.ZipArchiver;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;
import org.codehaus.plexus.components.io.resources.PlexusIoResourceCollection;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;

public class FakeArchiverManager implements ArchiverManager {

  private static @Nonnull String getFileExtention(@Nonnull final File file) {
    final String path = file.getAbsolutePath();

    String archiveExt = FileUtils.getExtension(path).toLowerCase(Locale.ENGLISH);

    if ("gz".equals(archiveExt) || "bz2".equals(archiveExt) || "xz".equals(archiveExt) || "snappy".equals(archiveExt)) {
      final String[] tokens = StringUtils.split(path, ".");

      if (tokens.length > 2 && "tar".equals(tokens[tokens.length - 2].toLowerCase(Locale.ENGLISH))) {
        archiveExt = "tar." + archiveExt;
      }
    }

    return archiveExt;

  }

  @Override
  @Nonnull
  public Archiver getArchiver(@Nonnull final File file) throws NoSuchArchiverException {
    return getArchiver(getFileExtention(file));
  }

  @Override
  @Nonnull
  public Archiver getArchiver(@Nonnull final String archiverName) throws NoSuchArchiverException {
      return new ZipArchiver();
  }

  @Override
  @Nonnull
  public PlexusIoResourceCollection getResourceCollection(@Nonnull final File file) throws NoSuchArchiverException {
    return getResourceCollection(getFileExtention(file));
  }

  @Override
  public @Nonnull PlexusIoResourceCollection getResourceCollection(final String resourceCollectionName)
      throws NoSuchArchiverException {
    return new PlexusIoZipFileResourceCollection();
  }

  @Override
  @Nonnull
  public UnArchiver getUnArchiver(@Nonnull final File file) throws NoSuchArchiverException {
    return getUnArchiver(getFileExtention(file));
  }

  @Override
  @Nonnull
  public UnArchiver getUnArchiver(@Nonnull final String unArchiverName) throws NoSuchArchiverException {
      ZipUnArchiver a = new ZipUnArchiver();
      a.enableLogging(new ConsoleLogger(Logger.LEVEL_DEBUG, "A"));
      return a;
  }

}
