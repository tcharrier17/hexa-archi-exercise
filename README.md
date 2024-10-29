# hexa-archi-exercise

This repository contains a backend project based on `Quarkus` and `MongoDB`.
The API allows to manipulate a todo list for different users, thanks to a CRUD API.
Also, a very simple login process is implemented.

## Technical notes

Some concepts of the hexagonal architecture are used in this project, like the use of ports, 
adapters, and a decomposition in 3 modules: application, domain and infra, that are linked
via the concept of unversed dependencies. However, not all principles are respected, in order 
use Quarkus in a simple way (ex: the application chooses its adapters).

Some tests are implemented in the project, and some of them are integration tests.
The aim is to be able to identify the breaking changes easily by testing the application globally.

## Run the application

In order to run the application, you need to install `scala`, `sbt` and `podman` in your machine.
Please check the following documentation if it is not already the case:
TODO
- https://www.scala-lang.org/download/ (for scala)
- https://www.scala-sbt.org/download/ (for sbt)
- https://podman.io/docs/installation (for podman)

Then, open a terminal at `./dev/docker` and launch the required docker containers using:
```shell
podman-compose -p hexa-archi-exercise up -d
```

NB: To stop the containers, you can run

```shell
podman-compose -p hexa-archi-exercise down
```

To compile the backend, run:
```shell
sbt compile
```

To launch the tests, run:
```shell
sbt test
```

To launch the backend, run:
```shell
sbt run
```

By default, the application will be available in the port **9000**.

Note that you can test the API using the postman collection located [there](dev/postman/hexa-archi-exercise.postman_collection.json).