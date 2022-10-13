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

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableMap;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.jooq.SQLDialect;

public final class IBDataDatabaseUtils {
  private static final Map<String, SQLDialect> dialects = unmodifiableMap(
      asList(SQLDialect.values()).stream().collect(toMap(SQLDialect::name, identity())));
//  private static final Map<String, LBDDatabase> liquibaseDatabaseClass = unmodifiableMap(
//      asList(LBDDatabase.values()).stream().collect(toMap(LBDDatabase::name, identity())));

  public static Set<SQLDialect> allDialects() {
    return Collections.unmodifiableSet(asList(SQLDialect.values()).stream().collect(toSet()));
  }

//  public static Set<LBDDatabase> allLBDialects() {
//    return Collections.unmodifiableSet(asList(LBDDatabase.values()).stream().collect(toSet()));
//  }
//
  public static Optional<SQLDialect> bySQLDialectName(String name) {
    return ofNullable(dialects.get(name));
  }

//  public static Optional<LBDDatabase> byLiquibaseDatabaseName(SQLDialect dialect) {
//    return ofNullable(liquibaseDatabaseClass.get(dialect.name()));
//  }

}
