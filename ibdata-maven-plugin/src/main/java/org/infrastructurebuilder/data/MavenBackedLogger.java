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

import static java.util.Objects.requireNonNull;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.maven.plugin.logging.Log;
import org.slf4j.helpers.MarkerIgnoringBase;

@Named(MavenBackedLogger.MAVEN_BACKED_LOGGER)
public class MavenBackedLogger extends MarkerIgnoringBase {

  static final String MAVEN_BACKED_LOGGER = "maven-backed-logger";
  private final Log l;

  @Inject
  public MavenBackedLogger(Log log) {
    this.l = requireNonNull(log);
  }

  @Override
  public String getName() {
    return MAVEN_BACKED_LOGGER;
  }

  @Override
  public boolean isTraceEnabled() {
    return isDebugEnabled();
  }

  @Override
  public void trace(String msg) {
    debug(msg);
  }

  @Override
  public void trace(String format, Object arg) {
    debug(format, arg);
  }

  @Override
  public void trace(String format, Object arg1, Object arg2) {
    debug(format, arg1, arg2);
  }

  @Override
  public void trace(String format, Object... arguments) {
    debug(format, arguments);
  }

  @Override
  public void trace(String msg, Throwable t) {
    debug(msg, t);
  }

  @Override
  public boolean isDebugEnabled() {
    return l.isDebugEnabled();
  }

  @Override
  public void debug(String msg) {
    l.debug(msg);
  }

  @Override
  public void debug(String format, Object arg) {
    l.debug(String.format(format, arg));
  }

  @Override
  public void debug(String format, Object arg1, Object arg2) {
    l.debug(String.format(format, arg1, arg2));
  }

  @Override
  public void debug(String format, Object... arguments) {
    l.debug(String.format(format, arguments));
  }

  @Override
  public void debug(String msg, Throwable t) {
    l.debug(msg, t);
  }

  @Override
  public boolean isInfoEnabled() {
    return l.isInfoEnabled();
  }

  @Override
  public void info(String msg) {
    l.info(msg);
  }

  @Override
  public void info(String format, Object arg) {
    l.info(String.format(format, arg));
  }

  @Override
  public void info(String format, Object arg1, Object arg2) {
    l.info(String.format(format, arg1, arg2));
  }

  @Override
  public void info(String format, Object... arguments) {
    l.info(String.format(format, arguments));
  }

  @Override
  public void info(String msg, Throwable t) {
    l.info(msg, t);
  }

  @Override
  public boolean isWarnEnabled() {
    return l.isWarnEnabled();
  }

  @Override
  public void warn(String msg) {
    l.warn(msg);
  }

  @Override
  public void warn(String format, Object arg) {
    l.warn(String.format(format, arg));
  }

  @Override
  public void warn(String format, Object arg1, Object arg2) {
    l.warn(String.format(format, arg1, arg2));
  }

  @Override
  public void warn(String format, Object... arguments) {
    l.warn(String.format(format, arguments));
  }

  @Override
  public void warn(String msg, Throwable t) {
    l.warn(msg, t);
  }

  @Override
  public boolean isErrorEnabled() {
    return l.isErrorEnabled();
  }

  @Override
  public void error(String msg) {
    l.error(msg);
  }

  @Override
  public void error(String format, Object arg) {
    l.error(String.format(format, arg));
  }

  @Override
  public void error(String format, Object arg1, Object arg2) {
    l.error(String.format(format, arg1, arg2));
  }

  @Override
  public void error(String format, Object... arguments) {
    l.error(String.format(format, arguments));
  }

  @Override
  public void error(String msg, Throwable t) {
    l.error(msg, t);
  }

}
