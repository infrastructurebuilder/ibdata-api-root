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

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.config.DefaultConfigMapSupplier;
import org.infrastructurebuilder.util.core.PathSupplier;
import org.infrastructurebuilder.util.core.TypeToExtensionMapper;

abstract public class AbstractIBDataMavenComponent {
  private final ConfigMapSupplier configMapSupplier;
  private final PathSupplier lateBoundWorkingPathSupplier;
  private final Log log;
  private final TypeToExtensionMapper t2e;
  private final Map<String, IBDataSetFinalizerSupplier<?>> allDataSetFinalizers;
  private final IBStreamerFactory streamerFactory;
  private MavenProject project;
  private MavenSession session;
  private MojoExecution mojoExecution;

  public AbstractIBDataMavenComponent(
      // Local singleton late-bound path supplier for workingPath
      final PathSupplier workingPathSupplier,
      // The Maven log
      final Log log,
      // A TypeToExtensionMapper (maybe this should be a map?)
      final TypeToExtensionMapper t2e,
      // ConfigMapSupplier (maven version, usually)
      final ConfigMapSupplier mavenCMS, Map<String, IBDataSetFinalizerSupplier<?>> allDSFinalizers,
      final IBStreamerFactory streamerFactory) {
    this.lateBoundWorkingPathSupplier = requireNonNull(workingPathSupplier);
    this.log = requireNonNull(log);
    this.t2e = requireNonNull(t2e);
    this.configMapSupplier = requireNonNull(mavenCMS);
    this.allDataSetFinalizers = requireNonNull(allDSFinalizers);
    this.streamerFactory = requireNonNull(streamerFactory);
  }

  public Optional<String> getBaseDir() throws MojoFailureException {
    return this.getProject().map(MavenProject::getBasedir).map(File::toPath).map(Path::toAbsolutePath).map(Path::toString);
  }

  public final IBDataSetFinalizer<?> getDataSetFinalizerSupplier(String key, ConfigMap config
  /* ,Optional<PathSupplier> overrideWorkingPath */ ) throws MojoFailureException {

    // overrideWorkingPath = Optional.empty(); // FIXME Remove this parameter
    // (FIXED)

    final IBDataSetFinalizerSupplier<?> supplier = ofNullable(allDataSetFinalizers.get(key))
        .orElseThrow(() -> new MojoFailureException("No finalizer name " + key));
    requireNonNull(config);
    // Little bit of a hack here
    // Optional<IBDataSetFinalizerSupplier> s2 = overrideWorkingPath.map(wp -> {
    // return Optional.of(supplier.get().forceOverrideOfWorkingPath(wp));
    // }).orElse(supplier);
    IBDataSetFinalizer<?> k = supplier
        .configure(new DefaultConfigMapSupplier(getConfigMapSupplier()).overrideConfiguration(config)).get();
    return k;

  }

  public ConfigMapSupplier getConfigMapSupplier() {

    return new DefaultConfigMapSupplier(configMapSupplier);
    // // Force the Working PAth Supplier value
    // .overrideValue(IBDATA_WORKING_PATH_SUPPLIER,
    // getWorkingPathSupplier().get().toAbsolutePath().toString());
  }

  public Log getLog() {
    return log;
  }

  public PathSupplier getWorkingPathSupplier() {
    return lateBoundWorkingPathSupplier;
  }

  public TypeToExtensionMapper getTypeToExtensionMapper() {
    return t2e;
  }

  public void setMojoExecution(MojoExecution mojoExecution) {
    this.mojoExecution = mojoExecution;
  }

  public void setProject(MavenProject project) {
    this.project = project;
  }

  public void setSession(MavenSession session) {
    this.session = session;
  }

  public Optional<MavenProject> getProject() {
    return ofNullable(project);
  }

  public Optional<MavenSession> getSession() {
    return ofNullable(session);
  }

  public Optional<MojoExecution> getMojoExecution() {
    return ofNullable(mojoExecution);
  }

  public void addConfig(String key, String value) {
    configMapSupplier.overrideValue(key, value);
  }

  public IBStreamerFactory getStreamerFactory() {
    return streamerFactory;
  }
}
