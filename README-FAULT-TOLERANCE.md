# MICROSERVICES LEVEL 2 - FAULT TOLERANCE AND RESILIENCE
If part of the microservice ecosystem is slow, thread starvation can affect other parts.

Set a timeout in the @Bean that gives you a RestTemplate.  
https://youtu.be/6osL1aAIXU4?t=219

However, if the requests are coming in at a faster rate than the timeout, we're we can still get thread starvation.

This is where we use the **Circuit Breaker Pattern**
- detect something is wrong
- take steps to avoid it getting worse
- deactivate the problem component for a while

## Circuit Breaker Parameters
How do we determine when to break the circuit?
https://youtu.be/kr-2li6kr9s?t=500

What do we do with requests while the circuit is broken?
https://youtu.be/ht8HNsX_jBQ?t=255
The options we have are:
- Throw an error e.g. temporarily unavailable
- Return some sort of default response
- Return previously cached responses

# Hystrix
Why use a circuit breaker, such as Hystrix?
- Failing fast
- Fallback functionality
- Automatic recovery
We use hystrix as thread concurrency handling is very hard.
  
Adding Hystrix dependency - https://youtu.be/niqew7GPP4k?t=19
- add dependency spring-cloud-starter-netflix-hystrix-dependency
- add `@EnableCircuitBreaker` to your main application class
- add `@HystrixCommand` to methods that need a circuit breaker
- configure hystrix

The methods annotated with `@HystrixCommand` can have a fallback method with the same signature.
Use a similar method name as a convention e.g. 'getFallbackXXX'
https://youtu.be/niqew7GPP4k?t=490
You can test this very simply by taking down one of the dependent services, since a service down will automatically cause Hystrix to invoke the fallback.

:warning: Hystrix wraps your class in a proxy so that it can detect problems before calling your class methods.
If you apply the `@HystrixCommand` to methods that are called from other methods within your class, then Hystrix will not be aware.
So, you need to refactor those methods out into some other class so that Histrix can wrap that class in a proxy.
https://youtu.be/1EIb-4ipWFk?t=504

Configure Hystrix by method via `@HystrixProperty`.  
https://youtu.be/1MPDOdFihPo?t=34
e.g.
```
@HystrixCommand(commandProperties = {
        @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
        @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
        @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),
        @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60")
```

#Hystrix Dashboard
Can be for an individual app or for a cluster.
For an individual app, add these two dependencies:
```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-hystrix-dashboard</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
Also add `@EnableHystrixDashboard` to your application class so that your app continually sends info to the dashboard.
In `application.properties`, add:
`management.endpoints.web.exposure.include=hystrix.stream`

Your hystrix dashboard will then appear on `<your microservice url>/hystrix`
You can then monitor your app's hystrix stream e.g.
`http://localhost:8081/actuator/hystrix.stream`

# Bulkhead Pattern
Allows the thread pools to be divvied up individually to methods so that slowness in one method does  not starve other methods.
```
@HystrixCommand(fallbackMethod = "defaultDoSomething",
          threadPoolKey = "movieInfoPool"
          threadPoolProperties = {
                  @HystrixProperty(name = "coreSize", value = "3"),
                  @HystrixProperty(name = "maxQueueSize", value = "-1"),
          }
  )
```
.