/*
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
package org.infrastructurebuilder.data.transform;

import java.util.List;

import javax.xml.transform.Transformer;

import org.infrastructurebuilder.data.IBDataSet;
import org.infrastructurebuilder.data.IBDataStream;
import org.infrastructurebuilder.util.core.LoggerEnabled;

public interface IBDataTransformer extends LoggerEnabled {

  /**
   * This should return the Named hint of the IBDataStreamTransformationSupplier that created id (or some derivative thereof)
   * @return
   */
  String getHint();

  /**
   * Override to return if a transformer responds to a given datastream (eg, line-based transformers probably don't repsond to PDFs)
   * @param i
   * @return
   */
  default boolean respondsTo(IBDataStream i) {
    return i != null;
  }

//  /**
//   * Overrides of this method MUST return a NEW INSTANCE of the calling transformer.
//   * unless no changes are made to the state of the object,
//   * @param map
//   * @return
//   */
//  default IBDataTransformer configure(ConfigMap map) {
//    return this;
//  }

  /**
   * Transform the supplied IBDataSet to a new IBDataTransformationResult
   * TODO Call only if respondsTo(i)
   * @param ds IBDataSet that is the "ongoing transformation".  Modifying anyhting in is not defined.
   * @param suppliedStreams List of "source streams" as noted in the Transformation
   * @param failOnError fail if any error occurs, otherwise return all errors in the result
   * @return
   */
  IBDataTransformationResult transform(Transformer t, IBDataSet ds, List<IBDataStream> suppliedStreams, boolean failOnError);
}
