/*
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
package org.infrastructurebuilder.data.model;

import static java.util.Objects.requireNonNull;
import static org.infrastructurebuilder.data.IBDataException.det;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.infrastructurebuilder.data.IBDataException;
import org.infrastructurebuilder.data.IBDataStream;
import org.infrastructurebuilder.data.IBSchema;
import org.infrastructurebuilder.data.IbdataApiVersioning;
import org.infrastructurebuilder.data.Metadata;
import org.infrastructurebuilder.util.core.Checksum;
import org.w3c.dom.Document;

public class IBMetadataUtils {

  public final static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
  public final static Supplier<DocumentBuilder> builderSupplier = () -> det.returns(() -> factory.newDocumentBuilder());
  public final static Supplier<Document> emptyDocumentSupplier = () -> builderSupplier.get().newDocument();

//  public final static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//  public final static Supplier<DocumentBuilder> builderSupplier = () -> cet
//      .withReturningTranslation(() -> factory.newDocumentBuilder());
  public final static Supplier<Metadata> emptyMetadataSupplier = () -> new Metadata();

  private final static TransformerFactory tf = TransformerFactory.newInstance();
  private final static Supplier<Transformer> tfSupplier = () -> {
    return det.returns(() -> tf.newTransformer());
  };

//  /**
//   * Map an object to a W3C Document. If null, returns an empty Document
//   */
//  public final static Function<Object, Xpp3Dom> fromXpp3Dom = (document) -> ofNullable(
//      // This might be null
//      IBDataException.det.withReturningTranslation(() ->
//      // This might just fail
//      (ofNullable(document).orElseGet(() -> new Metadata()) instanceof Xpp3Dom)
//          // IS it a document
//          ? (Xpp3Dom) document
//          :
//          // Not a document
//          (builderSupplier.get().parse(new InputSource(new StringReader(document.toString()))))
//      // translate Exception
//      )
//  // ofNullable
//  )
//
//      .orElseGet(emptyDocumentSupplier);

  public final static Function<Object, String> stringifyDocument = (document) -> det.returns(() -> {
    StringWriter writer = new StringWriter();
    if (document instanceof Document)
      det.translate(() -> tfSupplier.get().transform(new DOMSource((Document) document), new StreamResult(writer)));
    else
      writer.append(document.toString().trim());
    return writer.toString();
  });

  public final static Function<Object, Metadata> translateToMetadata = (document) -> det.returns(() -> {
    if (document == null)
      return new Metadata();
    if (document instanceof Metadata)
      return (Metadata) document;
    if (document instanceof Document) {
      Document d = (Document) document;
      if (d.hasAttributes() || d.hasChildNodes())
        return new Metadata(Xpp3DomBuilder.build(new StringReader(stringifyDocument.apply(d))));
      else
        return new Metadata();
    } else
      return new Metadata(Xpp3DomBuilder.build(new StringReader(document.toString()), true));
  });

  public final static Function<Xpp3Dom, Checksum> asChecksum = (dom) -> {
    return new Checksum(new ByteArrayInputStream(dom.toString().getBytes(StandardCharsets.UTF_8)));
  };

  public final static Function<IBDataStream, DataStream> toDataStream = (ibds) -> {
    DataStream ds = new DataStream();
    ds.setName(requireNonNull(ibds).getName());
    ds.setCreationDate(ibds.getCreationDate().toString());
    ibds.getDescription().ifPresent(ds::setDescription);
    ibds.getUrl().ifPresent(ds::setUrl);
    ds.setMetadata(ibds.getMetadata());
    ds.setMimeType(ibds.getMimeType());
    ds.setSha512(ibds.getChecksum().toString());
    ds.setUuid(ibds.getChecksum().asUUID().get().toString());
    ds.setPath(ibds.getPath());
    ibds.getPathAsPath().ifPresent(p -> ds.setOriginalLength(det.returns(() -> Files.size(p))));
    ibds.getStructuredDataMetadata().ifPresent(smd -> {
      DataStreamStructuredMetadata newSMD = new DataStreamStructuredMetadata();
      ds.setStructuredDataDescriptor(newSMD);
    });
    return (DataStream) ds.clone();
  };

  public final static Function<IBSchema, DataSchema> toDataSchema = (ibds) -> {
    DataSchema ds = new DataSchema();
    ds.setName(requireNonNull(ibds).getName().orElseThrow(() -> new IBDataException("Name is required")));
    ds.setCreationDate(ibds.getCreationDate().toString());
    ds.setDescription(ibds.getDescription().orElseThrow(() -> new IBDataException("Description is required")));
    ds.setMetadata(ibds.getMetadata());
    Checksum c = ibds.asChecksum();
    ds.setSha512(c.toString());
    ds.setUuid(c.asUUID().get().toString());
    SchemaType schemaType = new SchemaType();
    schemaType.setVersionedProvider(IbdataApiVersioning.getXMLCoordinates());
    ds.setSchemaType(schemaType);
    List<SchemaAsset> schemaAssets = new ArrayList<>();
    ibds.getSchemaResourcesMappedFromName().forEach((k, v) -> {
      v.forEach(uuid -> {

      });
    });
    ds.setSchemaAssets(schemaAssets);
//    ibds.getPathIfAvailable().ifPresent(p -> {
//      ds.setOriginalLength(new Long(IBDataException.det.withReturningTranslation(() -> Files.size(p))).toString());
//    });
//    ibds.getStructuredDataMetadata().ifPresent(smd -> {
//      DataStreamStructuredMetadata newSMD = new DataStreamStructuredMetadata();
//
//      ds.setStructuredDataDescriptor(newSMD);
//    });
    return (DataSchema) ds.clone();
  };

//  /**
//   * Function to compare W3c Document instances (by string compare, like a filthy
//   * animal
//   */
  public final static BiFunction<Document, Document, Boolean> w3cDocumentEqualser = (lhs, rhs) -> {
    return stringifyDocument.apply(lhs).equals(stringifyDocument.apply(rhs));
  };
}