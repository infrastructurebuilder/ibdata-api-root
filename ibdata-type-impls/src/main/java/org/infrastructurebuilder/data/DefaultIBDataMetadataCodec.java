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

//@Named
//@Singleton
//public class DefaultIBDataMetadataCodec implements IBDataMetadataCodec {
//  private final Path root;
//
//  @Inject
//  public DefaultIBDataMetadataCodec(PathSupplier workingPath) {
//    this.root = Objects.requireNonNull(workingPath).get();
//  }
//
//  @Override
//  public Path getRoot() {
//    return this.root;
//  }
//
//  @Override
//  public IBDataSet readDataSet(InputStream ins) {
//    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
//    DocumentBuilder builder = IBDataException.cet.withReturningTranslation(() -> builderFactory.newDocumentBuilder());
//    Document xmlDocument = IBDataException.cet.withReturningTranslation(() -> builder.parse(ins));
//
//    XPath xPath = XPathFactory.newInstance().newXPath();
//    String expression = "/Tutorials/Tutorial";
//    NodeList nodeList = (NodeList) IBDataException.cet.withReturningTranslation(() -> xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET));
//    return null;
//  }
//
//  @Override
//  public Map<UUID, Path> writeDataSets(Set<IBDataSet> datasets, Path targetPath) {
//    return null; // TODO implement writeDataSets(Set<IBDataSet>, Path)
//  }
//
//  @Override
//  public Path writeDataSet(IBDataSet dataSet) {
//
//    return null; // TODO implement writeDataSet
//  }
//
//  @Override
//  public IBDataSet transform(IBDataSetIdentifier dataSet) {
//    return null; // TODO implement transform(IBDataSetIDentifier)
//  }
//
//}
