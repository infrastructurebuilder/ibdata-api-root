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
package org.infrastructurebuilder.data.schema.liquibase;

import java.util.List;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.util.artifacts.ChecksumEnabled;

public class IBDataLiquibaseRollback extends IBDataLiquibaseChangeSet implements ChecksumEnabled {
  public static final String ROLLBACK = "rollback";
  private static final long serialVersionUID = 8585631038830491760L;

  public IBDataLiquibaseRollback(String id, String author, String dbms, Boolean runAlways, Boolean runOnChange,
      Boolean runInTransaction, List<String> context, Boolean failOnError,
      LiquibaseQuotingStrategies objectQuotingStrategy) {
    super(ROLLBACK, id, author, dbms, runAlways, runOnChange, runInTransaction, context, failOnError,
        objectQuotingStrategy);
  }

  public IBDataLiquibaseRollback(Xpp3Dom src) {
    super(src);
  }

  public IBDataLiquibaseRollback addComment(String comment) {
    return (IBDataLiquibaseRollback) super.addComment(comment);
  }

  protected String getTag() {
    return ROLLBACK;
  };

}
