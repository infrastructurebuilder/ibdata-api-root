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
package test;

import static org.junit.jupiter.api.Assertions.*;

import org.infrastructurebuilder.util.constants.IBConstants;
import org.infrastructurebuilder.data.IBDataSet;
import org.infrastructurebuilder.data.IBDataStream;
//import org.infrastructurebuilder.data.InjectIBData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

// Can't do this within the reactor
//@ExtendWith(InjectIBData.class)
public class App {

  private IBDataSet workingSet;

  @BeforeEach
  //  public void setUp(IBDataSet ds) throws Exception {
  public void setUp() throws Exception {
    this.workingSet = null;
  }

  @Test
  public void test() {
    System.out.println("In the test!");
//    assertEquals(1, workingSet.asStreamsList().size());
  }

}
