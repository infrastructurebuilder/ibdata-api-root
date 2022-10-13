# ibdata-database-type-impls

Database (and Jooq) -specific classes for managing database wor

## Conversion Notes

* BigInt (`decimal_integer`) and Long are both mapped to the `long` avro type
* `Interval` types are stored as a `long` of Millis.  To use in code, you must use
  `Duration.ofMillis(val)`.
* `array` types appear to work, but the conversions are entirely untested at the
  moment. There is an [open issue](https://github.com/infrastructurebuilder/ibdata-reference-root/issues/28)
  about making those conversions validated.
* `enum` types are forced as a `string`.  There is currently an [open issue](https://github.com/infrastructurebuilder/ibdata-reference-root/issues/27)
  about determining the symbol list of an enumeration explaining how to use your
  own enumerations
* `xlobs` do no currently work.
