# catalog-service

## Structure

|- controller  
|- entity  
|- exceptions  
|- repository  
|- service

---

## Notes

- `server.tomcat.connection-timeout` property defines a limit for how much time Tomcat should wait between accepting a TCP connection from a client and actually receiving the HTTP request. It helps prevent denial-ofservice (DoS) attacks where a connection is established, Tomcat reserves a thread to handle the request, and the request never comes.
- The default value is 20s, which is probably too much for a standard cloud native application. In the context of highly distributed systems in the cloud, we probably don’t want to wait more than a couple of seconds and risk a cascading failure due to a Tomcat instance hanging for too long. Something like 2s would be better
- The default thread pool for `server.tomcat.threads`can grow up to 200 threads and has 10 worker threads always running, which are good starting values in production. In your local environment, you might want to lower those values to optimize resource consumption, since it increases linearly with the number of threads
