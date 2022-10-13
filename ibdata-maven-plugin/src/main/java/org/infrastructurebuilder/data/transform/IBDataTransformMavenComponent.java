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

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static org.infrastructurebuilder.data.IBDataConstants.IBDATA;
import static org.infrastructurebuilder.data.IBDataConstants.IBDATASET_XML;
import static org.infrastructurebuilder.data.IBDataConstants.IBDATA_WORKING_PATH_SUPPLIER;
import static org.infrastructurebuilder.data.IBDataException.cet;

import java.net.URL;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.infrastructurebuilder.data.AbstractIBDataMavenComponent;
import org.infrastructurebuilder.data.DefaultIBDataEngine;
import org.infrastructurebuilder.data.DefaultIBDataSet;
import org.infrastructurebuilder.data.DefaultIBDataTransformationResult;
import org.infrastructurebuilder.data.IBDataDataStreamRecordFinalizerSupplier;
import org.infrastructurebuilder.data.IBDataEngine;
import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.data.IBDataSetFinalizer;
import org.infrastructurebuilder.data.IBDataSetFinalizerSupplier;
import org.infrastructurebuilder.data.IBDataStream;
import org.infrastructurebuilder.data.IBDataTypeImplsModelUtils;
import org.infrastructurebuilder.data.IBStreamerFactory;
import org.infrastructurebuilder.data.Metadata;
import org.infrastructurebuilder.data.model.DataSet;
import org.infrastructurebuilder.data.transform.base.IBDataTransformerSupplier;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.config.PathSupplier;
import org.infrastructurebuilder.util.files.IBChecksumPathType;
import org.infrastructurebuilder.util.files.TypeToExtensionMapper;

@Named("transform")
public final class IBDataTransformMavenComponent extends AbstractIBDataMavenComponent {

  public final static BiFunction<MavenProject, Path, IBDataTransformationResult> fromProject = (project,
      workingPath) -> {
    requireNonNull(project);
    DataSet ds = new DataSet();
    ds.setUuid(UUID.randomUUID().toString());
    ds.setGroupId(project.getGroupId());
    ds.setArtifactId(project.getArtifactId());
    ds.setVersion(project.getVersion());
    ds.setName(project.getName());
    ds.setDescription(project.getDescription());
    ds.setCreationDate(Instant.now().toString());
    ds.setMetadata(new Metadata());

    ds.setPath(IBDataException.cet.returns(() -> workingPath.toAbsolutePath().toUri().toURL().toExternalForm()));
    return new DefaultIBDataTransformationResult(new DefaultIBDataSet(ds), workingPath);
  };

  public final static Function<IBChecksumPathType, IBDataTransformationResult> fromPrevious = (previous) -> {
    Optional<DefaultIBDataSet> ds = IBDataTypeImplsModelUtils.mapDataSetToDefaultIBDataSet.apply(
        cet.withReturningTranslation(() -> previous.getPath().resolve(IBDATA).resolve(IBDATASET_XML).toUri().toURL()));
    return new DefaultIBDataTransformationResult(ds.get(), previous.getPath());
  };

  private final Map<String, IBDataDataStreamRecordFinalizerSupplier<?>> allRecordFinalizers;
  private final Map<String, IBDataTransformerSupplier> allTransformers;
  private final IBDataEngine engine;
  private boolean injectProjectDependencies = false;

  /*
   * Injected constructor.
   *
   * @param workingPathSupplier Late bound PathSupplier, which is setup within
   * _setup() to contain the working directory
   *
   * @param log Maven log
   *
   * @param defaultTypeToExtensionMapper a TypeToExtensionMapper, which maps mime
   * types to file extensions
   *
   * @param allRecordFinalizers
   *
   * @param allTransformers
   */
  @Inject
  public IBDataTransformMavenComponent(
      // Late-bound PathSupplier. Must be set in the executor before use
      @Named(IBDATA_WORKING_PATH_SUPPLIER) PathSupplier workingPathSupplier,
      // The logger
      Log log,
      // Mapper for extensions to mime types
      TypeToExtensionMapper defaultTypeToExtensionMapper,
      // The configuration map. Does not include config from components
      @Named(ConfigMapSupplier.MAVEN) ConfigMapSupplier mavenConfigMapSupplier,
      // All avilable transformers
      Map<String, IBDataTransformerSupplier> allTransformers,
      // All available IBDataDataStreamRecordFinalizerSupplier instances
      Map<String, IBDataDataStreamRecordFinalizerSupplier<?>> allRecordFinalizers,
      Map<String, IBDataSetFinalizerSupplier<?>> allDSFinalizers, IBStreamerFactory streamerFactory,
      IBDataEngine engine) {
    super(workingPathSupplier, log, defaultTypeToExtensionMapper, mavenConfigMapSupplier, allDSFinalizers,
        streamerFactory);
    this.allTransformers = requireNonNull(allTransformers);
    this.allRecordFinalizers = requireNonNull(allRecordFinalizers);
    this.engine = requireNonNull(engine);

  }

  public void setAdditionalURLS(List<URL> l) {
    if (DefaultIBDataEngine.class.isAssignableFrom(this.engine.getClass())) {
      ((DefaultIBDataEngine) this.engine).setAdditionalURLS(l);
    }
    int q = this.engine.prepopulate();
  }

  @SuppressWarnings("unchecked")
  public IBChecksumPathType transform(
      // Config supplied from plguin in pom
      List<Transformation> transformations) throws MojoFailureException {

    Map<UUID, IBDataStream> availableStreams = this.engine.getAvailableIds().stream().map(this.engine::fetchDataSetById)
        .filter(Optional::isPresent).map(Optional::get)
        // To the stream suppliers
        .flatMap(s -> s.getStreamSuppliers().stream())
        // call the supplier
        .map(Supplier::get).collect(Collectors.toMap(k -> k.getId(), Function.identity()));
    MavenProject p = getProject().orElseThrow(() -> new IBDataException("No project available"));
    IBChecksumPathType retVal = null;
    // Every Transformation produces a single DataStream
    try { // Outer catch for throwing MojoFailureException if anything goes awry
      IBDataTransformationResult ref = null;
      for (Transformation transformation : transformations) {
        requireNonNull(transformation); // FIXME is this redundant?
        // Inject the required stuff here
        transformation.forceDefaults(p.getGroupId(), p.getArtifactId(), p.getVersion());
        // Set name and desc if null
        if (transformation.getName() == null)
          transformation.setName(p.getName());
        if (transformation.getName() == null)
          throw new IBDataException("Transformation name required for " + transformation.getId());
        if (transformation.getDescription() == null)
          transformation.setDescription(p.getDescription());
        if (transformation.getDescription() == null)
          throw new IBDataException("Transformation description required for " + transformation.getId());

        // Get the configured finalizer
        // Acquire DataSet finalizer
        IBDataSetFinalizer<Transformation> finalizer;
        try {
          finalizer = (IBDataSetFinalizer<Transformation>) getDataSetFinalizerSupplier(transformation.getFinalizer(),
              transformation.getFinalizerConfig()); // ,
                                                    // Optional.ofNullable(ref).map(IBDataTransformationResult::getWorkingPathSupplier)*/);
        } catch (ClassCastException e) {
          throw new IBDataException("Finalizer " + transformation.getFinalizer() + " in transformation "
              + transformation.getId() + " was not considered viable", e);
        }

        //
        ConfigMapSupplier defaults = getConfigMapSupplier();
        // Get an ordered list of transformers
        Map<String, IBDataTransformer> configuredMap = new HashMap<>();
        List<IBDataStream> sources = null;
        for (Transformer t : transformation.getTransformers()) {
          ConfigMapSupplier tConfig = t.getConfigurationAsConfigMapSupplier(defaults);
          IBDataTransformerSupplier ts = ofNullable(allTransformers.get(t.getHint()))
              .orElseThrow(() -> new IBDataException("Transformer " + t.getHint() + " not found"));
          // Special instance of configuration.
          if (t instanceof RecordTransformer) {
            RecordTransformer g = (RecordTransformer) t;
            ts = ts.withFinalizer(
                // Select the record finalizer supplier for this transformer
                ofNullable(this.allRecordFinalizers.get(g.getRecordFinalizer()))
                    .orElseThrow(
                        () -> new IBDataException("Record finalizer  " + g.getRecordFinalizer() + " not found"))
                    // Then configure the supplier
                    .configure(g.getRecordFinalizerConfig(defaults))
                    // Get the record finalizer
                    .get());
          }
          ts = ts.configure(t.getConfigurationAsConfigMapSupplier(tConfig));
          configuredMap.put(t.getId(), ts.get());
          sources = t.asMatchingStreams(availableStreams.values());
        }

        if (retVal == null) {
          ref = fromProject.apply(p, finalizer.getWorkingPath());
        } else {
          ref = fromPrevious.apply(retVal);
        }
        // Walk the transformations list.
        for (Transformer t : transformation.getTransformers()) {
          if (ref.get().isPresent()) {
            IBDataTransformer transformer = configuredMap.get(t.getId());
            ref = transformer.transform(t, ref.get().get(), sources, t.isFailOnAnyError());
          }
        }

        // Finalize
        retVal = requireNonNull(ref)
            // supplies Optional dataset
            .get()
            // Map the dataset to the finalized dataset
            .map(r -> cet.withReturningTranslation(
                () -> finalizer.finalize(r, transformation, r.getStreamSuppliers(), getBaseDir())))
            // Or throw exception if no such dataset exists
            .orElseThrow(() -> new IBDataException("Failed to finalize.  IBDataSet unavailable from processing"));
      }
      return retVal;
    } catch (IBDataException e) {
      throw new MojoFailureException("Transformation failed to execute", e);
    }
  }

}
