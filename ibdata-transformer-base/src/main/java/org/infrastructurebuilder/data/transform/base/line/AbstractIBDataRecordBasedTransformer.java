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
package org.infrastructurebuilder.data.transform.base.line;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static org.infrastructurebuilder.data.IBDataConstants.MAP_SPLITTER;
import static org.infrastructurebuilder.data.IBDataConstants.RECORD_SPLITTER;
import static org.infrastructurebuilder.data.IBDataConstants.TRANSFORMERSLIST;
import static org.infrastructurebuilder.data.model.IBDataModelUtils.getStructuredSupplyTypeClass;
import static org.infrastructurebuilder.exceptions.IBException.cet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.System.Logger;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.infrastructurebuilder.data.DefaultIBDataSet;
import org.infrastructurebuilder.data.DefaultIBDataTransformationResult;
import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.data.IBDataSet;
import org.infrastructurebuilder.data.IBDataStream;
import org.infrastructurebuilder.data.IBDataStreamRecordFinalizer;
import org.infrastructurebuilder.data.model.DataStream;
import org.infrastructurebuilder.data.model.IBMetadataUtils;
import org.infrastructurebuilder.data.transform.AbstractIBDataTransformer;
import org.infrastructurebuilder.data.transform.IBDataTransformationResult;
import org.infrastructurebuilder.util.config.ConfigMap;
import org.infrastructurebuilder.util.config.ConfigMapSupplier;
import org.infrastructurebuilder.util.config.DefaultConfigMapSupplier;
import org.infrastructurebuilder.util.constants.IBConstants;
import org.infrastructurebuilder.util.core.Checksum;
import org.infrastructurebuilder.util.core.IBUtils;

abstract public class AbstractIBDataRecordBasedTransformer extends AbstractIBDataTransformer {
  private static final String IMPOSSIBLECLASSNAME = "_impossibleclassname###";
  private final Map<String, IBDataRecordTransformerSupplier> dataLineSuppliers;
  private final List<IBDataRecordTransformer<?, ?>> configuredTranformers;
  private final IBDataStreamRecordFinalizer configuredFinalizer;
  private final Optional<Class<?>> finalType;
  private int countOfRowsSkippedSoFar = 0;
  private final List<String> firstType;

  protected AbstractIBDataRecordBasedTransformer(Path workingPath, Logger log, ConfigMap config,
      Map<String, IBDataRecordTransformerSupplier> dataRecTransformerSuppliers, IBDataStreamRecordFinalizer finalizer) {
    super(workingPath, log, config);
    List<Class<?>> _fType = Collections.emptyList();
    this.dataLineSuppliers = dataRecTransformerSuppliers;
    this.configuredFinalizer = finalizer;
    if (config != null && config.containsKey(TRANSFORMERSLIST)
    /* && config.containsKey(UNCONFIGURABLEKEY_FINALIZER_KEY) */) {
      Map<String, IBDataRecordTransformerSupplier<?, ?>> map = new HashMap<>();
      this.dataLineSuppliers.forEach((k, v) -> {
        map.put(k, v);
      });
      // this.failOnError =
      // ofNullable(config.get(FAIL_ON_ERROR_KEY)).map(Boolean::parseBoolean).orElse(false);
      ConfigMapSupplier lcfg = new DefaultConfigMapSupplier().addConfiguration(config);
      // If the map contains the key, then the suppliers MUST contain all indicated
      // line transformers
      Optional<String> transformersList = ofNullable(config.getString(TRANSFORMERSLIST));
      Optional<String> theListString = transformersList.map(IBUtils.nullIfBlank);
      List<String> theList = theListString.map(str -> Arrays.asList(str.split(Pattern.quote(RECORD_SPLITTER))))
          .orElse(new ArrayList<>());
      if (theList.size() > 0) {
        Map<String, String> idToHint = theList.stream().map(s -> s.split(Pattern.quote(MAP_SPLITTER)))
            .collect(Collectors.toMap(k -> k[0], v -> v[1]));
        ArrayList<String> tList = new ArrayList<>(idToHint.values());
        tList.removeAll(dataRecTransformerSuppliers.keySet());
        if (tList.size() > 0)
          throw new IBDataException("Missing record transformers " + tList + "\n Available transformers are "
              + dataRecTransformerSuppliers.keySet());
        configuredTranformers = new ArrayList<>();
        Optional<Class<?>> previousType = Optional.empty();
        boolean first = true;
        for (String li : theList) {
          String[] s = li.split(Pattern.quote(MAP_SPLITTER));
          IBDataRecordTransformerSupplier<?, ?> s2 = map.get(s[1]).configure(lcfg);
          IBDataRecordTransformer<?, ?> transformer = s2.get()/* .configure(lcfg.get()) */;
          if (acceptable(previousType, transformer.accepts()))
            previousType = transformer.produces();
          else
            throw new IBDataException("Transformer " + s[0] + "/" + transformer.getHint() + " only responds to "
                + transformer.accepts().get() + " and it's predecessor produces " + previousType.orElse(null));
          if (first) {
            first = false;
            _fType = transformer.accepts().orElse(Arrays.asList(Object.class));
          }
          configuredTranformers.add(transformer);
        }
        this.finalType = previousType;
      } else {
        this.configuredTranformers = null;
        this.finalType = Optional.empty();
      }
    } else {
      this.configuredTranformers = null;
      this.finalType = Optional.empty();
    }
    this.firstType = _fType.stream().map(Class::getCanonicalName).collect(Collectors.toList());
  }

  @Override
  public boolean respondsTo(IBDataStream i) {
    return (!firstType.isEmpty()
        && getStructuredSupplyTypeClass(i.getMimeType()).map(firstType::contains).orElse(false));
  }

  private boolean acceptable(Optional<Class<?>> previous, Optional<List<Class<?>>> inbound) {
    boolean retVal = (!previous.isPresent() || !inbound.isPresent());
    if (!retVal) {
      List<Class<?>> l = inbound.get();
      Class<?> p = previous.get();
      for (Class<?> c : l) {
        if (c.isAssignableFrom(p))
          return true;
      }
      // inbound.get().stream().anyMatch(inboundClass ->
      // inboundClass.isAssignableFrom(previous.get()));
    }
    return retVal;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public IBDataTransformationResult transform(Transformer transformation, IBDataSet ds,
      List<IBDataStream> suppliedStreams, boolean failOnError) {
    IBDataStreamRecordFinalizer cf = getConfiguredFinalizer();
    if (!acceptable(finalType, cf.accepts()))
      throw new IBDataException(
          "Finalizer '" + cf.getId() + "' does not accept finally produced type '" + finalType.get() + "'");
    return localTransform(transformation, ds, suppliedStreams, getConfiguredFinalizer(), failOnError);
  }

  public List<IBDataRecordTransformer<?, ?>> getConfiguredTransformers() {
    return ofNullable(configuredTranformers)
        .orElseThrow(() -> new IBDataException("No list of configured record transformers"));
  }

  protected Map<String, IBDataRecordTransformerSupplier> getDataLineSuppliers() {
    return dataLineSuppliers;
  }

  protected IBDataStreamRecordFinalizer getConfiguredFinalizer() {
    return configuredFinalizer;
  }

  /*
   * TODO Make this a (non-parallel!) stream process private Stream<String>
   * streamFor(IBDataStreamSupplier ibds) { try (InputStream ins =
   * Objects.requireNonNull(ibds).get().get()) { return
   * IBUtils.readInputStreamAsStringStream(ins); } catch (IOException e) { throw
   * new IBDataException(e); } }
   */

  // This is a LITTLE bit dangerous
  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected String processStream(IBDataStream stream, IBDataStreamRecordFinalizer finalizer,
      Map<String, List<Long>> errors, List<Throwable> errorList) {
    return cet.withReturningTranslation(() -> {
      int skipRows = finalizer.getNumberOfRowsToSkip();
      Class<?> inbound;
      try (BufferedReader r = new BufferedReader(new InputStreamReader(stream.get()))) {
        String line;
        inbound = String.class;
        Optional<Object> inboundObject = empty(), s;
        long lineCount = 0;
        while ((line = cet.withReturningTranslation(() -> r.readLine())) != null) {
          lineCount++;
          if (skipRows > 0) {
            skipRows -= 1;
            getLog().debug("Skipping row " + lineCount);
            continue;
          }
          // log.info(String.format("Line %05d '%s'", lineCount, line));
          s = of(line);
          for (IBDataRecordTransformer t : getConfiguredTransformers()) {

            inboundObject = ofNullable(t.apply(s.get()));
            // log.info(String.format(" '%s'", line));
            if (!inboundObject.isPresent()) {
              errors.computeIfAbsent(t.getHint(), k -> new ArrayList<Long>()).add(lineCount);
              break;
            }
            s = inboundObject;
            inbound = (Class<?>) t.produces().orElse(inbound);
          }

          inboundObject.ifPresent(l -> {
            // log.info(String.format(" as '%s'", l));
            finalizer.writeRecord(l).ifPresent(e -> errorList.add((IBDataTransformationError) e));
          });

        }
      }
      return (String) finalizer.produces().orElse(stream.getMimeType());
    });
  }

  protected IBDataTransformationResult localTransform(Transformer t, IBDataSet ds2, List<IBDataStream> suppliedStreams,
      IBDataStreamRecordFinalizer finalizer, boolean failOnError) {
    requireNonNull(finalizer, "No finalizer supplied to localTransform");
    final Map<String, List<Long>> errors = new HashMap<>();
    final List<Throwable> errorList = new ArrayList<>();
    Map<UUID, Supplier<IBDataStream>> map = new HashMap<>();

    String finalType = null;
    for (IBDataStream stream : Stream
        .concat(requireNonNull(ds2, "Supplied transform dataset").asStreamsList().stream(), suppliedStreams.stream())
        .collect(Collectors.toList())) {
      if (this.respondsTo(stream)) {
        finalType = processStream(stream, finalizer, errors, errorList);
      }
    }
    cet.translate(() -> finalizer.close());
    Path targetPath = finalizer.getWorkingPath();
    Checksum c = new Checksum(targetPath);
    ds2.getStreamSuppliers().forEach(ss -> map.put(ss.get().getId(), ss));
    DataStream newStream = new DataStream();
    newStream.setMimeType(Optional.ofNullable(finalType).orElse(IBConstants.APPLICATION_OCTET_STREAM));
    newStream.setMetadata(IBMetadataUtils.translateToXpp3Dom.apply(t.getTargetStreamMetadataAsDocument()));
    newStream.setCreationDate(Instant.now().toString());
    newStream.setUuid(c.asUUID().get().toString());
    newStream.setUrl(cet.returns(() -> targetPath.toUri().toURL().toExternalForm()));
    newStream.setSha512(c.toString());
    newStream.setDataStreamDescription(t.getTransformation().getDescription());
    newStream.setDataStreamName(t.getTransformation().getName());
    Supplier<IBDataStream> x = finalizer.finalizeRecord(newStream);
    map.put(x.get().getId(), x);
    IBDataSet newSet = new DefaultIBDataSet(ds2).withStreamSuppliers(map);
    return new DefaultIBDataTransformationResult(ofNullable(newSet), errorList, getWorkingPath());
  }

}
