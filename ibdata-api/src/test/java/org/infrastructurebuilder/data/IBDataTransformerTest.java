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

import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.infrastructurebuilder.data.transform.Transformer;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.PathSupplier;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IBDataTransformerTest {

  public final static Logger log = LoggerFactory.getLogger(IBDataTransformerTest.class);
  private IBDataTransformer i;

  @Before
  public void setUp() throws Exception {
    i = new IBDataTransformer() {

      @Override
      public Logger getLog() {
        return log;
      }

      @Override
      public IBDataTransformationResult transform(Transformer t, IBDataSet ds, List<IBDataStream> suppliedStreams,
          boolean failOnError) {
        return new IBDataTransformationResult() {
          @Override
          public Optional<IBDataSet> get() {
            return Optional.empty();
          }

          @Override
          public PathSupplier getWorkingPathSupplier() {
            return () -> Paths.get(".");
          }

          @Override
          public List<IBDataTransformationError> getErrors() {
            return Collections.emptyList();
          }
        };
      }

      @Override
      public String getHint() {
        return "dummy";
      }
    };
  }

  @Test
  public void test() {
    assertFalse(i.respondsTo(null));
  }

}