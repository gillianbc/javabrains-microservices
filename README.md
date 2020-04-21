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

When designing endpoints, NEVER have an array of objects as the return type.  It's possible, but bad practice.  If you want to add in some other field later, outside of the array, e.g. a count, then you will cause a breaking change in all the consumer code.  
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

# Netflix Open Source

Netflix have created the following microservice open-source architectures:
* Eureka (service discovery)
* Ribbon
* Hysterix
* Zuul

but Spring abstracts these away for us.