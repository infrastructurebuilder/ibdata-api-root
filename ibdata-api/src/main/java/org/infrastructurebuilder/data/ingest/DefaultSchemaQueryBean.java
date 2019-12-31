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
package org.infrastructurebuilder.data.ingest;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class DefaultSchemaQueryBean implements Supplier<Optional<UUID>> {

  private String byUUID = null;
  private String byLookup = null;

  public void setByLookup(String byLookup) {
    this.byLookup = byLookup;
  }

  public void setByUUID(String byUUID) {
    this.byUUID = byUUID;
  }

  @Override
  public Optional<UUID> get() {
    return Optional.ofNullable(getUUID()).map(UUID::fromString);
  }

  private String getUUID() {
    // FIXME Fix the query lookup. This is definitely not OK
    return this.byUUID;
  }

}
