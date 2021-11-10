# user-api
Example of REST APIs written in Scala that allow to create and manage users in a SQL datastore.

## Technology stack

The project is using the following technology Stack:
* Scala
* Https
* Cats Effect
* Doobie
* Postgres


The Project use a multi-mobule Sbt project, that's composed by the following modules:
* service: contains the code about the REST api itself;
* end-to-end: contains tests on the multiple REST APIs;
* performance: contains the perfomance test scenario for the REST APIs using Gatling;

## How to run
To run the application you need to have `sbt` and Java 8 installed on your machine and run the following commands:
* `sbt project service; docker:publishLocal` to generate the docker-image;
* `sbt project performance; dockerComposeUp` to run 