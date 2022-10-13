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
package org.infrastructurebuilder.data.schema.liquibase;

import static java.util.Optional.ofNullable;

import java.util.List;
import java.util.Optional;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.util.core.Checksum;
import org.infrastructurebuilder.util.core.ChecksumBuilder;

public class IBDataLiquibaseChangeSet extends AbstractIBDataLB {
  public static final String CHANGE_SET = "changeSet";
  private static final long serialVersionUID = 8585631038830491760L;

  public IBDataLiquibaseChangeSet(String id, String author, String dbms, Boolean runAlways, Boolean runOnChange,
      Boolean runInTransaction, List<String> context, Boolean failOnError, LiquibaseQuotingStrategies objectQuotingStrategy) {
    this(CHANGE_SET, id, author, dbms, runAlways, runOnChange, runInTransaction, context, failOnError,
        objectQuotingStrategy);

  }

  protected IBDataLiquibaseChangeSet(String tag, String id, String author, String dbms, Boolean runAlways,
      Boolean runOnChange, Boolean runInTransaction, List<String> context, Boolean failOnError,
      LiquibaseQuotingStrategies objectQuotingStrategy) {
    super(tag);
    setRequiredAttribute("id", id);
    setRequiredAttribute("author", author);
    setAttribute("runAlways", runAlways);
    setAttribute("runOnChange", runOnChange);
    setAttribute("runInTransaction", runInTransaction);
    setAttribute("failOnError", failOnError);
    setAttribute("context", context);
    ofNullable(objectQuotingStrategy).ifPresent(c -> setAttribute("objectQuotingStrategy", c.name()));

  }

  protected String getTag() {
    return CHANGE_SET;
  }
  public IBDataLiquibaseChangeSet(Xpp3Dom src) {
    super(src);
    if (!getTag().equals(src.getName()))
      throw new IBDataException("Name must be " + getTag());
  }

  @Override
  public Checksum asChecksum() {
    return ChecksumBuilder.newInstance().addString(toString()).asChecksum();
  }

  public IBDataLiquibaseChangeSet addComment(String comment) {
    IBLBInternal x = getInternal("comment");
    x.setValue(comment);
    addChild(x.get());
    return this;
  }

  public IBDataLiquibaseChangeSet addPreCondition(Xpp3Dom preCondition) {
    Xpp3Dom x = getOrCreateChild("preConditions");
    x.addChild(preCondition);
    return this;
  }

  public IBDataLiquibaseChangeSet addRefactoring(IBDataLiquibaseRefactoring refactoring) {
    addChild(refactoring.get());
    return this;
  }

  public IBDataLiquibaseChangeSet addValidChecksum(Xpp3Dom validChecksum) {
    addChild(validChecksum);
    return this;
  }

  public IBDataLiquibaseChangeSet addRollback(IBDataLiquibaseRollback rollback) {
    addChild(rollback);
    return this;
  }

  private Xpp3Dom getOrCreateChild(String child) {
    return Optional.ofNullable(getChild(child)).orElseGet(() -> {
      Xpp3Dom xpp3Dom = new Xpp3Dom(child);
      addChild(xpp3Dom);
      return xpp3Dom;
    });
  }
}
