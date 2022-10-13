# ibdata-database-ingesters

This module provides ingesters and finalizers for ingesting data directly from a database.

Note that the use of this module effectively terminates provenance of data at the time of ingestion.  It is unlikely
that a repeat execution of a database ingestion will produce exactly the same results unless measures are taken to
ensure that this happens.  This includes providing proper sorting as well as ensuring that no actual changes were
made to the data.

The Infrastructure Builder team recommends that you never use this ingester.  It is, and will remain supported.  However, database
queries violate the ingestion construct with alarming frequency.  Using this ingester is almost always an indicator that you
are trying to take a dangerous shortcut.

You have been warned.


## Ingesters

| Hint | Effect | Parameters |
| --- | ------ | ---------- |
| `jdbc` | Selects records from a query and allows the finalizer to write them as Avro | <ul><li>`dialect` [org.jooq.SQLDialect](https://www.jooq.org/javadoc/3.12.x/org.jooq/org/jooq/SQLDialect.html) [Jooq dialect](https://www.jooq.org/doc/3.12/manual/sql-building/dsl-context/sql-dialects/) </li><li>`query` SQL Query to execute.  Not validated.</li> <li>`schema` Avro schema to write records as.  Optional.  If not provided, the system will attempt to infer.  If provided, must contain all fields in the `query` result</li><li>`namespace` Namespace if not schema is provided.  If not provided, the namespace is set to `org.infrastructurebuilder.data`</ul>|



## Finalizers

| Hint | Accepts | Produces | Effect | Parameters |
| ---- | ------- | -------- | ------ | ---------- |
| `default-jdbc` | Database input | `binary/avro` | Writes an Avro stream of records mapped from a query | `numberOfRowsToSkip` Skip the supplied number of rows (Default: `0`)  |
