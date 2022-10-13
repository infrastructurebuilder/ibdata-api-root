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

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.infrastructurebuilder.data.IBDataConstants.IBDATA;
import static org.infrastructurebuilder.data.IBDataConstants.IBDATASET_XML;
import static org.infrastructurebuilder.data.IBDataException.cet;
import static org.infrastructurebuilder.data.IBDataTypeImplsModelUtils.mapDataSetToDefaultIBDataSet;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.infrastructurebuilder.util.core.LoggerSupplier;
import org.slf4j.Logger;

import net.sf.corn.cps.CPScanner;
import net.sf.corn.cps.ResourceFilter;

@Named
@Singleton
public class DefaultIBDataEngine implements IBDataEngine {
  private final Map<UUID, DefaultIBDataSet> cachedDataSets = new ConcurrentHashMap<>();
  private final Logger log;

  private final List<URL> additionalURLS = new ArrayList<>();

  //  private final static Function<? super UUID, ? extends IBDataSet> mappingFunction = (uuid) -> {
  //    // FIXME We currently return null from mappingFunction because it doesn't get called
  //    return null;
  //  };
  //
  @Inject
  public DefaultIBDataEngine(@Named("maven-log") LoggerSupplier log) {
    this.log = log.get();
  }

  public List<URL> getAdditionalURLS() {
    return additionalURLS;
  }

  public void setAdditionalURLS(List<URL> additionalURLS) {
    this.additionalURLS.addAll(Optional.ofNullable(additionalURLS).orElse(Collections.emptyList()));
  }

  @Override
  public synchronized int prepopulate() {

    if (cachedDataSets.size() == 0) {
      Optional<FileSystemProvider> fsp = IBDataTypeImplsModelUtils.getZipFSProvider();
      URLClassLoader c = (URLClassLoader) getClass().getClassLoader();
      ArrayList<URL> urls = new ArrayList<>();
      urls.addAll(getAdditionalURLS());
      urls.addAll(Arrays.asList(c.getURLs()));
      log.debug("Class loader URLS");
      for (URL u : c.getURLs()) {
        log.debug("  " + u.toExternalForm());
        ;
      }
      URLClassLoader newClassLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]));
      AtomicReference<Map<UUID, ? extends DefaultIBDataSet>> ref = new AtomicReference<>(null);
      Runnable r = new Runnable() {

        @Override
        public void run() {
          List<URL> kv;
          //      kv = CPScanner.scanResources(new ResourceFilter().packageName("META-INF").resourceName("MANIFEST.MF"));
          //      log.info("Acquired " + kv.size() + " META-INF urls from classpath");
          kv = CPScanner.scanResources(new ResourceFilter().packageName(IBDATA).resourceName(IBDATASET_XML));
          log.info("Acquired " + kv.size() + " urls from classpath");
          //      Enumeration<URL> kv = cet.withReturningTranslation(() -> getClass().getClassLoader().getResources(IBDATA_IBDATASET_XML));
          Map<UUID, ? extends DefaultIBDataSet> x = kv.stream()
              //          enumerationAsStream(
              //          // This [should] get all the XML files in the classpath.  I don't want to use a CP scanner....
              //          kv, true)
              // Convert supplier URL to Extended Dataset
              .map(mapDataSetToDefaultIBDataSet)
              // Only get them if they're here  Optional::stream in Java 11
              .filter(Optional::isPresent).map(Optional::get)
              //         Collection
              .collect(toMap(k -> k.getId(), identity()));
          ref.set(x);
        }

      };
      Thread t = new Thread(r);
      t.setContextClassLoader(newClassLoader);
      t.start();
      while (t.isAlive()) {
        cet.withTranslation(() -> Thread.sleep(100L));
      }
      cachedDataSets.putAll(ref.get());
    }
    return cachedDataSets.size();
  }

  @Override
  public Optional<IBDataSet> fetchDataSetById(UUID id) {
    return Optional.ofNullable(cachedDataSets.get(id));
  }

  @Override
  public Optional<IBDataStream> fetchDataStreamById(UUID id) {
    return cachedDataSets.values().stream().flatMap(ds -> ds.getStreamSuppliers().stream())
        .filter(s -> s.get().getId().equals(id)).findFirst().map(Supplier::get);
  }

  @Override
  public Optional<IBDataStream> fetchDataStreamByMetadataPatternMatcher(Map<String, Pattern> patternMap) {
    throw new IBDataException("not implemented yet");
    //    return Optional.empty();  // FIXME implement fetchDataStreamByMetadataPatternMatcher
  }

  @Override
  public List<UUID> getAvailableIds() {
    return cachedDataSets.keySet().stream().collect(toList());
  }

}
