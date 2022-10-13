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
package org.infrastructurebuilder.data.mssql.jtds;

import static org.junit.Assert.*;

import org.infrastructurebuilder.data.mssql.jtds.MSSQLJTDSDatabaseDriverSupplier;
import org.jooq.SQLDialect;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MSSQLJTDSDatabaseDriverSupplierTest {
  public final static Logger log = LoggerFactory.getLogger(MSSQLJTDSDatabaseDriverSupplier.class);

  @Test
  public void testDatabaseDriverSupplier() {
    MSSQLJTDSDatabaseDriverSupplier q = new MSSQLJTDSDatabaseDriverSupplier(() -> log);
    assertNotNull(q);
    assertEquals("MSSQL-JTDS", q.getHint());
    assertEquals(SQLDialect.SQLSERVER.name(), q.getJooqName());
  }

}
