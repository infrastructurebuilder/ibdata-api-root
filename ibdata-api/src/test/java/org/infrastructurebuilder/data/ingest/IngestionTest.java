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
package org.infrastructurebuilder.data.ingest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.infrastructurebuilder.data.model.DataSet;
import org.junit.Before;
import org.junit.Test;

public class IngestionTest {

  private static final String DEFAULT = "default";
  private static final String DEFAULT_INGEST = "default-ingest";
  private Ingestion i;
  private DefaultIBDataSetIdentifier ds;
  private Map<String, Object> fc;
  private DataSet targetDs;
  private final Xpp3Dom metadata = new Xpp3Dom("metadata");
  BiFunction<? extends DataSet, ? extends DataSet, Boolean> equalser;
  private DefaultIBDataSetIdentifier ids;

  @Before
  public void setUp() throws Exception {
    // FIXME Create local .equals() for DataSet. See <a
    // href="https://github.com/infrastructurebuilder/ibcore-root/issues/4"> this
    // issue</a>.
    // Eventually this BiFunction should be static and move to (probably)
    // IBDataSetIdentifier
    equalser = (lhs, target) -> {
      if (target == null)
        return false;
      if (target == lhs)
        return true;
      if (target.getClass() != lhs.getClass())
        return false;
      DataSet rhs = (DataSet) target;
      return Objects.equals(lhs.getArtifactId(), rhs.getArtifactId())
          // ArtifactId
          && Objects.equals(lhs.getArtifactId(), rhs.getArtifactId())
      // Version (Maybe we want to convert to IBVersions to do the comparison?)
          && Objects.equals(lhs.getVersion(), rhs.getVersion());
    };

    fc = new HashMap<>();
    targetDs = new DataSet();
    targetDs.setGroupId("A");
    targetDs.setArtifactId("B");
    targetDs.setVersion("1.0.0-SNAPSHOT");
    targetDs.setDataSetName("name");
    targetDs.setDataSetDescription("description");
    targetDs.setPath("/");
    targetDs.setMetadata(new Xpp3Dom("metadata"));
    targetDs.setModelEncoding("UTF-8");
    targetDs.setModelVersion("1.0.0");
    i = new Ingestion();

    ids = (DefaultIBDataSetIdentifier) i.getDataSet();
    ids.injectGAV("A", "B", "1.0.0-SNAPSHOT");
    ids.setMetadata(metadata);
    ids.setName("name");
    ids.setDescription("description");
    ids.setPath("/");
    ds = new DefaultIBDataSetIdentifier(ids);

  }

  @Test
  public void testGetId() {
    assertEquals(DEFAULT, i.getId());
  }

  @Test
  public void testGetIngester() {
    assertEquals(DEFAULT, i.getIngester());
  }

  @Test
  public void testGetFinalizer() {
    assertEquals(DEFAULT_INGEST, i.getFinalizer());
  }

  @Test
  public void testGetFinalizerConfig() {
    assertEquals(0, i.getFinalizerConfig().size());
  }

  @Test
  public void testAsDataSet() {
    assertEquals(targetDs, i.asDataSet());
    i.setDataSet(ids);
    assertEquals(targetDs, ids.asDataSet());

  }

  @Test
  public void testIsExpands() {
    DefaultIBDataStreamIdentifierConfigBean s1 = new DefaultIBDataStreamIdentifierConfigBean(),
        s2 = new DefaultIBDataStreamIdentifierConfigBean();
    s1.setTemporaryId("s1");
    s2.setTemporaryId("s2");
    s1.setExpandArchives(true);
    s2.setExpandArchives(false);
    List<DefaultIBDataStreamIdentifierConfigBean> stream2s = Arrays.asList(s1, s1, s2);
    ids.setStreams(stream2s);
    i.setDataSet(ids);
    assertTrue(i.isExpand("s1"));
    assertFalse(i.isExpand("s2"));

  }

}
