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
package org.infrastructurebuilder.data.schema.liquibase.refactor;

import java.util.Objects;

import org.codehaus.plexus.util.xml.Xpp3Dom;

public class Column extends AbstractIBDataLiquibaseRefactoring {
  private static final long serialVersionUID = 2335148943544066898L;
  private Boolean autoIncrement;
  private Integer incrementBy;
  private String remarks;
  private Boolean descending;
  private String value;
  private String valueType;
  private boolean setToNull = false;

  public Column(String name, String type) {
    this(name, type, null);
  }

  public Column(String name, String type, String value) {
    super("column");
    setRequiredAttribute("name", name);
    setRequiredAttribute("type", type);
    this.value = value;
  }

  public Column setColumnValue(String value) {
    this.value = value;
    if (this.valueType == null)
      this.valueType = "value";
    setToNull = (value == null);
    return this;
  }

  public Column setAutoIncrement(boolean val) {
    this.autoIncrement = val;
    return this;
  }

  public Column setIncrementBy(int val) {
    this.incrementBy = val;
    return this;
  }

  public Column setRemarks(String val) {
    this.remarks = val;
    return this;
  }

  public Column setDescending(Boolean val) {
    this.descending = val;
    return this;
  }

  public Column setValueNumeric() {
    this.valueType = "valueNumeric";
    return this;
  }

  public Column setValueBoolean() {
    this.valueType = "valueBoolean";
    return this;
  }

  public Column setValueDate() {
    this.valueType = "valueDate";
    return this;
  }

  public Column setValueCLOB() {
    this.valueType = "valueClobFile";
    return this;
  }

  public Column setValueBLOB() {
    this.valueType = "valueBlobFile";
    return this;
  }

  @Override
  public Xpp3Dom get() {
    if (this.setToNull)
      this.value = "NULL";
    setAttribute(valueType, value);
    setAttribute("autoIncrement", autoIncrement);
    setAttribute("incrementBy", incrementBy);
    setAttribute("remarks", remarks);
    setAttribute("descending", descending);
    return super.get();
  }

  public Column addConstraint(Constraint c) {
    this.addChild(Objects.requireNonNull(c.get()));
    return this;

  }
}
