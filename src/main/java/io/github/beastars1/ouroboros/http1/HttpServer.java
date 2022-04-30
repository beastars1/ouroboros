package io.github.beastars1.ouroboros.http1;

import io.github.beastars1.ouroboros.eventloop.ServerBootstrap;
import io.github.beastars1.ouroboros.http1.httpHandler.HttpRequestHandlerImpl;

import java.io.IOException;

public class HttpServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerBootstrap server = new ServerBootstrap();
        server
                .provide(socketContext -> new HttpServerHandler(socketContext, new HttpRequestHandlerImpl()))
                .bind(8080);
    }
}
