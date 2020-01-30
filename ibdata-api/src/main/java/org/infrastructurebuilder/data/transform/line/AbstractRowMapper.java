package org.infrastructurebuilder.data.transform.line;

import static java.util.Optional.of;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.infrastructurebuilder.data.DefaultIBDataTransformationError;
import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.data.IBDataTransformationError;
import org.infrastructurebuilder.data.IBField;
import org.infrastructurebuilder.data.IBSchema;

abstract public class AbstractRowMapper {

  private final IBSchema schema;
  private final Set<IBDataTransformationError> errors = new HashSet<>(5);
  private final boolean failOnError;
  private Map<String, IBField> smMap;

  protected AbstractRowMapper(IBSchema in, boolean failOnError) {
    this.schema = in;
    this.smMap = Optional.ofNullable(in).map(IBSchema::getSchemaFields)
        .map(s -> s.stream().collect(Collectors.toMap(k -> k.getName(), Function.identity())))
        .orElse(Collections.emptyMap());
    this.failOnError = failOnError;
  }

  protected void addError(IBDataTransformationError defaultIBDataTransformationError) {
    errors.add(defaultIBDataTransformationError);
    if (failOnError) {
      throw new IBDataException(
          defaultIBDataTransformationError.getError().orElse(new IBDataException("Generic Transform Failure "
              + defaultIBDataTransformationError.getMessage().orElse("No addl data provided"))));
    }
  }

  protected final boolean isFailOnError() {
    return failOnError;
  }

  protected final IBSchema getSchema() {
    return schema;
  }

  protected void addGenericError(String msg) {
    addGenericError(new IBDataException(msg), msg);
  }

  protected void addGenericError(Throwable t, String msg) {
    addError(new DefaultIBDataTransformationError(of(t), Optional.ofNullable(msg)));
  }

  public List<IBDataTransformationError> getCurrentErrorsList() {
    return errors.stream().collect(Collectors.toList());
  }

  protected Map<String, IBField> getSchemaFieldMapByName() {
    return smMap;
  }
}