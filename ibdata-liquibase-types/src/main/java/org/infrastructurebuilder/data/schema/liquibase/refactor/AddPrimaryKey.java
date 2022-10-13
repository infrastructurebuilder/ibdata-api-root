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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.infrastructurebuilder.data.IBDataException;

public class AddPrimaryKey extends AbstractCSTTB {
  private static final long serialVersionUID = -4662870696860708839L;
  private final List<String> columnNames = new ArrayList<>();

  public AddPrimaryKey(String catalogName, String schemaName, String tableName, String tablespace,
      String constraintName,  Boolean clustered, Boolean unique, Boolean validate) {
    super("addPrimaryKey", catalogName, schemaName, tableName, tablespace);
    setRequiredAttribute("constraintName", constraintName);
    setAttribute("clustered", clustered);
    setAttribute("unique", unique);
    setAttribute("validate", validate);
  }

  public AddPrimaryKey addColumns(List<String> name) {
    columnNames.addAll(requireNonNull(name));
    return this;
  }

  @Override
  public Xpp3Dom get() {
    if (columnNames.size() == 0)
      throw new IBDataException("No columns names for AddPrimaryKey");
    setRequiredAttribute("columnNames", columnNames.stream().collect(Collectors.joining(",")));
    return super.get();
  }
}
