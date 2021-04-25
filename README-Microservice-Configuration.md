# Microservice Configuration

https://www.youtube.com/watch?v=upoIwn4rWCo&list=PLqq-6Pq4lTTaoaVoQVfRJPqvNTCjcTvJB

Examples:
- database connection details
- credentials
- feature flags - especially useful for trying new features
- business logic params
- scenario/AB testing e.g. 10% goes to scenario A, 90% to B
- Spring Boot configuration
- production config real-time

Config is done by either:
- properties
- yaml
- JSON

Goals:
- externalised - separate from the application code
- environment specific e.g. dev database, QA database, production database
- consistent - generally if you have 5 instances of the same microservice, they should pick up the same config (unless you actually need them to be different)
- version history - when was something changed, what was it before
- real-time management

At its most simple, we have `/src/main/resources/application.properties`

To use a property, we use e.g. `@Value("${my.greeting}")`

We can use that same `"${}"` syntax within the property file to reference other properties

If you have another `application.properties` file at the same location as the application's jar file, the properties in the external file will override the properties within the jar's `application.properties`.

If you use a command line arg, this will override both the properties in the external file and the properties within the jar's `application.properties`.
e.g.
`java -jar my-application.jar --myproperty="Hello"`
So your script could pass in these values e.g. from heroku system properties.

# @Value Hints and Tips
## Default Value
If a value property is not found, the application will not load.  You can supply a default:
e.g. `@Value("${my.greeting : hello from Gillian}"`
## List of Values
e.g. summer.months=Jun,July,Aug
Spring will automatically understand the comma separation when you do:
```
@Value("${summer.months}")
private List<String> summerMonths;
```
## Key Value Pairs
Use single quotes e.g.
`author={name: 'Gillian', job:'developer'}`
To use this, we need to use SPEL `#{}` e.g.
```
@Value("#{${author}}")
private Map<String, String> author;
```
## @ConfigurationProperties
If you have several related properties e.g. db.name, db.port, db.connection etc, you can create a bean class.
You also need @Configuration so that Spring knows to create it as a bean.  
Declare fields with the same names as the properties (minus the prefix) and provide getters and setters e.g. via Lombok.
e.g.
```
@Data
@ConfigurationProperties("db")
@Configuration
public class DbSettings {
    private String name;
    private int port;
    private String connection
}
```
Then autowire as usual:
```
@Autowired
private DbSettings dbSettings;
```
## When Do You Use Each?
Use @Value for one off values.  
Use @ConfigurationProperties if a bunch of related properties and when it needs to be autowired in several places in your code.

# Spring Boot Actuator
Exposes an endpoint to see config properties.
Add the actuator dependency to the pom.xml
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
 Some of the information is sensitive, so you have to tell Spring what you want to expose - this may be different in dev and production.
 
To enable all available endpoints use `management.endpoints.web.exposure.include=*`
Go to e.g. `localhost:8080/actuator/configprops`

# YAML
Very useful when property names are really long as you can use nesting e.g.
```
db:
    port: 1200
    connection: blah
```
The indentation (always spaces) is optional - not necessary if you only have one property with that path.
You can use any number of spaces, but it has to be consistent.

Syntax is `property:value`.  
You can use "" but they're not necessary around simple strings.  You will need "" around object values { } and "*"

# SPRING PROFILES
A predefined set of configuration properties e.g.
`application-test.yml` where `dev` is the name we're going to use for a profile.

You can set the active profile in the default config file `application.yml` e.g.
`spring.profiles.active: dev`
You'll see the active profile mentioned in the Spring log.  The default profile is ALWAYS active - it's always read by Spring.
Any other profile will be on top of the default profile.  If the same properties exist in both, then the one in the active profile wins.
You only need to put the environment specific properties in the profile files.

You can override the active profile when you `java -jar` via the command line.

## Selecting a Bean Specific to a Spring Profile
https://youtu.be/P91tqdWUHE4?t=874
`@Profile("dev")`  or `@Profile("production")`  for example, for a repository class.

# The Environment Object
When we use @Value etc, we are not looking up a property, we are asking Spring to inject that property value for us.
We can actually lookup values in the environment object and it has all the property and profile access methods that Spring uses under the covers.  
```
@Autowired
private Environment environment;
```
You should not use it, even though it's available - let Spring do its job.

# SPRING CLOUD CONFIG SERVER
https://youtu.be/JSdy9Q8Uk34
When you have a whole ecosystem of microservices, you need a config server.  A change to the config should not necessitate a build/deploy.
Spring Cloud Config Server connects directly to a git repo.  So, we get full version control.
## Setting up a Config Server from Scratch
https://youtu.be/gb1i4WyWNK4

Go to `start.spring.io` and select:
- config server

Open the generated project and add `@EnableConfigServer` to the application class.
In `application.properties` we set the git url where you have some application.properties or yml files.
It can be a local git repo initially.  
e.g.
`spring.cloud.config.server.git.uri=c:\!myfiles\configrepo`
To create a local git repo: 
- in an empty folder, create an application.yml file
- git init
- git add
- git commit -m "blah"
  (You can use system properties in the path - you may have to use file:\\)

By convention: `server.port=8888` for the config server.

The url of the config server is: `http://localhost:8888/<filename>/<profile>`

e.g. If you want to see the `application.yml` settings, use `http://localhost:8888/application/default` (since it's the file for the default profile)

## Consuming the Config Server - Spring Cloud Config Client
```
<dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-config</artifactId>
</dependency>

<dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-dependencies</artifactId>
        <version>${spring-cloud.version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>
```
Just select Config Client Spring Cloud Config if you're using the Sprint initializr to create a brand new project.

To point it at the config server, in the application.yml, add the url of the config server e.g.:
`spring.cloud.config.uri: http://localhost:8888/`

### Configuring Properties Specific to a Particular Microservice in the Ecosystem
e.g. server.port
In the git repo, the `application.yml` is the set of default properties that all microservices can use.
Create additional yml files using the `<microservice name>.yml`  e.g. `movie-info.yml`. 

The microservice name is in the artifactId in the `pom.xml`.  
It's conventional to also add the artifactId in the properties e.g.
`spring.application.name: movie-info`.  
It's this property that the config server will use when looking for a config file for that particular microservice.

# Real-time Config Refresh
i.e. without having to stop and start your services
If you commit into the git repo, the config server will detect the change - e.g. see the values via `http://localhost:8888/application/default`
The config client microservices, will not detect the change unless:
- you have a dependency on spring boot actuator
- you mark each of your desired beans (@Bean, @RestController, @Repository etc) `@RefreshScope`
- you make a POST request to the actuator endpoint `<microservice>/actuator/refresh` e.g. `localhost:8080/actuator/refresh`
  
(It's a POST because it will make changes, it's not idempotent).
The response from the POST will show the changed config values.

# Configuration Strategies - Best Practices
- something specific to the microservice that will NOT change, e.g. the application name, use `application.properties` in the jar
- something specific to the microservice that may change, e.g. the port, use config server

:exclamation:you can always override a property by adding it to the config server later, but putting a property in there from the start implies that it's something that you intend to be configurable

If you're using system environment variables e.g. from AWS or Heroku, use an alias, e.g.
```
someheroku.property.port: 8090
env.port: ${someheroku.property.port}
server.port: ${env.port}
```
then for a different microservice it could be:
```
someaws.property.port: 8093
env.port: ${someaws.property.port}
server.port: ${env.port}
```
## Security
- add Spring Security to the config server (how?)
- encyrypt credentials etc in the files you commit to git

To encrypt, use the config server's `/encrypt` feature.  It also has a `/decrypt` feature.
See JCE - Java Cryptography Extension

Use sensible default properties for the development environment to minimise effort when running in dev.  
For example, instead of:
`config.uri: http://localhost:${config.port}`, set a default using the colon 
i.e. `config.uri: http://localhost:${config.port:8888}`

Also see the gold standard for how services should be:
https://12factor.net
.