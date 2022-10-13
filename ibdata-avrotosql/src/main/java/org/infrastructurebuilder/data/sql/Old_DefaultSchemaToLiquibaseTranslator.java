package org.infrastructurebuilder.data.sql;
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
//package org.infrastructurebuilder.data.sql;
//
//import java.io.IOException;
//import java.io.OutputStream;
//import java.io.PrintStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.StandardOpenOption;
//import java.sql.Connection;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import org.apache.avro.Schema;
//import org.infrastructurebuilder.IBException;
//import org.infrastructurebuilder.util.core.IBUtils;
//import org.infrastructurebuilder.util.config.ConfigMap;
//import org.infrastructurebuilder.util.config.ConfigMapSupplier;
//
//import liquibase.Liquibase;
//import liquibase.changelog.ChangeLogParameters;
//import liquibase.changelog.DatabaseChangeLog;
//import liquibase.database.DatabaseConnection;
//import liquibase.database.DatabaseFactory;
//import liquibase.exception.ChangeLogParseException;
//import liquibase.exception.LiquibaseException;
//import liquibase.parser.core.xml.XMLChangeLogSAXParser;
//import liquibase.resource.ClassLoaderResourceAccessor;
//import liquibase.resource.CompositeResourceAccessor;
//import liquibase.resource.FileSystemResourceAccessor;
//import liquibase.resource.ResourceAccessor;
//
//public class DefaultSchemaToDatabaseTranslator implements SchemaToDatabaseTranslator {
//
//  private final DatabaseFactory f = DatabaseFactory.getInstance();
//  private final LocalLiquibaseExecutor lb = null;
//  private final LiquibaseSupplier ls = null;
//
//  public final ResourceAccessor createResourceOpener(ClassLoader cl, List<Path> additionalPaths) {
//    List<ResourceAccessor> l = new ArrayList<>();
//    l.addAll(Objects.requireNonNull(additionalPaths).stream().map(Path::toAbsolutePath).map(Path::toString)
//        .map(FileSystemResourceAccessor::new).collect(Collectors.toList()));
//    l.add(new ClassLoaderResourceAccessor(Objects.requireNonNull(cl)));
//    return new CompositeResourceAccessor(l);
//  }
//
//  public final ResourceAccessor createResourceOpener(ClassLoader cl) {
//    return createResourceOpener(cl, Collections.emptyList());
//  }
//
//  public final Liquibase createLiquibase(Connection c, String changelog, ConfigMapSupplier cms, boolean dropFirst)
//      throws LiquibaseException {
//    ResourceAccessor resourceAccessor = createResourceOpener(c.getClass().getClassLoader());
//    Liquibase liquibase = ls.get();
//    liquibase.setIgnoreClasspathPrefix(true);
//    ConfigMap parametersSupplier = cms.get();
//    if (parametersSupplier != null) {
//      for (String entry : parametersSupplier.keySet()) {
//        liquibase.setChangeLogParameter(entry, parametersSupplier.getString(entry));
//      }
//    }
//
//    if (dropFirst) {
//      liquibase.dropAll();
//    }
//
//    return liquibase;
//  }
//
//  LocalLiquibaseExecutor getLiquibaseBean() {
//    return this.lb;
//  }
//
//  public final DatabaseConnection createDatabase(String url) {
//    return null;
//  };
//
//  public final Path generateChangeLog(Optional<Path> pathToPreviousChangelog, Schema t) {
//    Path retVal;
//    try {
//      retVal = Files.createTempFile("liquibase-", ".xml");
//    } catch (IOException e1) {
//      throw new IBException(e1); // FIXME Which exceptions should I actually deal with?
//    }
//    DatabaseChangeLog cl = pathToPreviousChangelog.map(m -> {
//      XMLChangeLogSAXParser x = new XMLChangeLogSAXParser();
//      ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor(getClass().getClassLoader());
//      ChangeLogParameters changeLogParameters = new ChangeLogParameters();
//      try {
//        return x.parse(m.toAbsolutePath().toString(), changeLogParameters, resourceAccessor);
//      } catch (ChangeLogParseException e) {
//        throw new IBException(e);
//      }
//    }).orElse(new DatabaseChangeLog());
//
//    Liquibase liquibase = this.ls.get();
//
//    try (OutputStream outs = Files.newOutputStream(retVal, StandardOpenOption.WRITE);
//        PrintStream printStream = new PrintStream(outs, true, IBUtils.UTF_8.name())) {
//      liquibase.generateChangeLog(catalogAndSchema, diffToChangeLog, printStream);
//
//    }
//
//    //
//    changeLogSerializer.append(changeSet, retVal.toFile());
//    //        Document d =
//    cl.addChangeSet(changeSet);
//
//    return retVal;
//  }
//
//  @Override
//  public Path apply(Optional<Path> t, Schema u) {
//    return generateChangeLog(Objects.requireNonNull(t), u);
//  }
//}
//
