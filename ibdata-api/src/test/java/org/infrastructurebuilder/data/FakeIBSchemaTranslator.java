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

import java.util.List;
import java.util.Optional;

import org.infrastructurebuilder.data.schema.IBSchemaTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FakeIBSchemaTranslator implements IBSchemaTranslator<String, String> {
  public final static Logger log = LoggerFactory.getLogger(FakeIBSchemaTranslator.class);

  private String i;
  private String o;

  public FakeIBSchemaTranslator(String i, String o) {
    this.i = i;
    this.o = o;
  }

  @Override
  public Optional<String> getInboundType() {
    return Optional.of(i);
  }

  @Override
  public Optional<String> getOutboundType() {
    return Optional.of(o);
  }

  @Override
  public Optional<List<IBSchema>> from(List<IBDataDecoratedDAO<String>> s) {
    // TODO Auto-generated method stub
    return IBSchemaTranslator.super.from(s);
  }

  @Override
  public Optional<List<String>> to(List<IBSchema> s) {
    // TODO Auto-generated method stub
    return IBSchemaTranslator.super.to(s);
  }

  @Override
  public Logger getLog() {
    return log;
  }
}
