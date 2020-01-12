package org.infrastructurebuilder.data.ingest;

import java.nio.file.Path;

import org.infrastructurebuilder.data.IBIngestedSchemaSupplier;

public interface IBSchemaSourceSupplierMapper {
  boolean respondsTo(IBDataSchemaIngestionConfig v);

  IBIngestedSchemaSupplier getSupplierFor(String temporaryId, IBDataSchemaIngestionConfig v);

  Path getWorkingPath();

}
