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

import static java.util.Optional.empty;
import static org.infrastructurebuilder.util.constants.IBConstants.TEXT_CSV;
import static org.infrastructurebuilder.util.constants.IBConstants.TEXT_PLAIN;
import static org.infrastructurebuilder.util.constants.IBConstants.TEXT_PSV;
import static org.infrastructurebuilder.util.constants.IBConstants.TEXT_TSV;
import static org.infrastructurebuilder.util.core.IBUtils.readInputStreamAsStringStream;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Named;

import org.infrastructurebuilder.util.core.LoggerSupplier;
import org.slf4j.Logger;

@Named
public class DefaultStringStreamSupplier implements IBDataSpecificStreamFactory<String> {
  public final static List<String> TYPES = Arrays.asList(TEXT_CSV, TEXT_PSV, TEXT_TSV, TEXT_PLAIN);
  private final Logger log;
  private final List<InputStream> insList = new ArrayList<>();

  @Inject
  public DefaultStringStreamSupplier(LoggerSupplier l) {
    this.log = l.get();
  }

  @Override
  public Optional<Stream<String>> from(IBDataStream ds) {
    if (ds == null || !getRespondTypes().contains(ds.getMimeType()))
      return empty();
    return Optional.of(readInputStreamAsStringStream(ds.get()));
  }

  @Override
  protected void finalize() throws Throwable {
    super.finalize();
    for (InputStream ins : insList)
      ins.close();
  }

  @Override
  public List<String> getRespondTypes() {
    return TYPES;
  }

}
