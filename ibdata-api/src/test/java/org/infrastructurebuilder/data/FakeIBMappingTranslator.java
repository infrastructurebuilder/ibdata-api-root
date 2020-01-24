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

import java.util.Map;
import java.util.stream.Collectors;

public class FakeIBMappingTranslator implements IBMappingTranslator<Map<String,Object>> {

  private String type;

  public FakeIBMappingTranslator(String type) {
    this.type = type;
  }
  @Override
  public String getType() {
    // TODO Auto-generated method stub
    return type;
  }
  @Override
  public Map<String, Object> from(Map<String, Object> t) {
    // TODO Auto-generated method stub
    return t.entrySet().stream().collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue().toString().toUpperCase()));
  }
  @Override
  public Map<String, Object> to(Map<String, Object> o) {
    return o.entrySet().stream().collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue().toString().toLowerCase()));
  }

}
