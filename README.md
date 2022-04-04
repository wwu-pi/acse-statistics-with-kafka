# Statistics with message oriented middleware

This repository contains an example that shows how to use Kafka in a Spring application. The application calculates the average, minimum and maximum of numbers which are provided via a web form.

## Projects

The following two projects provide the application, which communicates through Kafka.
Both projects contain the application.properties, which define relevant configurations for the connection.
The first Kafka integration, represented by the functionality to add a new data point, uses the `KafkaTemplate` class offered by the Spring framework to send a message to a topic in Kafka.
The backend listens for messages that are sent to this topic and stores the corresponding data point in a repository.  

The second Kafka integration, represented by the functionality to call a aggregation function, uses the `ReplyingKafkaTemplate` to send a request and
synchronously wait for a response. Additional metadata is set so that Kafka can map the result to the awaiting caller.

### statistic-web
The Spring web project. It provides the web view using the MVC pattern. 

### statistics-backend
The backend project. It contains the two message listeners which consume the messages from Kafka topic. Check out the comments to understand
how queues are mimicked in Kafka via topics and consumer groups with the groupId-parameter. 

## How to run the application
Start a Kafka broker and an instance of Zookeeper. This is, for instance, possible using the provided `docker-compose.yml`.
To start the broker, open your command line and navigate to the project folder where the `docker-compose.yml` file is located.
Then, start it via `docker-compose up -d`.
 
Then start both projects by navigating into their root folder an then calling:

    ./mvnw spring-boot:run
    
The application is then available under [http://localhost:8080](http://localhost:8080).