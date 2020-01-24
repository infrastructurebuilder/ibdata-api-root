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

import static java.util.Objects.requireNonNull;

import java.util.List;

import org.codehaus.plexus.util.xml.Xpp3Dom;

public class Constraint extends AbstractIBDataLiquibaseRefactoring {

  private Boolean nullable;
  private String nonNullConstraintName;
  private Boolean primaryKey;
  private String primaryKeyName;
  private String primaryKeyTablespace;
  private String uniqueConstraintName;
  private Boolean unique;
  private String references;
  private String referencedTableCatalogName;
  private String referencedTableSchemaName;
  private String referencedTableName;
  private List<String> referencedColumnNames;
  private String foreignKeyName;
  private Boolean deleteCascade;
  private Boolean deferrable;
  private Boolean initiallyDeferred;
  private Boolean validateNullable;
  private Boolean validateUnique;
  private Boolean validatePrimaryKey;
  private Boolean validateForeignKey;
  private Boolean checkConstraint;

  public Constraint() {
    super("constraint");
  }

  @Override
  public Xpp3Dom get() {
    setAttribute("nullable", nullable);
    setAttribute("nonNullConstraintName", nonNullConstraintName);
    setAttribute("primaryKey", primaryKey);
    setAttribute("primaryKeyName", primaryKeyName);
    setAttribute("primaryKeyTablespace", primaryKeyTablespace);
    setAttribute("uniqueConstraintName", uniqueConstraintName);
    setAttribute("unique", unique);
    setAttribute("references", references);
    setAttribute("referencedTableCatalogName", referencedTableCatalogName);
    setAttribute("referencedTableSchemaName", referencedTableSchemaName);
    setAttribute("referencedTableName", referencedTableName);
    setAttribute("referencedColumnNames", referencedColumnNames);
    setAttribute("foreignKeyName", foreignKeyName);
    setAttribute("deleteCascade", deleteCascade);
    setAttribute("deferrable", deferrable);
    setAttribute("initiallyDeferred", initiallyDeferred);
    setAttribute("validateNullable", validateNullable);
    setAttribute("validateUnique", validateUnique);
    setAttribute("validatePrimaryKey", validatePrimaryKey);
    setAttribute("validateForeignKey", validateForeignKey);
    setAttribute("checkConstraint", checkConstraint);
    return super.get();
  }

  public Constraint setNullable(boolean val) {
    this.nullable = val;
    return this;
  }

  public Constraint setNotNullConstraintName(String name) {
    setNullable(false);
    this.nonNullConstraintName = requireNonNull(name);
    return this;
  }

  public Constraint setPrimaryKeyName(String tableSpace, String primaryKey) {
    setNullable(false);
    this.primaryKey = false;
    this.primaryKeyTablespace = tableSpace;
    this.primaryKeyName = primaryKey;
    return this;
  }

  public Constraint setUniqueConstraintName(String val) {
    this.uniqueConstraintName = val;
    return this.setUnique(true);
  }

  private Constraint setUnique(Boolean b) {
    this.unique = b;
    return this;
  }

  public Constraint setReferences(String val) {
    this.references = val;
    return this;
  }

  public Constraint setReferenceTable(String catalog, String schema, String table, List<String> columns) {
    this.referencedTableCatalogName = catalog;
    this.referencedTableSchemaName = schema;
    this.referencedTableName = table;
    this.referencedColumnNames = columns;
    return this;
  }

  public Constraint setForeignKeyName(String val) {
    this.foreignKeyName = val;
    return this;
  }

  public Constraint setDeleteCascade(Boolean val) {
    this.deleteCascade = val;
    return this;
  }
  public Constraint setDeferrable(Boolean val) {
    this.deferrable= val;
    return this;
  }
  public Constraint setInitiallyDeferred(Boolean val) {
    this.initiallyDeferred = val;
    return this;
  }
  public Constraint setValidateNullable(Boolean val) {
    this.validateNullable = val;
    return this;
  }
  public Constraint setValidateUnique(Boolean val) {
    this.validateUnique = val;
    return this;
  }
  public Constraint setValidatePrimaryKey(Boolean val) {
    this.validatePrimaryKey = val;
    return this;
  }
  public Constraint setValidateForeignKey(Boolean val) {
    this.validateForeignKey = val;
    return this;
  }
  public Constraint setCheckConstraint(Boolean val) {
    this.checkConstraint = val;
    return this;
  }
}
