# Microservices

Javabrains - https://github.com/koushikkothagal/spring-boot-microservices-workshop

https://www.youtube.com/watch?v=y8IQb4ofjDo&list=PLqq-6Pq4lTTZSKAFG6aCDVDP86Qx4lNas

# Overview

Four separate Spring Boot Applications.
* the catalog - e.g. http://localhost:8081/catalog/1
* the movie info e.g. http://localhost:8082/movies/1
* the ratings e.g. http://localhost:8083/ratings/1
* the discovery server http://localhost:8761

# Array Responses

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

* Client asks the Discovery Server, where can I find service X?
* Discovery Server responds with the url of service X
* Client then calls service X on that url

Spring Cloud uses client-side discovery

## Server-side Discovery

* Client tells the Discovery Server he has something for service X
* Discovery Server passes it on to service X

## Netflix Open Source

Netflix have created the following microservice open-source architectures:
* Eureka (service discovery)
* Ribbon (load balancing)
* Hysterix (long lost sister of Asterix the Gaul)
* Zuul (wasn't this the monster on top of the skyscraper in Ghostbusters?)

but Spring abstracts these away for us.

## Eureka Server

There can be multiple eureka servers, but we only have one so we must tell it that we don't want it to register itself and we don't want it to fetch the registry.  It has the only copy of the registry.
In application.properties, we set:
``` 
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

## Eureka Client
To register a client with the discovery server, in the client application, add the Eureka Discovery Client to the pom.xml (see https://start.spring.io/) and add property spring.application.name.  Without an application name, it will show as UNKNOWN in the discovery server UI http://localhost:8761/
You can also annotate the main app class with @EnableEurekaClient, but this is no longer mandatory.

### How does the eureka client find the Eureka server?
It looks on the default port first, 8671.  If you're not using the default port, then you have to tell the client where to look for the eureka server via property ???

### Consuming a Discovered Service
* Annotate your RestTemplate bean with @LoadBalanced
* In the url of the RestTemplate method, sub in the exposed app name (as seen in the discovery server UI http://localhost:8761/.  You can also see it in the log as the app starts up)

e.g. change this 
```
restTemplate.getForObject("http://localhost:8083/ratings/users/" + userId, UserRatings.class);
```
to this:
```
restTemplate.getForObject("http://MOVIE-RATINGS-SERVICE/ratings/users/" + userId, UserRatings.class);
```
