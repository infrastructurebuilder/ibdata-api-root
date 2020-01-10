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
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class IBDataStreamRecordFinalizerTest {

  private IBDataStreamRecordFinalizer<Object> i;

  @Before
  public void setUp() throws Exception {
    i = new IBDataStreamRecordFinalizer<Object>() {

      @Override
      public InputStream get() {
        return null;
      }

      @Override
      public int getNumberOfRowsToSkip() {
        return IBDataStreamRecordFinalizer.super.getNumberOfRowsToSkip();
      }
      @Override
      public void close() throws Exception {

      }

      @Override
      public String getId() {
        return null;
      }

      @Override
      public Optional<IBDataTransformationError> writeRecord(Object recordToWrite) {
        return null;
      }

      @Override
      public OutputStream getWriterTarget() throws IOException {
        return null;
      }

      @Override
      public IBDataStreamSupplier finalizeRecord(IBDataStreamIdentifier ds) {
        return () -> null;
      }

      @Override
      public Path getWorkingPath() {
        return null;
      }
    };
  }

  @Test
  public void testSkipRows() {
    assertEquals(0,i.getNumberOfRowsToSkip());
  }

  @Test
  public void testProduces() {
    assertFalse(i.produces().isPresent());
  }
  @Test
  public void testAccepts() {
    assertFalse(i.accepts().isPresent());
  }
  @Test
  public void testGet() {
    assertNull(i.get());
  }

}
