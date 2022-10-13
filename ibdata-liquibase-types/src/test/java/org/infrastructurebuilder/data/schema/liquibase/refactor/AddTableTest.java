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

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AddTableTest {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private AddTable t;

  @Before
  public void setUp() throws Exception {
    t = new AddTable(null, null, "table", null, null);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testAddColumnStringString() {
    t.addColumn("A", "B");
    assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<createTable tableName=\"table\">\n" +
        "  <column name=\"A\" type=\"B\"/>\n" +
        "</createTable>", t.get().toString());
  }

  @Test
  public void testAddTable() {
    assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<createTable tableName=\"table\"/>",
        t.get().toString());
    t = new AddTable("catalog", "schema", "table", "tablespace", "remarks");
    assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
        + "<createTable catalogName=\"catalog\" tablespace=\"tablespace\" schemaName=\"schema\" "
        + "remarks=\"remarks\" tableName=\"table\"/>", t.get().toString());
  }

  @Test
  public void testAddColumn() {
    t.addColumn(
        new Column("x", "BIGINT").setAutoIncrement(true).setColumnValue("1").setDescending(false).setIncrementBy(1)
            .setRemarks("remarkable").setValueBLOB().setValueCLOB().setValueBoolean().setValueDate().setValueNumeric()
            .addConstraint(new Constraint().setCheckConstraint(true).setDeferrable(true).setDeleteCascade(true)
                .setForeignKeyName("fk").setInitiallyDeferred(true).setNotNullConstraintName("notnullName")
                .setNullable(false).setPrimaryKeyName("tablespace", "pkname").setReferences("references")
                .setReferenceTable("catalog", "schema", "table", Arrays.asList("A", "B"))
                .setUniqueConstraintName("uneek").setValidateForeignKey(false).setValidateNullable(false)
                .setValidatePrimaryKey(true).setValidateUnique(false)));
    assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<createTable tableName=\"table\">\n"
        + "  <column valueNumeric=\"1\" autoIncrement=\"true\" incrementBy=\"1\" name=\"x\" type=\"BIGINT\" remarks=\"remarkable\" descending=\"false\">\n"
        + "    <constraint primaryKeyTablespace=\"tablespace\" validateUnique=\"false\" "
        + "deferrable=\"true\" nullable=\"false\" references=\"references\" "
        + "nonNullConstraintName=\"notnullName\" referencedTableCatalogName=\"catalog\" "
        + "referencedTableSchemaName=\"schema\" validatePrimaryKey=\"true\" "
        + "validateForeignKey=\"false\" checkConstraint=\"true\" referencedTableName=\"table\" "
        + "deleteCascade=\"true\" referencedColumnNames=\"A,B\" validateNullable=\"false\" "
        + "unique=\"true\" uniqueConstraintName=\"uneek\" primaryKeyName=\"pkname\" "
        + "initiallyDeferred=\"true\" foreignKeyName=\"fk\" primaryKey=\"false\"/>\n" + "  </column>\n"
        + "</createTable>", t.get().toString());
  }

}
