Alerts (safetynet)

An api for read file data persons, medical record and firestation mapping. This api uses Spring Boot to run.

Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

Prerequisites

What things you need to install the software and how to install them

Java 1.8

Maven 4.0.0

Spring boot 2.4.3

Installing

A step by step series of examples that tell you how to get a development env running:

1.Install Java:
https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html

2.Install Maven:
https://maven.apache.org/install.html

3. Install Spring tools suit
https://spring.io/tools

Running App

Post installation of Spring Boot, Java and Maven, you will have to be ready to import the code into an IDE (sts) of your choice and run the server to launch
the application.
Now you can see more at localhost:8080/ with request (see documentation for more details)

Testing

The app has unit tests written.
To run the tests from maven, go to the folder that contains the pom.xml file and execute the below command.
mvn test and mvn site to produce some reports (surefire, JaCoCo).

Testing API
Use postman is the good way  To test all request we have
To install postman
https://www.postman.com/

DOCUMENTATION API
http://localhost:8080/swagger-ui.html#/
