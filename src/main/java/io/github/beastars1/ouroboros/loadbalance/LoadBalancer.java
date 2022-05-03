package io.github.beastars1.ouroboros.loadbalance;

import io.github.beastars1.ouroboros.eventloop.ServerBootstrap;
import io.github.beastars1.ouroboros.loadbalance.serverPool.RoundRobinServerPool;
import io.github.beastars1.ouroboros.loadbalance.serverPool.ServerPool;
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
        ServerPool serverPool = new RoundRobinServerPool(serverList);
        ServerBootstrap server = new ServerBootstrap();
        server
                .provide(ctx -> new LoadBalanceHandler(ctx, serverPool.getServer()))
                .bind(port);
    }
}
