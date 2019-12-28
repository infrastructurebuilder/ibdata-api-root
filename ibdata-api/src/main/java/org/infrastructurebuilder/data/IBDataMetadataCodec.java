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

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * This class can read and write a DOM based on the versioned Modello models in ibdata-api
 *
 * @author mykel.alvis
 *
 */
public interface IBDataMetadataCodec {

  /**
   * The root path that all operations should proceed work with
   * @return
   */
  Path getRoot();

  /**
   * Read a dataset from an inputsttream
   * @param ins
   * @return
   */
  IBDataSet readDataSet(InputStream ins);

  /**
   * Write a set of IBDataset entries to files on the filesystem, returning a map of datasetId->directory written
   * Directories will be created and all files within the directory will be specific to that dataset
   *
   * @param datasets
   * @param targetPath
   * @return
   */
  Map<UUID, Path> writeDataSets(Set<IBDataSet> datasets, Path targetPath);

  /**
   * Writes a dataset to a new Path beneath getRoot(), returning that path once written
   * @param dataSet
   * @param outs
   * @return
   */
  Path writeDataSet(IBDataSet dataSet);

  IBDataSet transform(IBDataSetIdentifier dataSet);
}
