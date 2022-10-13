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
///**
// * Copyright © 2019 admin (admin@infrastructurebuilder.org)
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package org.infrastructurebuilder.data.transform.line;
//
//import java.nio.file.Path;
//import java.util.Map;
//
//import javax.inject.Named;
//
//import org.infrastructurebuilder.data.transform.line.IBDataRecordTransformer;
//import org.infrastructurebuilder.util.PropertiesSupplier;
//import org.infrastructurebuilder.util.config.ConfigMapSupplier;
//import org.infrastructurebuilder.util.config.SingletonLateBindingPathSupplier;
//import org.infrastructurebuilder.util.config.WorkingPathSupplier;
//
//@Named(ComplexArraySplitIBDataLineTransformerSupplier.COMPLEX_ARRAY_SPLIT)
//public class ComplexArraySplitIBDataLineTransformerSupplier
//    extends AbstractIBDataRecordTransformerSupplier<String, String[]> {
//  public static final String COMPLEX_ARRAY_SPLIT = "complex.array.split";
//
//  public ComplexArraySplitIBDataLineTransformerSupplier(
//      @Named(IBDATA_WORKING_PATH_SUPPLIER) WorkingPathSupplier wps,
//      @Named(ConfigMapSupplier.MAVEN) ConfigMapSupplier cms) {
//    super(wps, cms);
//  }
//
//  @Override
//  protected IBDataRecordTransformer<String, String[]> getUnconfiguredTransformerInstance(Path workingPath) {
//    return new ComplexArraySplitIBDataLineTransformer() {
//
//      @Override
//      public String[] apply(String t) {
//        return abstractApply()
//      }
//    };
//  }
//
//
//
//  abstract private class ComplexArraySplitIBDataLineTransformer extends AbstractIBDataRecordTransformer<String, String[]> {
//    private IBDataRecordTransformer<String, String[]> supplier;
//
//
//    /**
//     * @param ps
//     * @param config
//     */
//    public ComplexArraySplitIBDataLineTransformer(Path ps, Map<String, String> config) {
//      super(ps, config);
//    }
//
//    /**
//     * @param ps
//     */
//    public ComplexArraySplitIBDataLineTransformer(Path ps) {
//      super(ps);
//    }
//
//    @Override
//    public String getHint() {
//      return COMPLEX_ARRAY_SPLIT;
//    }
//  }
//
//
//
//}