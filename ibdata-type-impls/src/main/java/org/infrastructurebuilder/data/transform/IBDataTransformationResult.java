/*
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
package org.infrastructurebuilder.data.transform;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.infrastructurebuilder.data.IBDataSet;
import org.infrastructurebuilder.util.core.PathSupplier;

/**
 *
 * Supplies the result of a transformation.
 * If the supplied Optional IBDataSet is empty, then the transformation was not successful.
 * If the transformation was not successful, then the errors should be available from getErrors()
 *
 * If the transformation <b>was</b> successful, then the supplied IBDataSet is the result, and should
 * be passed on to any subsequent transformations.  Any new streams in the new IBDataSet are available for
 * additional transformation.
 *
 * @author mykel.alvis
 *
 * @param <T>
 */
public interface IBDataTransformationResult extends Supplier<Optional<IBDataSet>>{
  List<Throwable> getErrors();
  PathSupplier getWorkingPathSupplier();
}
