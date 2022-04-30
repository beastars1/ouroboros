# ouroboros
some network applications demo based on java nio

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
java -cp .\ouroboros.jar io.github.beastars1.ouroboros.http1.HttpServer
```
default port is `8080`, server is `localhost:8080`
