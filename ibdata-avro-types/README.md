# ibdata-avro-types

These tranformers (and finalizers) are for managing Avro types.

## Record Transformers

| Hint | Accepts | Produces | Effect | Parameters |
| ---- | ------- | -------- | ------ | ---------- |
| `map-to-generic-avro` | `Map[String,Object]` | `GenericRecord` | Maps a string map of fields to a schema-backed GenericRecord | <ul><li>`schema` - Path to schema (avsc)</li><li>`timestamp.formatter` - Timestamp format (Default : [`DateTimeFormatter.ISO_ZONED_DATE_TIME`](https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html)) </li><li>`time.formatter` - Time field type formatter (Default: `HH:MM` - 24 hour with hours 00-23 )</li><li>`date.formatter` - Date field type formatter (DEfault: `mm-DD-yy`) </li><li>`locale.language`- Locale (Default: default for system)</li><li>`locale.region` - Locale (Default: default for system)</li> </ul> |


## Record Finalizers

| Hint | Produces | Effect | Parameters |
| ---- | -------- | ------ | ---------- |
| `avro-generic` |  `avro/binary` | Writes a DataStream of Avro data (MimeType `avro/binary`) | `numberOfRowsToSkip` Skip the supplied number of rows (Default: `0`) |


## Notes

Currently, `ibdata-avro-types` supports the Avro logical types that the [Avro IDL supports](https://avro.apache.org/docs/current/idl.html#logical_types).

This means that logical types supported by `ibdata-avro-types` are:

*     decimal (logical type decimal)
*     date (logical type date)
*     time_ms (logical type time-millis)
*     timestamp_ms (logical type timestamp-millis)

`uuid` is has some support, but only from a `CharSequence` (like a `String`, for instance).

Specifically, `time-micros` is not supported.

Other logical types are not currently supported.
This means that if you are using a JSON-formatted schema that uses other logical types, the behavior of ibdata in general is unspecified.