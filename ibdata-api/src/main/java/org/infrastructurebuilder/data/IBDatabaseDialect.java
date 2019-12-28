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

import java.util.Optional;
import java.util.function.Supplier;

/**
 * A class for encapsulating Jooq dialects and possibly other types
 * The Supplier provides a reference for the name of the IBDataDatabaseDialect instance,
 * which should be loadable via DI
 * @author mykel.alvis
 *
 * Translation mapping for various tools used within IBData
 *
 * @author mykel.alvis
 *
 */
public interface IBDatabaseDialect extends Supplier<String> {
  Optional<String> hibernateDialectClass();

  String liquibaseDatabaseClass();

  Optional<String> springDbName();
}
