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

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.CREATE;
import static org.infrastructurebuilder.data.IBDataConstants.IBDATA_DOWNLOAD_CACHE_DIR_SUPPLIER;
import static org.infrastructurebuilder.data.IBDataConstants.IBDATA_WORKING_DIRECTORY;
import static org.infrastructurebuilder.data.IBDataConstants.IBDATA_WORKING_PATH_SUPPLIER;
import static org.infrastructurebuilder.data.IBDataConstants.MARKER_FILE;

import java.io.File;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.infrastructurebuilder.util.readdetect.model.IBResourceModel;
import org.infrastructurebuilder.util.readdetect.model.io.xpp3.IBResourceModelXpp3WriterEx;

public abstract class AbstractIBDataMojo extends AbstractMojo {

  /**
   * Before we can do any
   */
  @Component(hint = IBDATA_WORKING_PATH_SUPPLIER)
  private IBDataWorkingPathSupplier workingPathSupplier;

  @Component(hint = IBDATA_DOWNLOAD_CACHE_DIR_SUPPLIER)
  private IBDataCacheDirectoryPathSupplier cacheDirSupplier;

  @Parameter(defaultValue = "${mojoExecution}", readonly = true)
  private MojoExecution mojo;

  @Parameter(required = true, readonly = true, defaultValue = "${project}")
  private MavenProject project;

  @Parameter
  private List<String> requiredMetadata;

  @Parameter(defaultValue = "${session}", readonly = true)
  private MavenSession session;

  @Parameter(required = false, defaultValue = "false")
  private boolean skip;

  @Parameter(required = true, defaultValue = "${basedir}", readonly = true)
  private File basedir;

  @Parameter(property = IBDATA_WORKING_DIRECTORY, defaultValue = "${project.build.directory}/ibdata")
  private File workingDirectory;

  @Component
  private IBDataEngine engine;

  @Component
  private IBStreamerFactory streamerFactory;

  public IBStreamerFactory getStreamerFactory() {
    return streamerFactory;
  }

  private OpenOption[] options = { StandardOpenOption.CREATE, StandardOpenOption.WRITE };

  public IBDataEngine getEngine() {
    return engine;
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    _setup();
    if (!skip) {
      getLog().info("About to execute " + getMojo().getGoal());
      _execute();
    } else {
      getLog().warn("Skipping...");
    }
  }
  //
  //  @Override
  //  public IBDataEngine getIBDataEngineInstance() throws MojoFailureException {
  //    // Get the data engine from the injected map of IBDataEngineSuppliers or throw an IBDataException
  //    return ofNullable(dataEngine).flatMap(e -> ofNullable(dataEngines.get(e)))
  //        .orElseThrow(() -> new MojoFailureException("No supplier for data engine " + dataEngine)).get();
  //  }

  public MojoExecution getMojo() {
    return mojo;
  }

  protected MavenProject getProject() {
    return project;
  }

  public MavenSession getSession() {
    return session;
  }

  public Path getWorkingDirectory() {
    return this.workingDirectory.toPath();
  }

  protected abstract AbstractIBDataMavenComponent getComponent();

  protected abstract void _execute() throws MojoExecutionException, MojoFailureException;

  protected void _setup() throws MojoFailureException {
    IBDataException.cet.translate(() -> Files.createDirectories(workingDirectory.toPath()));
    workingPathSupplier.setPath(workingDirectory.toPath()); // workingPathSupplier is a Singleton
    if (getSession() != null) {
      cacheDirSupplier.setPath(Paths.get(getSession().getLocalRepository().getBasedir()).resolve(".cache")
          .resolve("download-maven-plugin").toAbsolutePath());
    }
    getComponent().setMojoExecution(getMojo());
    getComponent().setProject(getProject());
    getComponent().setSession(getSession());

  }

  protected Path writeMarker(IBResourceModel ds) throws MojoFailureException {
    Path p = Paths.get(getProject().getBuild().getDirectory()).resolve(MARKER_FILE).toAbsolutePath();
    if (Files.exists(p))
      getLog().warn("Existing marker file");
    try (Writer writer = Files.newBufferedWriter(p, UTF_8, CREATE)) {
      new IBResourceModelXpp3WriterEx().write(writer, ds);
      return p;
    } catch (Exception e) { // Catch anything and translate it to an IBDataException
      throw new MojoFailureException("Failed to write marker to " + getWorkingDirectory(), e);
    }
  }

}
