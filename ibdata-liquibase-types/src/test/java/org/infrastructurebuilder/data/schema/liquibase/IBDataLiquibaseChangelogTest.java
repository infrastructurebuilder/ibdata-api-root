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
package org.infrastructurebuilder.data.schema.liquibase;

import static org.infrastructurebuilder.data.schema.liquibase.IBDataLiquibaseChangelog.DATABASE_CHANGE_LOG;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.infrastructurebuilder.data.IBDataConstants;
import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.data.schema.liquibase.refactor.AddTable;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class IBDataLiquibaseChangelogTest {

  private static final String XML_LINE_1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
  private static final String XML_LINE_2 = "<databaseChangeLog xmlns=\"http://www.liquibase.org/xml/ns/dbchangelog\" ";
  private static final String XML_LINE_3 = "xmlns:pro=\"http://www.liquibase.org/xml/ns/pro\" ";
  private static final String XML_LINE_4 = "xsi:schemaLocation=\"http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.8.xsd&#10;&#9;http://www.liquibase.org/xml/ns/dbchangelog-ext http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-ext.xsd&#10;&#9;http://www.liquibase.org/xml/ns/pro http://www.liquibase.org/xml/ns/pro/liquibase-pro-3.8.xsd \" ";
  private static final String XML_LINE_5 = "xmlns:ext=\"http://www.liquibase.org/xml/ns/dbchangelog-ext\" ";
  private static final String XML_LINE_6 = "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"";
  private static final String EMPTY_CHECKSUM = "88a898a79f8c638264471d8ec39fada4e333b39b4d979243c8b2e4c9e8fc2c3991586321eef3ede2fe8f37ccb75ef70af9c151c69b7bd93d73e4a7016b5efda2";

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private IBDataLiquibaseChangelog cl;
  private IBDataLiquibaseRollback rollback;
  private AddTable refactoring;

  @Before
  public void setUp() throws Exception {
    cl = new IBDataLiquibaseChangelog();
    rollback = new IBDataLiquibaseRollback("id", "author", "dbms", true, true, true, Arrays.asList("a"), false,
        LiquibaseQuotingStrategies.LEGACY);
    refactoring = new AddTable(null, null, "table", null, null);

  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testIBDataLiquibaseChangelog() {
    assertEquals(XML_LINE_1 + XML_LINE_2 + XML_LINE_3 + XML_LINE_4 + XML_LINE_5 + XML_LINE_6 + "/>",
        cl.get().toString());
  }

  @Test(expected = IBDataException.class)
  public void testIBDataLiquibaseChangelogXpp3Dom() {
    Xpp3Dom d = new Xpp3Dom("abc");
    assertNotNull(new IBDataLiquibaseChangelog(d));
  }

  @Test
  public void testIBDataLiquibaseChangelogCL() {
    Xpp3Dom d = new Xpp3Dom(DATABASE_CHANGE_LOG);
    assertNotNull(new IBDataLiquibaseChangelog(d));
  }

//  @Test
//  public void testIBDataLiquibaseChangelogObject() {
//    String x = "<" + DATABASE_CHANGE_LOG + " />";
//    assertNotNull(new IBDataLiquibaseChangelog(x));
//  }

  @Test
  public void testAsChecksum() {
    assertEquals(EMPTY_CHECKSUM, cl.asChecksum().toString());
  }

//  @Test
//  public void testAsDocumentSupplier() {
//    assertNull(cl.asDocumentSupplier().get());
//  }
//
  @Test
  public void testAddPreCondition() {
    cl.addPreCondition(new Xpp3Dom("precondition"));
    assertEquals(XML_LINE_1 + XML_LINE_2 + XML_LINE_3 + XML_LINE_4 + XML_LINE_5 + XML_LINE_6 + ">"
        + "\n  <preConditions>\n" + "    <precondition/>\n" + "  </preConditions>\n" + "</databaseChangeLog>",
        cl.get().toString());
  }

  @Test
  public void testAddProperty() {
    cl.addProperty("name", "value", "context", "dbms", true);
    assertEquals(XML_LINE_1 + XML_LINE_2 + XML_LINE_3 + XML_LINE_4 + XML_LINE_5 + XML_LINE_6 + ">"
        + "\n  <property name=\"name\" context=\"context\" dbms=\"dbms\" global=\"true\" value=\"value\"/>\n"
        + "</databaseChangeLog>", cl.get().toString());

  }

  @Test
  public void testAddInclude() {
    cl.addInclude("filename", false, Arrays.asList("context1", "context2"));
    assertEquals(XML_LINE_1 + XML_LINE_2 + XML_LINE_3 + XML_LINE_4 + XML_LINE_5 + XML_LINE_6 + ">"
        + "\n  <include file=\"filename\" context=\"context1,context2\" relativeToChangelogFile=\"false\"/>\n"
        + "</databaseChangeLog>", cl.get().toString());

  }

  @Test
  public void testAddChangeSet() {
    Xpp3Dom validChecksum = new Xpp3Dom("validChecksum");
    validChecksum.setValue("abc");
    IBDataLiquibaseChangeSet cs = new IBDataLiquibaseChangeSet("id", "author", "dbms", false, false, false,
        Arrays.asList("c1", "c2"), false, LiquibaseQuotingStrategies.QUOTE_ONLY_RESERVED_WORDS).addComment("comment")
            .addPreCondition(new Xpp3Dom("preCondition")).addRefactoring(refactoring).addRollback(rollback)
            .addValidChecksum(validChecksum);

    cl.addChangeSet(cs);
    assertEquals(XML_LINE_1 + XML_LINE_2 + XML_LINE_3 + XML_LINE_4 + XML_LINE_5 + XML_LINE_6 + ">"
        + "\n  <changeSet author=\"author\" context=\"c1,c2\" id=\"id\" runInTransaction=\"false\" runOnChange=\"false\" objectQuotingStrategy=\"QUOTE_ONLY_RESERVED_WORDS\" failOnError=\"false\" runAlways=\"false\">\n"
        + "    <comment>comment</comment>\n" +
        "    <preConditions>\n" +
        "      <preCondition/>\n" +
        "    </preConditions>\n" +
        "    <createTable tableName=\"table\"/>\n" +
        "    <rollback author=\"author\" context=\"a\" id=\"id\" runInTransaction=\"true\" runOnChange=\"true\" objectQuotingStrategy=\"LEGACY\" failOnError=\"false\" runAlways=\"true\"/>\n" +
        "    <validChecksum>abc</validChecksum>\n" +
        "  </changeSet>\n" +
        ""
        + "</databaseChangeLog>", cl.get().toString());
  }

}
