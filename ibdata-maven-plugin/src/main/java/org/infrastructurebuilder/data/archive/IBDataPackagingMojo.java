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
package org.infrastructurebuilder.data.archive;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static org.infrastructurebuilder.data.IBDataConstants.INGESTION_TARGET;
import static org.infrastructurebuilder.data.IBDataConstants.TRANSFORMATION_TARGET;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.archiver.ArchiveFinalizer;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.infrastructurebuilder.data.AbstractIBDataMojo;
import org.infrastructurebuilder.data.archiver.DefaultIBDataMavenArchiveFinalizer;
import org.infrastructurebuilder.data.archiver.IBDataLateBindingFinalizerConfigSupplier;
import org.infrastructurebuilder.util.files.IBChecksumPathType;
import org.infrastructurebuilder.util.logging.SLF4JFromMavenLogger;
import org.infrastructurebuilder.util.readdetect.IBResource;

/**
 * This is a transplant from the existing jar plugin for packaging.
 * Many of the same javadoc comments have been left  in place
 *
 * @author mykel.alvis
 *
 */
@Mojo(name = "package", defaultPhase = LifecyclePhase.PACKAGE, requiresProject = true)
public class IBDataPackagingMojo extends AbstractIBDataMojo {

  /**
   * The archive configuration to use. See
   * <a href="http://maven.apache.org/shared/maven-theArchiver/index.html">Maven
   * Archiver Reference</a>.
   */
  @Parameter
  private final MavenArchiveConfiguration archive = new MavenArchiveConfiguration();

  @Component(hint = DefaultIBDataMavenArchiveFinalizer.NAME)
  private ArchiveFinalizer archiveFinalizer;

  /**
   * Classifier to add to the artifact generated. If given, the artifact will
   * be attached as a supplemental artifact. If not given this will create the
   * main artifact which is the default behavior. If you try to do that a
   * second time without using a classifier the build will fail.
   */
  @Parameter(property = "maven.jar.classifier")
  private String classifier;

  @Component
  IBDataPackageMavenComponent component;

  /**
   * Path to the default MANIFEST file to use. It will be used if
   * <code>useDefaultManifestFile</code> is set to <code>true</code>.
   *
   * @since 2.2
   */
  // CHECKSTYLE_OFF: LineLength
  @Parameter(defaultValue = "${project.build.outputDirectory}/META-INF/MANIFEST.MF", required = true, readonly = true)
  // CHECKSTYLE_ON: LineLength
  private File defaultManifestFile;

  /**
   * Name of the generated JAR.<br/>
   * Starting with <b>3.0.0</b> the property has been renamed from
   * <code>jar.finalName</code> to <code>maven.jar.finalName</code>.
   */
  @Parameter(property = "maven.ibdata.finalName", defaultValue = "${project.build.finalName}")
  private String finalName;

  /**
   * Require the jar plugin to build a new JAR even if none of the contents
   * appear to have changed. By default, this plugin looks to see if the
   * output jar exists and inputs have not changed. If these conditions are
   * true, the plugin skips creation of the jar. This does not work when other
   * plugins, like the maven-shade-plugin, are configured to post-process the
   * jar. This plugin can not detect the post-processing, and so leaves the
   * post-processed jar in place. This can lead to failures when those plugins
   * do not expect to find their own output as an input. Set this parameter to
   * <tt>true</tt> to avoid these problems by forcing this plugin to recreate
   * the jar every time.<br/>
   * Starting with <b>3.0.0</b> the property has been renamed from
   * <code>jar.forceCreation</code> to <code>maven.jar.forceCreation</code>.
   */
  @Parameter(property = "maven.jar.forceCreation", defaultValue = "false")
  private boolean forceCreation;

  /**
   * Directory containing the generated JAR.
   */
  @Parameter(defaultValue = "${project.build.directory}", required = true)
  private File outputDirectory;

  @Parameter(defaultValue = "${session}", readonly = true, required = true)
  private MavenSession session;

  /**
   * Skip creating empty archives.<br/>
   * Starting with <b>3.0.0</b> the property has been renamed from
   * <code>jar.skipIfEmpty</code> to <code>maven.jar.skipIfEmpty</code>.
   */
  @Parameter(property = "maven.jar.skipIfEmpty", defaultValue = "false")
  private boolean skipIfEmpty;

  @Component(role = Archiver.class, hint = "ibdata")
  private JarArchiver theArchiver;

  /**
   * Set this to <code>true</code> to enable the use of the
   * <code>defaultManifestFile</code>.<br/>
   * Starting with <b>3.0.0</b> the property has been renamed from
   * <code>jar.useDefaultManifestFile</code> to
   * <code>maven.jar.useDefaultManifestFile</code>.
   *
   * @since 2.2
   */
  @Parameter(property = "maven.jar.useDefaultManifestFile", defaultValue = "false")
  private boolean useDefaultManifestFile;

  public File createArchive(final IBResource context) throws MojoExecutionException {
    final File jarFile = getJarFile(outputDirectory, finalName, getClassifier());
    final MavenArchiver archiver = new MavenArchiver();

    final IBDataLateBindingFinalizerConfigSupplier lbconfig = new IBDataLateBindingFinalizerConfigSupplier();
    lbconfig.setT(context);
    theArchiver.addArchiveFinalizer(
        new DefaultIBDataMavenArchiveFinalizer(() -> new SLF4JFromMavenLogger(getLog()), lbconfig));

    archiver.setArchiver(theArchiver);
    archiver.setOutputFile(jarFile);
    archive.setForced(forceCreation);

    try {
      if (Optional.ofNullable(context.getPath())
          .map(f -> Files.exists(f, NOFOLLOW_LINKS) && Files.isDirectory(f, NOFOLLOW_LINKS)).orElse(false)) {
      } else {
        getLog().warn("JAR will be empty - no content was marked for inclusion!");
      }

      final File existingManifest = getDefaultManifestFile();

      if (useDefaultManifestFile && existingManifest.exists() && archive.getManifestFile() == null) {
        getLog().info("Adding existing MANIFEST to archive. Found under: " + existingManifest.getPath());
        archive.setManifestFile(existingManifest);
      }

      archiver.createArchive(session, getProject(), archive);

      return jarFile;
    } catch (final Exception e) {
      throw new MojoExecutionException("Error assembling JAR", e);
    }
  }

  public File getDefaultManifestFile() {
    return defaultManifestFile;
  }

  private String getClassifier() {
    return "";
  }

  @Override
  protected void _execute() throws MojoExecutionException, MojoFailureException {
    getLog().info("Executing Packaging");
    final Map pc = getPluginContext();

    if (!pc.containsKey(INGESTION_TARGET) && !pc.containsKey(TRANSFORMATION_TARGET))
      throw new MojoExecutionException("Target does not exist");
    if (pc.containsKey(INGESTION_TARGET) && pc.containsKey(TRANSFORMATION_TARGET))
      throw new MojoExecutionException("Cannot package ingestion and transformation at the same time");
    final IBResource context = (IBResource) pc.getOrDefault(INGESTION_TARGET,
        pc.get(TRANSFORMATION_TARGET));
    final Path target = context.getPath();
    getLog().debug("Context payload acquired " + target.toString());
    try {
      if (skipIfEmpty
          && (!Files.exists(target, NOFOLLOW_LINKS) || Files.list(target).collect(Collectors.toList()).size() < 1)) {
        getLog().info("Skipping packaging of the IBData Archive");
      } else {
        final File jarFile = createArchive(context);
        final File ftest = getProject().getArtifact().getFile();
        if (ftest != null && ftest.isFile())
          throw new MojoExecutionException("You have to use a classifier "
              + "to attach supplemental artifacts to the project instead of replacing them.");
        getLog().debug("Setting project artifact to " + jarFile);
        getProject().getArtifact().setFile(jarFile);
      }
    } catch (final IOException e) {
      throw new MojoExecutionException("Something went wrong in execute()", e);
    }
    pc.remove(INGESTION_TARGET);
    pc.remove(TRANSFORMATION_TARGET);

  }

  //  @Override
  //  protected void _setup() throws MojoFailureException {
  //    super._setup();
  //  }

  /**
   * Returns the Jar file to generate, based on an optional classifier.
   *
   * @param basedir
   *            the output directory
   * @param finalName
   *            the name of the ear file
   * @param classifier
   *            an optional classifier
   * @return the file to generate
   */
  protected File getJarFile(final File basedir, final String finalName, final String classifier) {
    if (basedir == null)
      throw new IllegalArgumentException("basedir is not allowed to be null");
    if (finalName == null)
      throw new IllegalArgumentException("finalName is not allowed to be null");

    final StringBuilder fileName = new StringBuilder(finalName);

    if (hasClassifier()) {
      fileName.append("-").append(classifier);
    }

    fileName.append(".jar");

    return new File(basedir, fileName.toString());
  }

  /**
   * @return true in case where the classifier is not {@code null} and
   *         contains something else than white spaces.
   */
  protected boolean hasClassifier() {
    boolean result = false;
    if (getClassifier() != null && getClassifier().trim().length() > 0) {
      result = true;
    }

    return result;
  }

  protected IBDataPackageMavenComponent getComponent() {
    return component;
  }
}
