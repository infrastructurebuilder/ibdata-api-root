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

import static java.util.Optional.empty;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.infrastructurebuilder.util.config.ConfigMap;
import org.slf4j.Logger;

/**
 * For ease of use making a component that does this. Some concrete class would be injected into an IBDataTransformer
 * @author mykel.alvis
 *
 * @param <I> Input type (line of text, array of strings, whatever, Avro record)
 *      The inbound value with ALWAYS be non-null
 * @param <O> Output type (target type (frequently Avro record)
 *      The result of the apply CAN be null, which should terminate processing for that record/row/whatever
 * @param <T>
 */
public interface IBDataRecordTransformer<I, O> extends Function<I, O> {
  /**
   * The name/id of the IBDataRecordTransformer.
   * This must be unique among all loaded transformers
   * @return
   */
  public String getHint();

  boolean respondsTo(Object o);

  Class<I> getInboundClass();

  Class<O> getOutboundClass();



  IBDataRecordTransformer<I, O> configure(ConfigMap cms);

  Logger getLogger();

  /**
   * Return a new type that this transformer produces.
   * @return new type that this transformer produces as output or "empty()" if  pass-thru (i.e. the original type should be retained)
   */
  default Optional<Class<?>> produces() {  // FIXME Value is currently not used during execution
    return empty();
  }

  /**
   * Return an OPTIONAL LIST (I know, but it does the job), of types that this record transformer can accept
   * This is a coordination effort between types of record transformers.
   * @return empty if any type is acceptable (as with a pass thru)
   */
  default Optional<List<Class<?>>> accepts() {
    return empty();
  }

}
