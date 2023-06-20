# catalog-service

## Core Structure

|- config  
|- controller  
|- entity  
|- exceptions  
|- repository  
|- service

---

## Useful commands

- `java -jar -Dspring.profiles.active={{ .target.Profile }} {{ jar.Directory }}.jar`
- e.g. `java -jar -Dspring.profiles.active=testdata target/catalog-service-0.0.1-SNAPSHOT.jar`

---

## Notes

- `server.tomcat.connection-timeout` property defines a limit for how much time Tomcat should wait between accepting a TCP connection from a client and actually receiving the HTTP request. It helps prevent denial-ofservice (DoS) attacks where a connection is established, Tomcat reserves a thread to handle the request, and the request never comes.
- The default value is 20s, which is probably too much for a standard cloud native application. In the context of highly distributed systems in the cloud, we probably don’t want to wait more than a couple of seconds and risk a cascading failure due to a Tomcat instance hanging for too long. Something like 2s would be better
- The default thread pool for `server.tomcat.threads`can grow up to 200 threads and has 10 worker threads always running, which are good starting values in production. In your local environment, you might want to lower those values to optimize resource consumption, since it increases linearly with the number of threads
- Integration tests need a Spring application context to run. A full application context, including an optional embedded server, can be initialized for testing using the @SpringBootTest annotation
- When tests are focused only on a "slice" of the application and only need a part of the configuration, Spring Boot provides several annotations for more targeted integration tests. When you use those annotations, a Spring application context is initialized, but only the components and configuration parts used by the specific functionality slice are loaded
- @WebMvcTest is for testing Spring MVC components
- @JsonTest is for testing JSON serialization and deserialization
