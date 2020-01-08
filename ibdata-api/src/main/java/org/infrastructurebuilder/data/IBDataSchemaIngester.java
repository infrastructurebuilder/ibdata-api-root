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

import java.util.SortedMap;
import java.util.SortedSet;
import java.util.function.Supplier;

import org.infrastructurebuilder.util.LoggerEnabled;
import org.infrastructurebuilder.util.config.ConfigMap;

/**
 * Instances of this need to Inject a  Named(IBDATA_WORKING_PATH_SUPPLIER) WorkingPathSupplier wps
 * if they need working paths.  The ingester mojo will configure that path properly.
 * @author mykel.alvis
 *
 */
public interface IBDataSchemaIngester extends LoggerEnabled {

  IBDataSchemaIngester configure(ConfigMap map);
  /**
   *
   * Reads a source and returns a calculated set of attributes.  Since the result of any write action is
   * expected to be the output of some finalizer, the execution of how that set of data arrives is left to that component
   *
   * Ingestion returns an List<IBDataStreamSupplier>, which is supplied to a finalizer to produce a DataSet

   * @param dss
   * @return
   */
  SortedSet<Supplier<IBSchema>> ingest(SortedMap<String,IBDataSchemaSupplier> dss);
}
