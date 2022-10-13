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
package org.infrastructurebuilder.data.archive;

import java.util.Locale;
import java.util.Map;

import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;

@Mojo(name = "ibdata", defaultPhase = LifecyclePhase.SITE, requiresDependencyResolution = ResolutionScope.RUNTIME, requiresProject = true, threadSafe = true)
public class IBDataTransactionReport extends AbstractMavenReport {

  private static final String DESCRIPTION = "This report details the executions of ingestions and transactions within an IBData build";
  private static final String IB_DATA_TRANSACTION_REPORT_NAME = "IBData Transaction Report";
  private static final String IBDATA_TRX_REPORT = "ibdata-trx-report";

  public String getOutputName() {
    return IBDATA_TRX_REPORT;
  }

  public String getName(Locale locale) {
    return IB_DATA_TRANSACTION_REPORT_NAME;
  }

  public String getDescription(Locale locale) {
    return DESCRIPTION;
  }

  @Parameter(defaultValue = "${project}", readonly = true)
  private MavenProject project;

  @Override
  protected void executeReport(Locale locale) throws MavenReportException {

    Log logger = getLog();

    logger.info("Generating " + getOutputName() + ".html" + " for " + project.getName() + " " + project.getVersion());
    @SuppressWarnings("rawtypes")
    final Map pc = getPluginContext(); // This context might not work, because of the site lifecycle swap.  WE'll have to test it, and use a marker file if not.

    Sink mainSink = getSink();
    if (mainSink == null) {
      throw new MavenReportException("Could not get the Doxia sink");
    }

    // Page title
    mainSink.head();
    mainSink.title();
    mainSink.text("IBData Report for " + project.getName() + " " + project.getVersion());
    mainSink.title_();
    mainSink.head_();

    mainSink.body();

    // Heading 1
    mainSink.section1();
    mainSink.sectionTitle1();
    mainSink.text("IBData Report for " + project.getName() + " " + project.getVersion());
    mainSink.sectionTitle1_();

    // Content
    mainSink.paragraph();
    mainSink.text("This page provides data about the IBData transactions that occurred within this build ");
    mainSink.paragraph_();

    // Close
    mainSink.section1_();
    mainSink.body_();

  }

}
