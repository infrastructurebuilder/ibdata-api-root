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
package org.infrastructurebuilder.data.archiver;

import static org.junit.Assert.assertEquals;

import java.lang.System.Logger;

import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;
import org.junit.Before;
import org.junit.Test;

public class DefaultIBDataMavenArchiveFinalizerTest extends AbstractIBDataConfigTestSetup {
  private Logger l = System.getLogger(DefaultIBDataMavenArchiveFinalizerTest.class.toString());

  @Before
  public void setup() {
    super.abstractSetup();
    logger = l;
  }

  @Test
  public void testFinalizeArchiveCreationArchiver() {
    final DefaultIBDataMavenArchiveFinalizer f = new DefaultIBDataMavenArchiveFinalizer(() -> logger, c);
    f.finalizeArchiveCreation(new JarArchiver());
  }

  @Test(expected = ArchiverException.class)
  public void testNullArchiverConfig() {
    c = new IBDataLateBindingFinalizerConfigSupplier();
    c.setT(null);
    final DefaultIBDataMavenArchiveFinalizer f = new DefaultIBDataMavenArchiveFinalizer(() -> logger, c);
    f.finalizeArchiveCreation(new JarArchiver());
  }

  @Test
  public void testFinalizeArchiveExtractionUnArchiver() {
    final DefaultIBDataMavenArchiveFinalizer f = new DefaultIBDataMavenArchiveFinalizer(() -> logger, c);
    f.finalizeArchiveExtraction(new ZipUnArchiver());
  }

  @Test
  public void testGetVirtualFiles() {
    final DefaultIBDataMavenArchiveFinalizer f = new DefaultIBDataMavenArchiveFinalizer(() -> logger, c);
    assertEquals(0, f.getVirtualFiles().size());
  }

}
