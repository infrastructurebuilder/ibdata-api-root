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

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import org.infrastructurebuilder.util.IBUtils;
import org.infrastructurebuilder.util.config.TestingPathSupplier;
import org.junit.Before;
import org.junit.Test;

public class IBSerializerTest {

  public final TestingPathSupplier wps = new TestingPathSupplier();
  private IBSerializer<Object, Object, InputStream> i;

  @Before
  public void setUp() throws Exception {
    Path p1 = wps.get();
    Path p3 = wps.getTestClasses().resolve(IBMetadataUtilsTest.TEST_INPUT_0_11_XML_WO_SLASH);
    Path p = p1.resolve(UUID.randomUUID().toString());
    IBUtils.copy(p3, p);

    i = new IBSerializer<Object, Object, InputStream>() {

      @Override
      public IBSerializer<Object, Object, InputStream> toPath(Path p) {
        return null;
      }

      @Override
      public IBSerializer<Object, Object, InputStream> withSerializationConfiguration(Object c) {
        return null;
      }

      @Override
      public Optional<InputStream> getSerializer() {
        return Optional.of(IBDataException.cet.withReturningTranslation(() -> Files.newInputStream(p)));
      }
    };
  }

  @Test
  public void testClose() throws Exception {
    i.close();
  }

}
