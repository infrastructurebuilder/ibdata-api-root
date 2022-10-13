package org.infrastructurebuilder.data.transform;

import static org.junit.Assert.*;

import org.infrastructurebuilder.util.config.TestingPathSupplier;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PassThruTransformerSupplierTest {

  public final static TestingPathSupplier wps = new TestingPathSupplier();

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    wps.finalize();
  }

  @Before
  public void setUp() throws Exception {
    p = new PassThruTransformerSupplier(wps, () -> log);

  }

  PassThruTransformerSupplier p;
  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testConfigureConfigMapSupplier() {
    p = p.withFinalizer(finalizer);
  }

}
