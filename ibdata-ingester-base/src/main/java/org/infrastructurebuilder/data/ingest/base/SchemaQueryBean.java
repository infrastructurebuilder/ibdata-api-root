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
package org.infrastructurebuilder.data.ingest.base;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import org.infrastructurebuilder.data.IBDataException;

public class SchemaQueryBean implements Supplier<List<UUID>> {

  private static final String ERROR = "Either uuid or lookup. Not both.";
  private String byUUID = null;
  private String byLookup = null;

  public SchemaQueryBean() {
  }
  private SchemaQueryBean(String byUUID2, String byLookup2) {
    this.byLookup = byLookup2;
    this.byUUID = byUUID2;
  }

  public void setByLookup(String byLookup) {
    if (byUUID != null)
      throw new IBDataException(ERROR);
    this.byLookup = byLookup;
  }

  public void setByUUID(String byUUID) {
    if (byLookup != null)
      throw new IBDataException(ERROR);
    this.byUUID = byUUID;
  }

  @Override
  public List<UUID> get() {
    return Arrays.asList(UUID.fromString(getUUID()));
  }

  private String getUUID() {
    // FIXME Fix the query lookup. This is definitely not OK
    return this.byUUID;
  }

  public SchemaQueryBean copy() {
    return new SchemaQueryBean(byUUID, byLookup);
  }

}
