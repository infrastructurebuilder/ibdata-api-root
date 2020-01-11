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
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.move;
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
import static org.infrastructurebuilder.IBConstants.TEXT_PLAIN;
import static org.infrastructurebuilder.IBConstants.TEXT_PSV;
import static org.infrastructurebuilder.IBConstants.TEXT_TSV;
import static org.infrastructurebuilder.data.IBDataConstants.APPLICATION_IBDATA_ARCHIVE;
import static org.infrastructurebuilder.data.IBDataConstants.IBDATA;
import static org.infrastructurebuilder.data.IBDataConstants.IBDATASET_XML;
import static org.infrastructurebuilder.data.IBDataException.cet;
import static org.infrastructurebuilder.data.IBMetadataUtils.toDataStream;
import static org.infrastructurebuilder.util.IBUtils.nullSafeURLMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import org.infrastructurebuilder.data.model.DataSet;
import org.infrastructurebuilder.data.model.DataStream;
import org.infrastructurebuilder.data.model.io.xpp3.IBDataSourceModelXpp3Reader;
import org.infrastructurebuilder.data.model.io.xpp3.IBDataSourceModelXpp3Writer;
import org.infrastructurebuilder.util.IBUtils;
import org.infrastructurebuilder.util.artifacts.Checksum;
import org.infrastructurebuilder.util.artifacts.ChecksumBuilder;
import org.infrastructurebuilder.util.files.DefaultIBChecksumPathType;
import org.infrastructurebuilder.util.files.IBChecksumPathType;
import org.infrastructurebuilder.util.files.TypeToExtensionMapper;
import org.infrastructurebuilder.util.files.model.IBChecksumPathTypeModel;

public class IBDataModelUtils {

  public final static IBDataSourceModelXpp3Writer xpp3Writer = new IBDataSourceModelXpp3Writer();

  public final static void writeDataSet(DataSet ds, Path target, Optional<String> basedirString) {
    Path v = target.resolve(IBDATA).resolve(IBDATASET_XML);
    cet.withTranslation(() -> Files.createDirectories(v.getParent()));
    try (Writer writer = Files.newBufferedWriter(v, UTF_8, CREATE_NEW)) {
      DataSet d = ds.clone();
      d.getStreams().stream().forEach(s -> {
        final String k = s.getUrl().get();
        basedirString.ifPresent(basedir -> {
          if (k.contains(basedir))
            s.setUrl(k.replace(basedir, "${basedir}"));
        });
        if (k.contains("!/") && !(k.startsWith("zip:") || k.startsWith("jar:")))
          s.setUrl("jar:" + s.getUrl());
      });
      xpp3Writer.write(writer, d);
    } catch (Throwable e) { // Catch anything and translate it to an IBDataException
      throw new IBDataException(e);
    }
  }

  public final static String relativizePath(DataSet ds, DataStream s) {
    return nullSafeURLMapper.apply(ds.getPath().orElse(null)).map(u -> {
      String u1 = u.toExternalForm();
      String s2P = s.getPath();
      return ofNullable(s2P).map(s2 -> (s2.startsWith(u1)) ? s2.substring(u1.length()) : s2).orElse(null);
    }).orElse(s.getPath());
  }

  public final static Function<String, Optional<URL>> safeMapURL = (s) -> ofNullable(s)
      .map(u -> cet.withReturningTranslation(() -> IBUtils.translateToWorkableArchiveURL(u)));

  public final static Function<IBDataSetIdentifier, Checksum> dataSetIdentifierChecksum = (ds) -> {
    return ChecksumBuilder.newInstance()
        // Group
        .addString(ds.getGroupId())
        // artifact
        .addString(ds.getArtifactId())
        // version
        .addString(ds.getVersion())
        //
        .addString(ds.getName())
        //
        .addString(ds.getDescription())
        //
        .addDate(ds.getCreationDate())
        // fin
        .asChecksum();
  };

  public final static Checksum fromPathDSAndStream(Path workingPath, DataSet ds) {
    return ChecksumBuilder.newInstance(of(workingPath))
        // Checksum of data of streams
        .addChecksum(new Checksum(ds.getStreams().stream().map(s -> s.getChecksum()).collect(toList())))
        // Checksum of stream metadata
        .addChecksum(dataSetIdentifierChecksum.apply(ds)).asChecksum();
  }

  public final static Function<? super InputStream, ? extends DataSet> mapInputStreamToDataSet = (in) -> {
    IBDataSourceModelXpp3Reader reader;

    reader = new IBDataSourceModelXpp3Reader();
    try {
      return cet.withReturningTranslation(() -> reader.read(in, true));
    } finally {
      cet.withTranslation(() -> in.close());
    }
  };

  /**
   * Given the parameters, create a final data location (either through atomic
   * moves or copies), that maps to a state that will allow us to generate an
   * archive
   *
   * @param workingPath This is a current working path.
   * @param finalData   This is as much of the metadata of the dataset as we
   *                    currently know. It has no streams attached at the moment
   * @param ibdssList   These are the streams we will attach
   * @return A location suitable for archive generation
   * @throws IOException
   */
  public final static IBChecksumPathType forceToFinalizedPath(Date creationDate, Path workingPath, DataSet finalData,
      List<IBDataStreamSupplier> ibdssList, List<IBSchemaSupplier> schemaSuppliers, TypeToExtensionMapper t2e,
      Optional<String> basedir) throws IOException {

    // This archive is about to be created
    finalData.setCreationDate(requireNonNull(creationDate)); // That is now
    Path newWorkingPath = workingPath.getParent().resolve(UUID.randomUUID().toString());
    finalData
        .setPath(cet.withReturningTranslation(() -> newWorkingPath.toAbsolutePath().toUri().toURL().toExternalForm()));
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
            .map(IBSchemaSupplier::get)
            // to a (unique) list
            .collect(toList());
    List<Map<String, IBDataStreamSupplier>> finalizedSchemaDataStreams = //
        // All finalized schema
        finalizedDataSchemaList.stream()
            //
            .map(IBSchemaDAO::get)
            //
            .collect(toList());

    List<DataStream> finalizedDataStreams =
        // The list of streams
        ibdssList.stream()
            // Fetch the IBDS
            .map(Supplier::get)
            // Relocate the stream
            .map(dss -> dss.relocateTo(newWorkingPath, t2e))
            // Map the IBDataStream to a DataStream object
            .map(toDataStream)
            // to list
            .collect(toList());
    finalData.setStreams(finalizedDataStreams);
    // finalData.getStreams().stream().forEach(dss ->
    // dss.setPath(IBDataModelUtils.relativizePath(finalData, dss)));
    // The id of the archive is based on the checksum of the data within it
    Checksum dsChecksum = fromPathDSAndStream(newWorkingPath, finalData);
    finalData.setUuid(dsChecksum.asUUID().get().toString());
    // We're going to relocate the entire directory to a named UUID-backed directory
    Path newTarget = workingPath.getParent().resolve(finalData.getUuid().toString());
    move(newWorkingPath, newTarget, ATOMIC_MOVE);
    finalData.setPath(newTarget.toAbsolutePath().toUri().toURL().toExternalForm());
    // Create the IBDATA dir so that we can write the metadata xml
    createDirectories(newTarget.resolve(IBDATA));
    // Clear the path so that it doesn't persist in the metadata xml
    DataSet finalData2 = finalData.clone(); // Executes the clone hook, including relativizing the path
    finalData2.setPath(null);
    // write the dataset to disk
    IBDataModelUtils.writeDataSet(finalData2, newTarget, basedir);
    // newTarget now points to a valid DataSet with metadata and referenced streams
    return DefaultIBChecksumPathType.from(newTarget, dsChecksum, APPLICATION_IBDATA_ARCHIVE);
  }

  /**
   * "remodel" the existing result into an IBChecksumPathTypeModel Eventually,
   * this will probably be how moveTo relocates URLs that aren't concrete physical
   * file paths (like paths into archives)
   *
   * @param theResult
   * @return
   */
  public final static IBChecksumPathTypeModel remodel(IBChecksumPathType theResult) {
    if (theResult instanceof IBChecksumPathTypeModel)
      return (IBChecksumPathTypeModel) theResult;
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
      return of(JAVA_LANG_STRING);
    default: // FIXME there are other types to return
      return empty();
    }
  }

}
