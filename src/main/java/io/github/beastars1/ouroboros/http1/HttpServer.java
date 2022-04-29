package io.github.beastars1.ouroboros.http1;

import io.github.beastars1.ouroboros.eventloop.ServerBootstrap;

import java.io.IOException;

public class HttpServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerBootstrap server = new ServerBootstrap();
        server
                .provide(HttpServerHandler::new)
                .bind(8080);
    }
}
