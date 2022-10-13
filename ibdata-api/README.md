# ibdata-api

API for IBData, a data acquisition and transformation framework for use primarily within Maven.


This code is insufficiently tested at ${test.coverage.percentage.required} %

## Schema

The IBData system can handle an arbitrary number of data schema types.  However, it does so by using an
interim format  The IBData schema format is a versioned model of types of structured data.

Current implementations exist for Apache Avro and Google Protocol Buffers.

The protocol aspects of these elements, as well as any for other types, are not relevant to the write of IBData.  IBData deals
exclusively with the capture and delivery of data artifacts, not the processing or execution of them.  This any actions, errors,
methods, servers, or whatever that are not data definitions within various

If your data problem does not deal with either entirely unstructured data or structured data with well-defined schema, then you're probably not
going to enjoy working with IBData.