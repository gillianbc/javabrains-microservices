# Microservices

Javabrains - https://github.com/koushikkothagal/spring-boot-microservices-workshop

https://www.youtube.com/watch?v=y8IQb4ofjDo&list=PLqq-6Pq4lTTZSKAFG6aCDVDP86Qx4lNas

# Installing and Running the Project
Clone the repository.
To open all 4 microservices projects in IntelliJ in one window, open the javabrains-microservices folder.
Drill down to the pom.xml of each.  Right-click and add as maven project.
Intellij will automatically make you a Spring run configuration
Run all 4.  
Go to the UI of the discovery server http://localhost:8761 to see what's up.    

# MICROSERVICES LEVEL 1 - COMMUNICATION AND SERVICE DISCOVERY

# Overview

Four separate Spring Boot Applications.  
* the catalog - e.g. http://localhost:8081/catalog/1     
* the movie info e.g. http://localhost:8082/movies/1
* the ratings e.g. http://localhost:8083/ratings/1
* the discovery server http://localhost:8761

The catalog is the daddy - it shows the name/desc of favourite films for a user along with his ratings.  (I only have user id 1)

The name/desc of a film comes from the movie info microservice.  (Only ids 1 and 2 available)

The rating of a film comes from the ratings microservice.  (Only ids 1 and 2 available)

# Notes
The port is configure in `application.properties` - by default it is 8080.

If you're calling another API and unmarshalling using RestTemplate, make sure your target class has the default constructor.

We can use RestTemplate to call another API.  As we don't want to create a new RestTemplate every time, we can annotate a method that returns us a new RestTemplate with `@Bean`

Note that RestTemplate is deprecated.  We should use WebClient which is asynchcronous i.e. reactive or synchronous (if we use block()).  You can select WebClient via the Spring initializr or add it later.  This sample project doesn't use WebClient - but I have shown how to use that in a branch https://github.com/gillianbc/javabrains-microservices/blob/webclient/movie-catalog-service/movie-catalog-service/src/main/java/com/gillianbc/moviecatalogservice/resource/MovieCatalogResource.java


```
server.port=8081
spring.application.name=movie-catalog-service
```

# Array Responses are Bad

When designing REST endpoints, *NEVER* have an array of objects as the return type.  It's possible, but bad practice.  If you want to add in some other field later, outside of the array, e.g. a count, then you will cause a breaking change in all the consumer code.  
Always have an enclosing object e.g.
```
{
    "userId": "1",
    "catalogItemList": [
        {
            "name": "Fried Green Tomatoes at the Whistlestop Cafe",
            "desc": "desc",
            "rating": 4
        },
        {
            "name": "Shawshank Redemption",
            "desc": "desc",
            "rating": 3
        }
    ]
}
```

# Copies of Classes - Not Dependencies

In a microservice architecture, it IS appropriate to copy a pojo class into another application rather than having a dependency.  The class can be stripped down to what's actually necessary in the consuming class and changes to the original don't necessarily affect the copy.  

# Discovery Server

## Client-side Discovery
The discovery service is like a phonebook:
* Client asks the Discovery Server, where can I find service X?
* Discovery Server responds with the url of service X
* Client then calls service X on that url

Spring Cloud uses client-side discovery

## Server-side Discovery

* Client tells the Discovery Server he has something for service X
* Discovery Server passes it on to service X and returns the response to the client

## Netflix Open Source

Netflix have created the following microservice open-source architectures:
* Eureka (service discovery)
* Ribbon (load balancing)
* Hysterix (long lost sister of Asterix the Gaul)
* Zuul (wasn't this the monster on top of the skyscraper in Ghostbusters?)

but Spring abstracts these away for us.

## Eureka Server

Create a Eureka Server via the Spring Initializr - select Eureka Server.
```
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>
```

There can be multiple eureka servers, but we only have one so we must tell it that we don't want it to register itself and we don't want it to fetch the registry.  It owns the only copy of the registry.
In application.properties, we set:
``` 
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```
Java 11 may throw up some JAXB errors - see https://youtu.be/GTM2J0nYmbs?t=320

## Eureka Client
To register a client with the discovery server, in the client application, add the Eureka Discovery Client to the pom.xml (see https://start.spring.io/) 
```
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
```
Also add property spring.application.name.  Without an application name, it will show as UNKNOWN in the discovery server UI http://localhost:8761/
You can also annotate the main app class with @EnableEurekaClient, but this is no longer mandatory.

### How does the eureka client find the Eureka server?
It looks on the default port first, 8671.  If you're not using the default port, then you have to tell the client where to look for the eureka server via property XXX (probably eureka.server.port)

### Consuming a Discovered Service
* Annotate your RestTemplate bean with @LoadBalanced
* In the url of the RestTemplate method, sub in the exposed app name (as seen in the Eureka discovery server UI http://localhost:8761/.  You can also see it in the log as the app starts up)

e.g. change this:
```
restTemplate.getForObject("http://localhost:8083/ratings/users/" + userId, UserRatings.class);
```
to this:
```
restTemplate.getForObject("http://MOVIE-RATINGS-SERVICE/ratings/users/" + userId, UserRatings.class);
```

### Load Balancing
When you run a jar file, e.g.  java -jar my-application-0.0.1-SNAPSHOT.jar, you can override the server.port that is in that jar’s application.properties:
```
java -Dserver.port=8201 -jar my-application-0.0.1-SNAPSHOT.jar
```

We could start 3 instances of the movie info service on different ports and the Eureka discovery server UI http://localhost:8761/ would show 3 as UP.

### What Services Have Been Discovered?

There is a DiscoveryClient bean created by Eureka.  You can autowire this and call it's getInstances() method.  You then have access to the individual instances e.g. for load balancing details, but best to let Spring manage this for you.   https://youtu.be/tG2dA6zyEgM?t=431

### Heartbeats and Server Down
The discovery server periodically checks if each service is still up. The clients ping the discovery server at regular intervals.
If the discovery server itself goes down, then Spring will use the cached location of the service. 

