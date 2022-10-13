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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.data.transform.Record;
import org.infrastructurebuilder.util.LoggerSupplier;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.config.DefaultConfigMapSupplier;
import org.infrastructurebuilder.util.config.PathSupplier;

public class ArrayToNameMapIBDataLineTransformerSupplierTest
    extends AbstractTTestClass<ArrayToNameMapIBDataLineTransformerSupplier, String[], Map<String, String>> {

  @Override
  Class<String[]> getI() {
    return String[].class;
  }

  @Override
  Class<Map<String, String>> getO() {
    Map<String, String> c = new HashMap<>();
    return (Class<Map<String, String>>) c.getClass();
  }

  @Override
  ArrayToNameMapIBDataLineTransformerSupplier getT(PathSupplier wps, LoggerSupplier l) {
    return new ArrayToNameMapIBDataLineTransformerSupplier(wps, l);
  }

  @Override
  public ConfigMapSupplier getCMS() {
    ConfigMap cm = new ConfigMap();
    List<String> value = Arrays.asList("A", "B");
    cm.put(Record.FIELD_KEY, value);
    return new DefaultConfigMapSupplier(cm);
  }

  @Override
  public void failTest() throws Exception {
    throw new IBDataException();
  }

  @Override
  Map<String, String> getSuccessTestValue() {
    Map<String, String> v = new HashMap<>();
    v.put("A", "1");
    v.put("B", "2");
    return v;
  }

  @Override
  String[] getSuccessTestData() {
    // More data than the FIELD_KEY asks for, so the last value is truncated
    String[] s = { "1", "2", "3" };
    return s;
  }

}
