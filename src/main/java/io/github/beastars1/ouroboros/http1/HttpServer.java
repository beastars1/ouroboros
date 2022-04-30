package io.github.beastars1.ouroboros.http1;

import io.github.beastars1.ouroboros.eventloop.ServerBootstrap;
import io.github.beastars1.ouroboros.http1.httpHandler.UriHandler;

import java.io.IOException;

public class HttpServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 8080;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        }

        ServerBootstrap server = new ServerBootstrap();
        server
                .provide(socketContext -> new HttpServerHandler(socketContext, new UriHandler()))
                .bind(port);
    }
}
