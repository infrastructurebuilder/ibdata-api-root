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

/**
 *
 * @author mykel.alvis
 *
 *         <p>
 *         A subset of serialization framework types supported by IBData. Note
 *         that these types are what IBData uses internally for translation
 *         between other schema types and formats. Formatters for a given time
 *         can be supplied that allow translation between a given set of
 *         internal formats, and it is possible that the list will expand over
 *         time.
 *         </p>
 *
 *         <p>
 *         As a practice, most number types (INT, LONG, FLOAT, etc) are going to
 *         be handled as some underlying type representation (eg BigInteger) but
 *         will be managed as a string using the string serializers, eventually
 *         reconstituted at the far end as whatever time is most appropriate for
 *         the target sink.
 *         </p>
 *
 *         <p>
 *         Not all formats of all types are supported. As Avro and ProtoBuf are
 *         the "default" instances of schema, with database types as a target
 *         faucet and sink, not every type of every serialization framework will
 *         have an implementation here.
 *         </p>
 *
 *         Not directly supported:
 *
 *         <OL>
 *         <LI>RECORD -> Stored as either an expanded field set or as a separate
 *         stream</LI>
 *         <LI>ARRAY -> TBD</LI>
 *         <LI>MAP -> TBD</LI>
 *         <LI>FIXED -> Stored as some other underlying mechanism</LI>
 *         <LI>UNION -> TBD</LI>
 *         <LI>NULL -> Not actually a type</LI>
 *         </OL>
 */
public enum IBDataStructuredDataMetadataType {

  /**
   * The means for comprehending this type were unknown at ingestion time.  Sorry.
   *
   */
  UNKNOWN,
  /**
   * A STREAM is the primary special case, and is the default schema type for all
   * unstructured data. A STREAM indicates a "single record consisting of all the
   * bytes in the DataStream". You might cleverly note that this means "the file
   * contents"
   *
   * A schema may have at most 1 STREAM field. If it has a STREAM field, it may
   * have no other fields.
   */
  STREAM,
  /**
   * A KEY is a String that most accurately maps as a key field in the target data
   * stream. This usually defaults to an auto-increment integer, random id, or ROWID
   */
  KEY,

  /**
   *
   * An ID is a calculated UUID from the SHA-512 of the data elements of this
   * record, stored as a UTF-8 STRING . For a singleton (i.e. unstructured data),
   * this value equals the value of the stored bytes. For a record, this value is
   * a UUID from the SHA-512 of the <i>record's</i> bytes If a stream is REF'd in
   * any way, the ID is required. Otherwise, it's optional but is generated by
   * default. Removing an ID will likely result in a speed-up of ingestion and
   * transformation.
   *
   * Currently not in use
   */
//  ID,
  /**
   * Stored as an INT, with an associated map of values.
   */
  ENUM,
  /**
   * Stored as a UTF-8 STRING, with a reference to some other Stream record
   * identified by this REF. Note that the refer value points to the other records
   * identifier element, which is a <code>UUID:UUID</code> item that identifies
   * it's source STREAM id and it's source ITEM ID.
   */
  REF,
  /**
   * UTF-* string
   */
  STRING,
  /**
   * [B,C,*]LOB. Stored directly as the provided bytes, and assumed to be
   * retrieved as such (depending on implementation). This can be used to produce
   * any arbitrary stream of bytes necessary.
   *
   * By default, it is the metadata type of unstructured data
   */
  BYTES,
  /**
   * small signed / sint32 handled as a BigInteger
   */
  INT,
  /**
   * large signed / sint64 handled as a BigInteger
   */
  LONG,
  /**
   * small unsigned integer handled as a BigInteger
   */
  UINT,
  /**
   * large unsigned integer handled as a BigInteger
   */
  ULONG,
  /**
   * float handled as a BigDecimal
   */
  FLOAT,
  /**
   * double handled as a BigDecimal
   */
  DOUBLE,
  /**
   * boolean
   */
  BOOLEAN,
  /**
   * Stored as days since the epoch in a long (per the Avro mechanism)
   */
  DATE,
  /**
   * Stored as microseconds without denoted TZ as a LONG (Signed 64-bit integer)
   */
  TIMESTAMP,
  /**
   * Stored as microseconds since midnight
   */
  TIME,

  /**
   *
   * THIS IS DUMB.
   *
   * A special IBData-specific type to denote that a given set of unstructured
   * data is itself actually somewhat structured. Specifically, the structure is a
   * set of strings delimited by newlines.
   *
   * Having a metadata type set to this as an output probably be very unusual, but
   * a lot of inbound ingested data is likely to be in this format. This includes
   * CSV files, text files of prose text, and any other "lines of text" format.
   *
   * Note that this metadata type does not specify the target MIME type associated
   * with the stream. That is an entirely different matter.
   */
  NLDELIMITEDSTRINGS,

  /**
   * Newline delimited JSON, is precisely NLDELIMITEDSTRINGS but the expected type
   * of the string is JSON parseable
   */
  NLDELIMITEDJSON,

  /**
   * Supported as a const if the implementing framework allows it. Otherwise, used
   * as a replacement value.
   *
   * Note that substitutions in re: the format must be managed by the implementer.
   */
  CONST

}
