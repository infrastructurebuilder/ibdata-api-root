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
package org.infrastructurebuilder.data.schema.liquibase.refactor;

import static java.util.Objects.requireNonNull;

public class CreateIndex extends AbstractCSTTB {
  private static final long serialVersionUID = 3467442984848414265L;

  public CreateIndex(String catalogName, String schemaName, String tableName, String tablespace, String indexName,
      Boolean clustered, Boolean unique) {
    super("createIndex", catalogName, schemaName, tableName, tablespace);
    setRequiredAttribute("indexName", indexName);
    setAttribute("clustered", clustered);
    setAttribute("unique", unique);
  }

  public CreateIndex addColumn(Column c) {
    addChild(requireNonNull(c).get());
    return this;
  }

}
