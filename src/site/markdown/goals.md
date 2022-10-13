# Goals

## What Is IBData Supposed To Accomplish?

### Immutability

## How?

### Ingestion

#### Ingest Schema From Some Source

#### Ingest Stream With Default Schema

#### Ingest Stream Mapping To Known Schema

#### Ingest Stream and Accompanying Schema

### Transformation

#### Transform Stream To Different Stream Of Same MIME Type

Examples include:

* Changing a JPG to a lower-resolution JPG, like a thumbnail
* Changing a binary stream to a stream of hex characters

#### Transform Stream to Different Stream of Different MIME Type As Single Operation

#### Transform Stream to Record-based Stream


#### Transform Record-based Stream to Different Record-based Stream

##### Filtering

* Remove every 3rd record
* Remove a record where the field "name" is not capitalized

##### Change Type of Record

* Read CSV file and write Avro stream