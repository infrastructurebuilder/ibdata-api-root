# ibdata-default-transformers

Just a quick list of the "default" transformers.  These are available from this artifact (`ibdata-default-transformers`).

## Transformers


| Hint | Effect | Parameters |
| ---- | ------ | ---------- |
| `pass-thru` | Does nothing | none |
| `add-stream` | **DO NOT USE** Brute-force add a stream to the final DataSet | `addedPath` Path to the new stream (moving to a URL) |


## Record Transformers

| Hint | Accepts | Produces | Effect | Parameters |
| ---- | ------- | -------- | ------ | ---------- |
| `string-trim` | `String` | `String` | Trims lines that are processed String entries (like csv, etc) | none |
| `regex-line-filter` | `String` | `String` | Filters lines based on a supplied regex.  | `regex` - filtering regex (Defaut: `.*`) |
| `random-line-filter` | Anything | Same as Accepted Type | Filters lines based on a random number generator.  | `percentage` - Percentage of lines as a floating point number (Default : `.5`) |
| `regex-array-split` | `String` | `Array[String]` | Splits a line (like a CSV) to an array using a simple regex  | `regex` - Value to split on (Default : `,`) |
| `array-to-numbered-column` | `Array[String]` | `Map[String,String]` | Maps an array to a map with keys based on a pattern supplied with the index  | `format` - Map key format (Default : `COLUMN%00d`) |
| `array-to-name-map` | `String[]` | `Map[String,String]` | Maps an array to a map with keys based on a list of keys index by field position within the array | `fields` - a List of fields ( [see below](#list-of-fields) ) (Default : none) |
| `tostring-array-join` | `Object[]` | `String` | Joins an array back into a string, joined by a delimiter and optional bounding prefix and suffix.  Each value has `.toString()` applied to acquire the actual value| `delimiter` - Value between items  (Default : `,`)<br/>`prefix` - prepended to the string if present<br/>`suffix` appended to string if `prefix` is present (also, value for `prefix` is used if no `suffix` is defined) |

## Record Finalizers

| Hint | Accepts | Produces | Effect | Parameters |
| ---- | ------- | -------- | ------ | ---------- |
| `string` | `text/plain` | `text/plain` and equivalents (like `text/csv`, `text/tab-separated-values`, and `text/pipe-separated-values`) | Writes a file of lines of strings (MimeType defaults to `text/plain` but can be overriden) | `numberOfRowsToSkip` Skip the supplied number of rows (Default: `0`) |




## List of Fields

The list of fields in the `array-to-name-map` is described in the `<configuration>` element as follows:

```
  <configuration>
    <fields>
      <field>field_name_1</field>
      <field>field_name_2</field>
      <field>last_Field_named</field>
    </fields>
  </configuration>
```

In this case, the string `A,B,C,D` would be mapped to the map `field_name_1=A, field_name_2=B, last_Field_named=C`, as the order matters.  Without a corresponding field in the list, the value `D` gets dropped.

## Notes

The `regex-array-split` is a fairly simplistic field splitter.  It does not recognize that the split `regex` delimiter value can be inside quotes, which is often the case with deliminated files.
The upcoming `opencsv-to-name-map` transformer will deal with this problem.