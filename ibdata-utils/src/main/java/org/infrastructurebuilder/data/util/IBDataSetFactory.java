package org.infrastructurebuilder.data.util;

import java.nio.file.Path;
import java.util.function.Function;

import org.infrastructurebuilder.data.IBDataSet;
import org.infrastructurebuilder.data.IBDataStream;
import org.infrastructurebuilder.data.model.DataSet;
import org.infrastructurebuilder.data.util.impl.DefaultDataStream;
import org.infrastructurebuilder.data.util.impl.DefaultIBDataSet;
import org.infrastructurebuilder.util.core.TypeToExtensionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IBDataSetFactory {

  private final static Logger log = LoggerFactory.getLogger(IBDataSetFactory.class);

  public final static Function<DataSet, IBDataSet> fromDataSetToIBDataSet = ds -> {
    return new DefaultIBDataSet(ds);
  };

  public final static Function<IBDataSet, DataSet> fromIBDataSetToDataSet = i -> {
    DataSet d = new DataSet();
    d.setArtifactId(i.getArtifactId());
    d.setCreationDate(i.getCreationDate().toString());
    d.setDescription(i.getDescription().orElse(null));
    d.setGroupId(i.getGroupId());
    d.setMetadata(i.getMetadata());
    d.setName(i.getName());
    return d;
  };

  private final static IBDataSetFactory instance = new IBDataSetFactory();

  public static IBDataSetFactory getInstance() {
    return instance;
  }

  private TypeToExtensionMapper t2e;

  private TypeToExtensionMapper getExtensionMapper() {
    return t2e;
  }

  public IBDataStream relocate(IBDataStream ds, Path newWorkingPath) {
    var em = getExtensionMapper();
    return null;
  }

  public IBDataSet clone(IBDataSet ibds) {
    return new DefaultIBDataSet(fromIBDataSetToDataSet.apply(ibds));
  }

}
