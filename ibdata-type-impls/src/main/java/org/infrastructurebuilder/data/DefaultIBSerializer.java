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
//package org.infrastructurebuilder.data;
//
//import static java.nio.file.Files.newBufferedWriter;
//import static java.nio.file.StandardOpenOption.CREATE;
//import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
//import static java.util.Optional.empty;
//import static org.infrastructurebuilder.data.IBDataException.cet;
//
//import java.io.PrintWriter;
//import java.nio.file.OpenOption;
//import java.nio.file.Path;
//import java.util.Optional;
//import java.util.Properties;
//
//import javax.inject.Inject;
//import javax.inject.Named;
//
//@Named
//public class DefaultIBSerializer extends AbstractIBSerializer<String, Properties, PrintWriter> {
//
//  public final Optional<PrintWriter> serializer;
//
//  @Inject
//  public DefaultIBSerializer() {
//    this(empty(), empty());
//  }
//
//  private DefaultIBSerializer(Optional<Path> p, Optional<Properties> c) {
//    super(p, c);
//    if (getConfig().isPresent() && getPath().isPresent()) {
//      OpenOption[] options = getOpenOptionsFromConfig(getConfig().get());
//      serializer = Optional
//          .of(new PrintWriter(cet.withReturningTranslation(() -> newBufferedWriter(getPath().get(), options))));
//    } else {
//      serializer = empty();
//
//    }
//  }
//
//  private OpenOption[] getOpenOptionsFromConfig(Properties properties) {
//    // TODO Get open formats for whatever the config is
//    OpenOption[] val = new OpenOption[] { CREATE, TRUNCATE_EXISTING };
//    return val;
//  }
//
//  @Override
//  public Optional<PrintWriter> getSerializer() {
//    return serializer;
//  }
//
//  @Override
//  protected IBSerializer<String, Properties, PrintWriter> newInstance(Optional<Path> p, Optional<Properties> c) {
//    return new DefaultIBSerializer(getPath(), getConfig());
//  }
//
//}
