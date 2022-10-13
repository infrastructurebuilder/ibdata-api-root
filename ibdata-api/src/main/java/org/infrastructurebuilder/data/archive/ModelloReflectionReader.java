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
package org.infrastructurebuilder.data.archive;

//public class ModelloReflectionReader<T> {
//  private final IBDataSourceModelXpp3Reader reader = new IBDataSourceModelXpp3Reader(); // No transformations
//  private final Class<T> clazz;
//
//  public ModelloReflectionReader(Class<T> clazz) {
//    this.clazz = Objects.requireNonNull(clazz);
//  }
//
//  public T readFromXpp3Dom(Xpp3Dom d) {
//    return readFromModel(new StringReader(d.toString()));
//  }
//
//  @SuppressWarnings("unchecked")
//  public T readFromModel(Reader r) {
//    return IBDataException.cet.withReturningTranslation(() -> {
//      XmlPullParser parser = new MXParser();
//      parser.setInput(r);
//
//      if (parser.nextTag() != XmlPullParser.START_TAG)
//        throw new IBDataException("No start tag");
//      return (T) Reflect.on(reader).call("parse" + clazz.getSimpleName(), parser, false).get();
//    });
//  }
//
//}
