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

import static org.infrastructurebuilder.data.IBDataConstants.INGESTION_TARGET;
import static org.infrastructurebuilder.data.IBDataConstants.TRANSFORMATION_TARGET;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.infrastructurebuilder.data.AbstractIBDataMojo;
import org.infrastructurebuilder.data.IBDataModelUtils;
import org.infrastructurebuilder.util.files.IBChecksumPathType;

@Mojo(name = "transform", defaultPhase = LifecyclePhase.PROCESS_SOURCES, requiresProject = true, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class IBDataTransformMojo extends AbstractIBDataMojo {

  @Parameter(required = true)
  private List<Transformation> transformations = new ArrayList<>();

  @Parameter(required = false, property = "ibdata.injectDependencies")
  private boolean injectDependencies = false;

  @Component
  IBDataTransformMavenComponent component;

  @Override
  protected IBDataTransformMavenComponent getComponent() {
    return component;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void _execute() throws MojoExecutionException, MojoFailureException {
    List<URL> l = new ArrayList<>();
    if (this.injectDependencies) {
      try {
        List<Path> myList = (List<Path>) this.getProject().getCompileClasspathElements().stream().map(x -> Paths.get(x.toString())).collect(Collectors.toList());
        for (Path ff : myList) {
          l.add(ff.toUri().toURL());
        }
      } catch (MalformedURLException | DependencyResolutionRequiredException e) {
        throw new MojoFailureException("Failed to acquire additional URLS", e);
      }
    }
    component.setAdditionalURLS(l);
    // Note: component.transform translates all IBDataExecptions into MojoFailureExceptions
    IBChecksumPathType theResult = component.transform(transformations);
    getLog().debug("Setting plugin context");
    @SuppressWarnings("rawtypes")
    final Map pc = getPluginContext();
    if (pc.containsKey(INGESTION_TARGET))
      throw new MojoFailureException("Transformation and Ingestion cannot be performed in the same module build.");
    setPluginContext(pc);
    getLog().debug("Class of the results is " + theResult.getClass());
    Path p = writeMarker(IBDataModelUtils.remodel(theResult)).toAbsolutePath();
    getLog().debug("Marker written to " + p);
    pc.put(TRANSFORMATION_TARGET, theResult);
    getLog().debug("plugin context set");
    getLog().info("Data transformations complete with " + theResult.getPath() + " as " + theResult.getChecksum());
  }

}
