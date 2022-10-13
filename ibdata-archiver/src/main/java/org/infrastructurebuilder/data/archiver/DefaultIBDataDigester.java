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

import static org.infrastructurebuilder.data.archiver.DefaultIBDataStreamingDigester.SHA512_STREAMING;

@javax.inject.Named(DefaultIBDataDigester.SHA512)
public class DefaultIBDataDigester extends org.codehaus.plexus.digest.AbstractDigester {
  public static final String SHA512 = "sha512";

  @javax.inject.Inject
  public DefaultIBDataDigester(
      @javax.inject.Named(SHA512_STREAMING) org.codehaus.plexus.digest.StreamingDigester other) {
    super(other);
  }

  @Override
  public String getFilenameExtension() {
    return "." + SHA512;
  }

}
