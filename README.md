# ouroboros
some network applications demo based on java nio.

- on jdk 17

## EventLoop
- echo
- ByteBufferPool
- HTTP/1.1

## Build
```bash
mvn clean package
```

## HTTP/1.1 Server
```bash
java -cp .\ouroboros.jar io.github.beastars1.ouroboros.http1.HttpServer 8080
```
default port is `8080`, server is `localhost:8080`.

## TCP Load Balance
1. start some true servers
    ```bash
   java -cp .\ouroboros.jar io.github.beastars1.ouroboros.http1.HttpServer 8081
   java -cp .\ouroboros.jar io.github.beastars1.ouroboros.http1.HttpServer 8082
   ```
2. start loadBalancer server
   ```bash 
   java -cp .\ouroboros.jar io.github.beastars1.ouroboros.loadbalance.LoadBalancer 8080 localhost:8081,localhost:8082
   ```
`8080` is the load balance port, `8081`,`8082` is true servers.
