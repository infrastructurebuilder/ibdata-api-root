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
package org.infrastructurebuilder.data.transform;

import java.util.List;
import java.util.Optional;

import org.infrastructurebuilder.data.IBDataTransformationError;
import org.infrastructurebuilder.data.IBSchema;
import org.infrastructurebuilder.util.config.ConfigMap;

public interface IBSchemaOutboundRowMapper<O> {

  String getOutboundType();

  IBSchemaOutboundRowMapper<O> configure(IBSchema outboundSourceSchema, ConfigMap cm);

  Optional<O> map(IBDataIntermediary row);

  List<IBDataTransformationError> getCurrentErrorsList();
}
