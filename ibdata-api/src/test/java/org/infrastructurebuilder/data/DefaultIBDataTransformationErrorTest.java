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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class DefaultIBDataTransformationErrorTest {

  private static final String MESSAGE = "message";
  private Throwable r = new RuntimeException();
  private String message = MESSAGE;
  private IBDataTransformationError v;

  @Before
  public void setUp() throws Exception {
    v = new DefaultIBDataTransformationError(Optional.of(r), Optional.of(message));
  }

  @Test
  public void testDefaultIBDataTransformationError() {
    DefaultIBDataTransformationError k = new DefaultIBDataTransformationError();
    assertFalse(k.getError().isPresent());
    assertFalse(k.getMessage().isPresent());
  }

  @Test
  public void testDefaultIBDataTransformationErrorOptionalOfThrowableOptionalOfString() {

  }

  @Test
  public void testGetMessage() {
    assertEquals(MESSAGE, v.getMessage().get());
  }

  @Test
  public void testGetError() {
    assertTrue(v.getError().get().getClass().equals(r.getClass()));
  }

}
