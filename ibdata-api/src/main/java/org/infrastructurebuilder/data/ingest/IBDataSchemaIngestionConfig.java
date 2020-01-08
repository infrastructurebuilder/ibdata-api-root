package org.infrastructurebuilder.data.ingest;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.infrastructurebuilder.data.Metadata;

public interface IBDataSchemaIngestionConfig {

  String getTemporaryId();

  Metadata getMetadata();

  Optional<String> getInline();

  Optional<SchemaQueryBean> getSchemaQuery();

  Optional<List<Path>> getFiles();

  Optional<String> getDescription();

  Optional<String> getName();

}