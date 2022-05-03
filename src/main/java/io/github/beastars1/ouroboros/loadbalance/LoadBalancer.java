package io.github.beastars1.ouroboros.loadbalance;

import io.github.beastars1.ouroboros.eventloop.ServerBootstrap;
import io.github.beastars1.ouroboros.loadbalance.serverPool.ServerPool;
import io.github.beastars1.ouroboros.loadbalance.serverPool.ServerPoolFactory;
import io.github.beastars1.ouroboros.logging.Logger;
import io.github.beastars1.ouroboros.logging.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LoadBalancer {
    private static final Logger log = LoggerFactory.getLogger(LoadBalancer.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        log.info(Arrays.toString(args));
        if (args.length < 2) {
            log.error("input 2 args at least, such as: `8080 localhost:8081,localhost:8082`");
            return;
        }
        int port = Integer.parseInt(args[0]);
        String s = args[1];
        String[] split = s.split(",");
        List<Server> serverList = Arrays.stream(split)
                .map(Server::parseServer)
                .toList();
        String lbStrategy = (args.length == 3) ? args[2] : "roundRobin";
        log.info("load balancer strategy: " + lbStrategy);
        ServerPool serverPool = switch (lbStrategy) {
            case "roundRobin" -> ServerPoolFactory.roundRobinPool(serverList);
            case "random" -> ServerPoolFactory.randomPool(serverList);
            case "minConnection" -> ServerPoolFactory.minConnectionPool(serverList);
            default -> throw new RuntimeException("lb strategy is wrong");
        };
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .provide(ctx -> {
                    Server server = serverPool.getServer();
                    log.info("get server: " + server);
                    server.addConnection();
                    return new LoadBalanceHandler(ctx, server);
                })
                .bind(port);
    }
}
