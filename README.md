# Build Character Backend [![Build Status](https://travis-ci.com/Renanxc/build-character-backend.svg?branch=master)](https://travis-ci.com/Renanxc/build-character-backend)

This repository contains an Rest Api that consumes the public Api for RPG data to create a session to build your own Character.

# Dependencies
install the following technologies before you start
- java SDK (oraclejdk8/openjdk8)
- Maven
- Docker

# Folder Structure
```
.
├── Dockerfile
├── README.md
├── docker-compose.yml
├── newrelic
│   ├── newrelic.jar
│   └── newrelic.yml
├── pom.xml
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── rakuten
    │   │           └── buildcharacterbackend
    │   │               ├── Application.java
    │   │               ├── api
    │   │               │   └── v1
    │   │               │       ├── BuildController.java
    │   │               │       ├── BuildOptionController.java
    │   │               │       ├── CustomizedExceptionHandler.java
    │   │               │       ├── SessionController.java
    │   │               │       └── exception
    │   │               │           └── TTlOutOfRangeException.java
    │   │               ├── domain
    │   │               │   ├── dto
    │   │               │   │   ├── request
    │   │               │   │   │   ├── CharacterRequest.java
    │   │               │   │   │   └── SessionRequest.java
    │   │               │   │   └── response
    │   │               │   │       ├── RPGCharacterDataStructure.java
    │   │               │   │       ├── SessionResponse.java
    │   │               │   │       └── error
    │   │               │   │           ├── ErrorCause.java
    │   │               │   │           └── ErrorResponse.java
    │   │               │   └── entity
    │   │               │       └── Session.java
    │   │               ├── infrastructure
    │   │               │   ├── client
    │   │               │   │   ├── Response
    │   │               │   │   │   └── RpgCharacterClientResponse.java
    │   │               │   │   ├── RpgCharacterClient.java
    │   │               │   │   ├── RpgCharacterClientOptions.java
    │   │               │   │   ├── RpgCharacterClientOptionsToClasses.java
    │   │               │   │   └── RpgCharacterClientStrategy.java
    │   │               │   ├── configuration
    │   │               │   │   ├── HomeController.java
    │   │               │   │   ├── RedisConfig.java
    │   │               │   │   ├── SwaggerConfig.java
    │   │               │   │   └── security
    │   │               │   │       └── SecurityConfig.java
    │   │               │   └── repository
    │   │               │       └── SessionRepository.java
    │   │               ├── service
    │   │               │   ├── RpgCharacterService.java
    │   │               │   └── SessionService.java
    │   │               └── util
    │   │                   ├── Mapper.java
    │   │                   └── TtlUtils.java
    │   └── resources
    │       ├── ValidationMessages.properties
    │       ├── application-dev.yml
    │       ├── application-prd.yml
    │       ├── application.yml
    │       ├── banner.txt
    │       └── log4j2.xml
    └── test
        ├── java
        │   └── com
        │       └── rakuten
        │           └── buildcharacterbackend
        │               ├── ApplicationTests.java
        │               ├── api
        │               │   └── v1
        │               │       ├── BuildControllerTest.java
        │               │       ├── BuildOptionControllerTest.java
        │               │       └── SessionControllerTest.java
        │               ├── infrastructure
        │               ├── integration
        │               │   ├── BuildControllerIT.java
        │               │   ├── BuildOptionControllerIT.java
        │               │   └── SessionControllerIT.java
        │               ├── service
        │               │   ├── RpgCharacterServiceTest.java
        │               │   └── SessionServiceTest.java
        │               ├── testUtil
        │               │   ├── CharacterRequestCreator.java
        │               │   ├── OptionsStrategyRequestCreator.java
        │               │   ├── RpgCharacterClientResponseCreator.java
        │               │   ├── SessionMockConstants.java
        │               │   ├── SessionRequestCreator.java
        │               │   └── SessionResponseCreator.java
        │               └── util
        │                   ├── MapperTest.java
        │                   └── TtlUtilsTest.java
        └── resources
            └── application.yml
```

# Set up
Clone the branch to your computer (git clone git@github.com:Renanxc/build-character-backend.git)

Then you can follow the next steps:

# Start Locally
## I. Without Docker
```shell
1. Build jar (maven)
$ mvn package

2. Run jar locally
$ java -jar target/app.jar

3. Open browser and access localhost:8090
http://localhost:8090

4. You shall see the swagger-ui.
```

## II. With Docker
```
1. Build and run it locally
$ docker-compose -f docker-compose-dev.yml up --build

2. Open browser and access localhost:8090
http://localhost:8090
```

# Documentation
It was documented with lib Swagger: localhost:8090
But feel free to see the document hosted on Heroku too: https://build-character-backend.herokuapp.com (The user was setted as user:user).

# Tests
You can run the tests with:
```
Unit Tests
- mvn test unit-tests

Integration Tests
- mvn test integration-tests
```

# Logs
You can see logs generated in ./logs after the first run command.

# Tools used in this project
 - RedisLab
 - Heroku
 - NewRelic
 - DockerHub
 - TravisCI