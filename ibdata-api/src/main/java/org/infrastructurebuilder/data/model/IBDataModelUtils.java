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
package org.infrastructurebuilder.data.model;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;
import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.infrastructurebuilder.IBConstants.APPLICATION_XML;
import static org.infrastructurebuilder.IBConstants.AVRO_BINARY;
import static org.infrastructurebuilder.IBConstants.JAVA_LANG_STRING;
import static org.infrastructurebuilder.IBConstants.ORG_APACHE_AVRO_GENERIC_INDEXED_RECORD;
import static org.infrastructurebuilder.IBConstants.ORG_W3C_DOM_NODE;
import static org.infrastructurebuilder.IBConstants.TEXT_CSV;
import static org.infrastructurebuilder.IBConstants.TEXT_CSV_WITH_HEADER;
import static org.infrastructurebuilder.IBConstants.TEXT_PLAIN;
import static org.infrastructurebuilder.IBConstants.TEXT_PSV;
import static org.infrastructurebuilder.IBConstants.TEXT_PSV_WITH_HEADER;
import static org.infrastructurebuilder.IBConstants.TEXT_TSV;
import static org.infrastructurebuilder.IBConstants.TEXT_TSV_WITH_HEADER;
import static org.infrastructurebuilder.IBConstants.UTF8;
import static org.infrastructurebuilder.data.IBDataConstants.APPLICATION_IBDATA_ARCHIVE;
import static org.infrastructurebuilder.data.IBDataConstants.IBDATA;
import static org.infrastructurebuilder.data.IBDataConstants.IBDATASET_XML;
import static org.infrastructurebuilder.data.IBDataException.cet;
import static org.infrastructurebuilder.data.IBMetadataUtils.toDataSchema;
import static org.infrastructurebuilder.data.IBMetadataUtils.toDataStream;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.data.IBDataSchemaAsset;
import org.infrastructurebuilder.data.IBDataSetIdentifier;
import org.infrastructurebuilder.data.IBDataStreamSupplier;
import org.infrastructurebuilder.data.IBSchemaDAO;
import org.infrastructurebuilder.data.IBSchemaDAOSupplier;
import org.infrastructurebuilder.data.model.io.xpp3.IBDataSourceModelXpp3Reader;
import org.infrastructurebuilder.data.model.io.xpp3.IBDataSourceModelXpp3Writer;
import org.infrastructurebuilder.data.model.io.xpp3.PersistedIBSchemaXpp3Reader;
import org.infrastructurebuilder.data.model.io.xpp3.PersistedIBSchemaXpp3Writer;
import org.infrastructurebuilder.util.IBUtils;
import org.infrastructurebuilder.util.artifacts.Checksum;
import org.infrastructurebuilder.util.artifacts.ChecksumBuilder;
import org.infrastructurebuilder.util.config.IBRuntimeUtils;
import org.infrastructurebuilder.util.files.DefaultIBResource;
import org.infrastructurebuilder.util.files.IBResource;
import org.infrastructurebuilder.util.files.TypeToExtensionMapper;
import org.infrastructurebuilder.util.files.model.IBResourceModel;

public class IBDataModelUtils {

  public final static IBDataSourceModelXpp3Writer xpp3Writer = new IBDataSourceModelXpp3Writer();

  public final static void writeDataSet(DataSet ds, Path target, Optional<String> basedirString) {
    Path v = target.resolve(IBDATA).resolve(IBDATASET_XML);
    cet.withTranslation(() -> Files.createDirectories(v.getParent()));
    try (Writer writer = Files.newBufferedWriter(v, UTF_8, CREATE_NEW)) {
      DataSet d = ds.clone();
      d.getStreams().stream().forEach(s -> {
        s.getUrl().ifPresent(k -> {
          basedirString.ifPresent(basedir -> {
            if (k.contains(basedir))
              s.setUrl(k.replace(basedir, "${basedir}"));
          });
          if (k.contains("!/") && !(k.startsWith("zip:") || k.startsWith("jar:")))
            s.setUrl("jar:" + s.getUrl());
        });
      });
      xpp3Writer.write(writer, d);
    } catch (Throwable e) { // Catch anything and translate it to an IBDataException
      throw new IBDataException(e);
    }
  }

  public final static String relativizePath(DataSet ds, DataStream s) {
    return ds.getLocalPath().map(u -> {
      String u1 = u.toAbsolutePath().toString() + File.separator;
      String s2P = s.getPath();
      return ofNullable(s2P).map(s2 -> (s2.startsWith(u1)) ? s2.substring(u1.length()) : s2).orElse(null);
    }).orElse(s.getPath());
  }

  public final static Function<String, Optional<URL>> safeMapURL = (s) -> ofNullable(s)
      .map(u -> cet.withReturningTranslation(() -> IBUtils.translateToWorkableArchiveURL(u)));

  public final static Function<IBDataSetIdentifier, Checksum> dataSetIdentifierChecksum = (ds) -> {
    return ChecksumBuilder.newInstance()
        // Group
        .addChecksumEnabled(ds.getGAV())
        //
        .addString(ds.getName())
        //
        .addString(ds.getDescription())
        //
        .addDate(ds.getCreationDate())
        //
        .addChecksumEnabled(ds.getMetadata())
        // fin
        .asChecksum();
  };

  public final static Checksum fromPathDSAndStream(Path workingPath, DataSet ds) {
    return ChecksumBuilder.newInstance(of(workingPath))
        // Checksum of data of streams
        .addChecksum(new Checksum(ds.getStreams().stream().map(s -> s.getChecksum()).collect(toList())))
        // Checksum of data
        .addChecksumEnabled(ds)
        // FIXME Why not DataSet.asChecksum?
        /*.addChecksum(dataSetIdentifierChecksum.apply(ds))*/.asChecksum();
  }

  public final static BiFunction<? super IBRuntimeUtils, ? super PersistedIBSchema, ? extends Path> writeSchemaToPath = (
      ibr, schema) -> {
    Path path = ibr.getWorkingPath().resolve(UUID.randomUUID().toString() + ".xml");
    try (Writer w = Files.newBufferedWriter(path)) {
      new PersistedIBSchemaXpp3Writer().write(w, schema);
      return path;
    } catch (IOException e) {
      throw new IBDataException(String.format("Failed to persist InlineIBSchema to %s", path.toString()), e);
    }

  };

  public final static Function<? super InputStream, ? extends PersistedIBSchema> mapInputStreamToPersistedSchema = (
      in) -> {
    PersistedIBSchemaXpp3Reader reader = new PersistedIBSchemaXpp3Reader();
    try {
      return cet.withReturningTranslation(() -> reader.read(in, true)).forceIndexUpdatePostRead().clone();
    } finally {
      cet.withTranslation(() -> in.close());
    }
  };

  public final static Function<? super String, ? extends PersistedIBSchema> mapUTF8StringToPersistedSchema = (in) -> {
    return mapInputStreamToPersistedSchema.apply(new ByteArrayInputStream(in.getBytes(UTF8)));
  };

  public final static Function<? super URL, ? extends PersistedIBSchema> mapURLToPersistedSchema = (in) -> {
    try (InputStream ins = requireNonNull(in).openStream()) {
      PersistedIBSchema d = mapInputStreamToPersistedSchema.apply(ins);
      Path p = (in.getProtocol().contains("file:")) ? Paths.get(in.toURI()) : null;
      return d;
    } catch (IOException | URISyntaxException e) {
      throw new IBDataException(e);
    }
  };

  public final static Function<? super Path, ? extends PersistedIBSchema> mapPathToPersistedSchema = (in) -> {
    return mapURLToPersistedSchema.apply(cet.withReturningTranslation(() -> requireNonNull(in).toUri().toURL()));
  };

  public final static Function<? super InputStream, ? extends DataSet> mapInputStreamToDataSet = (in) -> {
    IBDataSourceModelXpp3Reader reader;

    reader = new IBDataSourceModelXpp3Reader();
    try {
      return cet.withReturningTranslation(() -> reader.read(in, true));
    } finally {
      cet.withTranslation(() -> in.close());
    }
  };

  public final static Function<? super URL, ? extends DataSet> mapURLToDataSet = (in) -> {
    try (InputStream ins = requireNonNull(in).openStream()) {
      DataSet d = mapInputStreamToDataSet.apply(ins);
      Path p = (in.getProtocol().contains("file:")) ? Paths.get(in.toURI()) : null;
      d.setPath(in);
      return d;
    } catch (IOException | URISyntaxException e) {
      throw new IBDataException(e);
    }
  };

  public final static Function<? super Path, ? extends DataSet> mapPathToDataSet = (in) -> {
    return mapURLToDataSet.apply(cet.withReturningTranslation(() -> requireNonNull(in).toUri().toURL()));

  };

  /**
   * Given the parameters, create a final data location (either through atomic
   * moves or copies), that maps to a state that will allow us to generate an
   * archive
   *
   * @param creationDate    The date to set all the model elements to as a
   *                        creation date
   * @param workingPath     This is a current working path.
   * @param finalData       This is as much of the metadata of the dataset as we
   *                        currently know. It has no streams attached at the
   *                        moment
   * @param ibdssList       These are the streams we will attach
   * @param schemaSuppliers schemas and their available streams to attach here
   * @param t2e             {@link TypeToExtensionMapper} for managing extensions
   * @param basedir         The {@code ${basedir} } of this build for
   *                        relativisation of inbound resources
   *
   * @return A location suitable for archive generation
   * @throws IOException
   */
  public final static IBResource forceToFinalizedPath(
      // Reset everything to this creation date
      Date creationDate
      // Target WP
      , Path workingPath
      // The inbound data set
      , DataSet finalData
      // The inbound data streams (not including
      , List<IBDataStreamSupplier> ibdssList
      // Schema suppliers
      , List<IBSchemaDAOSupplier> schemaSuppliers
      // Should be runtime utils
      , TypeToExtensionMapper t2e
      // ??
      , Optional<String> basedir) throws IOException {

    // FIXME We need to be able to do multiple schema ingestions somehow
    if (Objects.requireNonNull(schemaSuppliers).size() > 1)
      throw new IBDataException("Currently IBData limits schema ingestion to a single instance");

    // This archive is about to be created
    finalData.setCreationDate(requireNonNull(creationDate)); // That is now
    Path newWorkingPath = workingPath.getParent().resolve(UUID.randomUUID().toString());
    finalData.setPath(cet.withReturningTranslation(() -> newWorkingPath));
    // We're moving everything to a new path
    Files.createDirectories(newWorkingPath);
    // Set the schemas here!
    // Hint: When you wrote the schema the first time, the UUID for that stream was
    // computable. Now that the stream has been
    // moved, you're still OK
    List<IBSchemaDAO> finalizedDataSchemaList =
        // List all the schema
        schemaSuppliers.stream()
            // Get the schema, if you need it for some reason
            .map(IBSchemaDAOSupplier::get)
            // to a (unique) list
            .collect(toList());

    // FIXME Currently this list should have ONE element at most

    Map<String, DataSchema> tToDataSchema = new HashMap<>();
    Map<String, Map<String, IBDataStreamSupplier>> tToMapOfDataStreamSuppliers = new HashMap<>();
    Map<String, Map<String, DataStream>> tToMapOfDataStreamElementsForSuppliers = new HashMap<>();
    for (IBSchemaDAO ff : finalizedDataSchemaList) {
      String temporaryId = ff.getInboundId();
      DataSchema q = toDataSchema.apply(ff.getSchema());
      Map<String, IBDataStreamSupplier> schemaDataStreamSuppliers = ff.get();
      Map<String, DataStream> remappedAssets = new HashMap<>();
      schemaDataStreamSuppliers.forEach((schemaSpecificSupplier, assetStreamSupplier) -> {
        DataStream assetStream = toDataStream.apply(assetStreamSupplier.get().relocateTo(newWorkingPath, t2e));
        remappedAssets.put(schemaSpecificSupplier, assetStream);
      });
      tToMapOfDataStreamElementsForSuppliers.put(temporaryId, remappedAssets);
      tToMapOfDataStreamSuppliers.put(temporaryId, schemaDataStreamSuppliers);
      DataStream theAsset = remappedAssets.get(ff.getPrimaryAssetKeyName());
      String uuid = ofNullable(theAsset.getUuid())
          .orElseThrow(() -> new IBDataException("No primary asset for " + temporaryId));
      List<IBDataSchemaAsset> v = new ArrayList<>(q.getSchemaAssets());

      tToDataSchema.put(temporaryId, q);
    }
    Set<String> temporaryIds = tToMapOfDataStreamElementsForSuppliers.keySet();

    // FIXME This is all part of the "one schema ingested" problem
    if (temporaryIds.size() > 1)
      throw new IBDataException("Currently IBData can only handle a single set of elements");
    Optional<String> temporaryId = ofNullable(temporaryIds.size() == 1 ? temporaryIds.iterator().next() : null);

    // ?? Does order matter here?

    Collection<DataStream> schemaMapValues; // schemaMap.values()
    schemaMapValues = temporaryId.map(tToMapOfDataStreamElementsForSuppliers::get).map(Map::values)
        .orElse(Collections.emptySet());

    List<DataStream> finalizedDataStreams =
        // Join the two streams
        Stream.concat(
            // Values from the schema
            schemaMapValues.stream(),
//            schemaMap.values().stream(),
            // Values from regular data streams
            ibdssList.stream()
                // Fetch the IBDS
                .map(Supplier::get)
                // Relocate the stream
                .map(dss -> dss.relocateTo(newWorkingPath, t2e))
                // Map the IBDataStream to a DataStream object
                .map(toDataStream))
            // to list
            .collect(toList()
            // End Concat
            );

    // TODO Maybe we ADD streams and schema here?
    finalData.setStreams(finalizedDataStreams);
    finalData.setSchemas(tToDataSchema.values().stream().collect(toList()));
    // finalData.getStreams().stream().forEach(dss ->
    // dss.setPath(IBDataModelUtils.relativizePath(finalData, dss)));
    // The id of the archive is based on the checksum of the data within it
    Checksum dsChecksum = fromPathDSAndStream(newWorkingPath, finalData);
    finalData.setUuid(dsChecksum.asUUID().get().toString());
    // We're going to relocate the entire directory to a named UUID-backed directory
    Path newTarget = workingPath.getParent().resolve(finalData.getUuid().toString());
    Files.move(newWorkingPath, newTarget, ATOMIC_MOVE);
    finalData.setPath(newTarget);

    // Create the IBDATA dir so that we can write the metadata xml
    createDirectories(newTarget.resolve(IBDATA));
    // Clear the path so that it doesn't persist in the metadata xml
    DataSet finalData2 = finalData.clone(); // Executes the clone hook, including relativizing the path
    finalData2.setPath((Path) null);
    // write the dataset to disk
    IBDataModelUtils.writeDataSet(finalData2, newTarget, basedir);
    // newTarget now points to a valid DataSet with metadata and referenced streams
    return DefaultIBResource.from(newTarget, dsChecksum, APPLICATION_IBDATA_ARCHIVE);
  }

  /**
   * "remodel" the existing result into an IBResourceModel Eventually, this will
   * probably be how moveTo relocates URLs that aren't concrete physical file
   * paths (like paths into archives)
   *
   * @param theResult
   * @return
   */
  public final static IBResourceModel remodel(IBResource theResult) {
    if (theResult instanceof IBResourceModel)
      return (IBResourceModel) theResult;
    throw new IBDataException("Cannot cast to model");
  }

  public static Optional<String> getStructuredSupplyTypeClass(String t) {
    switch (t) {
    case AVRO_BINARY:
      return of(ORG_APACHE_AVRO_GENERIC_INDEXED_RECORD);
    case APPLICATION_XML:
      return of(ORG_W3C_DOM_NODE);
    case TEXT_CSV:
    case TEXT_PLAIN:
    case TEXT_PSV:
    case TEXT_TSV:
    case TEXT_CSV_WITH_HEADER:
    case TEXT_PSV_WITH_HEADER:
    case TEXT_TSV_WITH_HEADER:
      return of(JAVA_LANG_STRING);
    default: // FIXME there are other types to return
      return empty();
    }
  }

}
