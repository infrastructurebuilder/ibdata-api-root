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
package org.infrastructurebuilder.data.archive;

import static org.infrastructurebuilder.data.IBDataConstants.IBDATA_WORKING_PATH_SUPPLIER;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.maven.plugin.logging.Log;
import org.infrastructurebuilder.data.AbstractIBDataMavenComponent;
import org.infrastructurebuilder.data.IBDataSetFinalizerSupplier;
import org.infrastructurebuilder.data.IBStreamerFactory;
import org.infrastructurebuilder.data.archiver.IBDataLateBindingFinalizerConfigSupplier;
import org.infrastructurebuilder.data.ingest.IBDataIngesterSupplier;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.config.PathSupplier;
import org.infrastructurebuilder.util.files.TypeToExtensionMapper;

@Named("package")
public class IBDataPackageMavenComponent extends AbstractIBDataMavenComponent {
  /**
   * Injected constructor.
   * @param workingPathSupplier  Late bound PathSupplier, which is setup within _setup() to contain the working directory
   * @param log Maven log
   * @param defaultTypeToExtensionMapper a TypeToExtensionMapper, which maps mime types to file extensions
   * @param mavenConfigMapSupplier Supplier of Map/String/String instance which is bound to many (most) maven properties
   */
  @Inject
  public IBDataPackageMavenComponent(
      // Late-bound  PathSupplier.  Must be set in the executor before use
      @Named(IBDATA_WORKING_PATH_SUPPLIER) PathSupplier workingPathSupplier,
      // The logger
      Log log,
      // Mapper for extensions to mime types
      TypeToExtensionMapper defaultTypeToExtensionMapper,
      // The configuration map. Does not include config from components
      @Named(ConfigMapSupplier.MAVEN) ConfigMapSupplier mavenConfigMapSupplier,
      //
      Map<String, IBDataIngesterSupplier> allIngesters,
      Map<String, IBDataSetFinalizerSupplier<?>> allDSFinalizers,
      final IBStreamerFactory notUsed) {
    super(workingPathSupplier, log, defaultTypeToExtensionMapper, mavenConfigMapSupplier, allDSFinalizers, notUsed);
  }

}
