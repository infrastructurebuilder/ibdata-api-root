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
package org.infrastructurebuilder.data;

import static org.infrastructurebuilder.data.IBDataException.cet;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Currently unused. Could be a consistent interface with serializers
 * @author mykel.alvis
 *
 * @param <T>
 * @param <C>
 * @param <S> The type of serializer that this returns.
 */
public interface IBSerializer<T, C, S extends AutoCloseable> extends AutoCloseable {

  IBSerializer<T, C, S> toPath(Path p);

  IBSerializer<T, C, S> withSerializationConfiguration(C c);

  Optional<S> getSerializer();

  @Override
  default void close() throws Exception {
    getSerializer().ifPresent(e -> cet.withTranslation(() -> e.close()));
  }

}
