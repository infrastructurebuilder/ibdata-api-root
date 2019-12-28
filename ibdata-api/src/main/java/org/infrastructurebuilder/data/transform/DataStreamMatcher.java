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

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import org.infrastructurebuilder.data.IBDataStreamIdentifier;
import org.infrastructurebuilder.data.model.DataStream;

public class DataStreamMatcher extends DataStream {
  private static final long serialVersionUID = 933423703865650592L;

  boolean matches(IBDataStreamIdentifier ds) {
    return Optional.ofNullable(ds).map(d -> {
      boolean retVal = true;
      retVal &= matchString(getMimeType(), ds.getMimeType());
      retVal &= matchString(getId().toString(), Optional.ofNullable(d.getId()).map(UUID::toString).orElse(null));
      retVal &= matchDate(getCreationDate(), d.getCreationDate());
      retVal &= matchString(getName(), d.getName());
      retVal &= matchString(getDescription(), d.getDescription());
      return retVal;
    }).orElse(false);
  }

  private boolean matchDate(Date creationDate2, Date creationDate) {
    return creationDate2 == null || (creationDate.equals(creationDate2));
  }

  private boolean matchString(String name2, String name) {
    return matchString(Optional.ofNullable(name), Optional.ofNullable(name2));
  }

  private boolean matchString(Optional<String> name2, Optional<String> name) {
    boolean retVal = ( name2 == null || !name2.isPresent() || (name.equals(name2)));
    if (!retVal) {
      retVal = name2.map(pattern -> {
        return name.map(n -> n.matches(Pattern.quote(pattern)))
            // source is empty but pattern exists
            .orElse(false);
      }).orElse(true);
    }
    return retVal;
  }

}
