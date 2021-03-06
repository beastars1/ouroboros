package io.github.beastars1.ouroboros.echodemo;

import io.github.beastars1.ouroboros.eventloop.ClientBootstrap;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class EchoClient {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        ClientBootstrap client = new ClientBootstrap();
        InetSocketAddress address = new InetSocketAddress("localhost", 8080);
        CompletableFuture<String> future = client
                .provide(EchoClientHandler::new)
                .connect(address);
        future.get();
    }
}
