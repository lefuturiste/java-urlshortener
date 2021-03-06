# Urlshortner web server in Java

A simple web API made in Java to manage and short urls!

## Installation

You will soon be capable of downloading .jar directly from the github release. But for now you must build your own .jar by cloning this repository.

## Docker

You easily deploy the web app using docker, you will need to use the `docker-compose.yml` provided in this repository.

### Requirements

- Maven in cli
- JDK 8 or newer
- MongoDB sync driver
- Json support
- JUnit to do the tests 

## Usage

Routes:

```
GET / ~ get basic information about the app

~ Rest API: ~
GET /api/url ~ get all the urls
GET /api/url/:id ~ get one url
POST /api/url ~ create a new url
PUT /api/url/:id ~ update one url
DELETE /api/url/:id ~ delete one url

GET /:slug ~ will redirect to the associated url
```

## Tests

This projet use JUnit to make Unit tests.

Run the test with maven:

`mvn test`

## Build

If you want to build the source code you must use maven:

`mvn clean compile assembly:single`

You will get a clean .jar in the `target/` directory

You can now execute the jar with the command:

`java -cp ./target/app.jar fr.lefuturiste.urlshortener.App`
