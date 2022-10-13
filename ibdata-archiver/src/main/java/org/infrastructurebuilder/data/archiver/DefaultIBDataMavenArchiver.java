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
package org.infrastructurebuilder.data.archiver;

import javax.inject.Inject;
import javax.inject.Named;

import org.codehaus.plexus.archiver.jar.JarArchiver;

@Named(DefaultIBDataMavenArchiver.IBDATA)
public class DefaultIBDataMavenArchiver extends JarArchiver {


  public static final String IBDATA = "ibdata";

  /**
   * <p>Constructor for DefaultIBDataMavenArchiver.</p>
   */
  @Inject
  public DefaultIBDataMavenArchiver() {
    super();
  }


}
