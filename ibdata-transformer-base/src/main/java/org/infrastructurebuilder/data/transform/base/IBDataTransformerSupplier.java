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
package org.infrastructurebuilder.data.transform.base;

import java.util.function.Supplier;

import org.infrastructurebuilder.data.IBDataStreamRecordFinalizer;
import org.infrastructurebuilder.data.transform.IBDataTransformer;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;

/**
 * IMPORTANT!  READ THIS!
 * All Suppliers of IBDataTransformer, including suppliers of IBDataRecordTransformer,
 * are meant to provide disposable, non-singleton instances of the transformer.
 *
 * @author mykel.alvis
 *
 */
public interface IBDataTransformerSupplier extends Supplier<IBDataTransformer> {

  /**
   *
   * Return a NEW INSTANCE of IBDataTransformerSupplier.  Methods implementing this must not
   * <code>return this;</code>
   *
   * This method must be called before <code>configure()</code>
   *
   * Only actually necessary with record-based systems.  Everyone else should <code>return this;</code>
   * and write files like adults
   *
   * @param ts2
   * @return
   */

  IBDataTransformerSupplier withFinalizer(IBDataStreamRecordFinalizer<?> ts2);
  /**
   * Return a NEW INSTANCE of IBDataTransformerSupplier.  Methods implementing this must not
   * <code>return this;</code>
   *
   * @param cms
   * @return
   */
  IBDataTransformerSupplier configure(ConfigMapSupplier cms);

}
