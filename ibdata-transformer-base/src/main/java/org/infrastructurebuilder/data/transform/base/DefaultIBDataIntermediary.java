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

import java.util.Map;
import java.util.Optional;

import org.infrastructurebuilder.data.IBSchema;

public class DefaultIBDataIntermediary extends AbstractIBDataIntermediary {

  /**
   *
   */
  private static final long serialVersionUID = -8639321692512100157L;

  public DefaultIBDataIntermediary(IBSchema s, float loadFactor, int concurrencyLevel) {
    super(s, loadFactor, concurrencyLevel);
  }

  public DefaultIBDataIntermediary(IBSchema s, float loadFactor) {
    super(s, loadFactor);
  }

  public DefaultIBDataIntermediary(IBSchema s, Map<? extends String, ? extends Object> m) {
    super(s, m);
  }

  public DefaultIBDataIntermediary(IBSchema s) {
    super(s);
  }

  @Override
  public <T> Optional<T> getTyped(String targetType, String name) {
    return super.getTyped(targetType, name);
  }

}
