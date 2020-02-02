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
package org.infrastructurebuilder.data.type;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.infrastructurebuilder.data.IBField;
import org.infrastructurebuilder.data.model.SchemaField;
import org.infrastructurebuilder.util.config.IBRuntimeUtilsTesting;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultIBDataTypeTranslationFactoryTest {

  private static final String VARCHAR = "varchar";
  public final static Logger log = LoggerFactory.getLogger(DefaultIBDataTypeTranslationFactoryTest.class);
  public final static IBRuntimeUtilsTesting ibr = new IBRuntimeUtilsTesting(log);

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  private List<IBDataTypeTranslator<?>> map;
  private IBDataTypeTranslator<?> a, b, c;
  private DefaultIBDataTypeHandlerFactory f;
  private List<IBDataType> types;

  @Before
  public void setUp() throws Exception {
    IBDataType x = new StringIBDataType(), y = new StringIBDataType(), z = new DateIBDataType();
    types = Arrays.asList(x, y, z);
    a = new AType();
    b = new BType();
    c = new CType();
    map = Arrays.asList(a, b, c);
    f = new DefaultIBDataTypeHandlerFactory(ibr, types, map);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetTranslatorFor() {
    Optional<IBDataTypeTranslator<?>> t = f.getTranslatorFor(VARCHAR);
    assertTrue(t.isPresent());
  }

  private class FakeIBDataType extends IBDataType {

    public FakeIBDataType(String type) {
      super(type);
    }

  }

  private class AType extends AbstractIBDataTypeTranslator<String> {

    public AType() {
      super(VARCHAR, ibr);
    }

    @Override
    public IBField to(String in) {
      SchemaField f = new SchemaField();
      f.setType(StringIBDataType.TYPE);
      return f;
    }

    @Override
    public String from(IBField out) {
      return out.getType();
    }
  }

  private class BType extends AType {
    public BType() {
      super();
    }

    @Override
    public Integer getWeight() {
      return 2;
    }
  }

  private class CType extends AbstractIBDataTypeTranslator<Date> {
    public CType() {
      super("date", ibr);
    }

    @Override
    public IBField to(Date in) {
      SchemaField f = new SchemaField();
      f.setType(DateIBDataType.TYPE);
      return f;
    }

    @Override
    public Date from(IBField out) {
      return new Date();
    }
  }
}
