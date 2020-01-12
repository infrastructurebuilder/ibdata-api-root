package org.infrastructurebuilder.data.ingest;

import java.nio.file.Path;

import org.infrastructurebuilder.util.config.ConfigMap;
import org.slf4j.Logger;

abstract public class AbstractIBRootIngester {

  protected final Path workingPath;
  protected final ConfigMap config;
  protected final Logger log;

  /**
   * @param workingPath
   * @param log
   * @param config
   */
  public AbstractIBRootIngester(Path workingPath, Logger log, ConfigMap config) {
    this.workingPath = workingPath;
    this.log = log;
    this.config = config;
  }

  public Logger getLog() {
    return log;
  }

  protected ConfigMap getConfig() {
    return config;
  }

  protected Path getWorkingPath() {
    return workingPath;
  }

}