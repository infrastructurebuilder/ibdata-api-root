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
import static java.util.stream.Collectors.joining;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.infrastructurebuilder.util.core.Checksum;
import org.infrastructurebuilder.util.core.ChecksumBuilder;
import org.infrastructurebuilder.util.core.ChecksumEnabled;

abstract public class AbstractIBDataLB extends Xpp3Dom implements Supplier<Xpp3Dom>, ChecksumEnabled {

  /**
   *
   */
  private static final long serialVersionUID = 1584676988574626120L;

  public AbstractIBDataLB(String name) {
    super(name);
  }

  public AbstractIBDataLB(Xpp3Dom src) {
    super(src);
  }

  @Override
  public Xpp3Dom get() {
    return this;
  }

  public void setAttribute(String name, String value) {
    ofNullable(value).ifPresent(c -> super.setAttribute(name, c));
  }

  public void setAttribute(String name, List<String> value) {
    ofNullable(value).ifPresent(c -> setAttribute(name, c.stream().collect(joining(","))));
  }

  public void setAttribute(String name, Object value) {
    ofNullable(value).ifPresent(c -> setAttribute(name, c.toString()));
  }

  public void setRequiredAttribute(String name, String value) {
    this.setAttribute(name, Objects.requireNonNull(value, "attribute " + name + " required"));
  }

  @Override
  public Checksum asChecksum() {
    return ChecksumBuilder.newInstance().addString(toString()).asChecksum();
  }

  public IBLBInternal getInternal(String name) {
    return new IBLBInternal(name);
  }

  public class IBLBInternal extends AbstractIBDataLB {

    /**
     *
     */
    private static final long serialVersionUID = -1632062627660026946L;

    public IBLBInternal(String name) {
      super(Objects.requireNonNull(name));
    }

  }

}