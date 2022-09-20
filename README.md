# user-api
Example of REST APIs written in Scala that allow to create and manage users in a SQL datastore.

## Technology stack

The project is using the following technology Stack:
* Scala
* Http4s
* Cats Effect
* Doobie
* Postgres


The Project uses a multi-mobule Sbt project, that's composed by the following modules:
* service: contains the code about the REST api itself;
* end-to-end: contains public api tests on the multiple REST APIs;
* performance: contains the load tests scenario for the REST APIs using Gatling;

## How to run
To run the application you need to have the following dependencies installed on your machine:
* `Scala` and `sbt` installed;
* `Java 11` installed;
* `Docker engine installed`.

To build the project to run it's needed to build the Docker image with command `sbt 'service/docker:publishLocal`.

To run the end-to-end tests you can do it with the command `sbt 'end-to-end/test'.`

Instead to run the performance test in your local machine the procedure is the following:
* `dockerComposeUp:` to run the Postgres instance and the rest api container;
* `sbt 'project performance; gatling:test':` to run the performance test using Gatling on the local machine.