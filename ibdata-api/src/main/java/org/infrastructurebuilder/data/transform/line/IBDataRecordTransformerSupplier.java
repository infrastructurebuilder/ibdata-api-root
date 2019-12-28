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
package org.infrastructurebuilder.data.transform.line;

import java.util.function.Supplier;

import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.slf4j.Logger;

/**
 * IMPORTANT!  READ THIS!
 * All Suppliers of IBDataTransformer, including suppliers of IBDataRecordTransformer,
 * are meant to provide disposable, non-singleton instances of the transformer.
 *
 * @author mykel.alvis
 *
 */
public interface IBDataRecordTransformerSupplier<I, O> extends Supplier<IBDataRecordTransformer<I, O>> {
  String getHint();

  Logger getLogger();

  IBDataRecordTransformerSupplier<I, O> configure(ConfigMapSupplier cms);

}
