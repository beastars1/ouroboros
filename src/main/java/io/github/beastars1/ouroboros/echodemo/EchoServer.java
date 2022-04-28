package io.github.beastars1.ouroboros.echodemo;

import io.github.beastars1.ouroboros.eventloop.ServerBootstrap;

import java.io.IOException;

public class EchoServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerBootstrap server = new ServerBootstrap();
        server
                .provide(socketContext -> {
                    return new EchoServerHandler(socketContext);
                })
                .bind(8080);
    }
}
