package io.github.beastars1.ouroboros.loadbalance.serverPool;

import io.github.beastars1.ouroboros.loadbalance.Server;

import java.util.List;

public class ServerPoolFactory {
    public static ServerPool roundRobinPool(List<Server> servers) {
        return new RoundRobinServerPool(servers);
    }

    public static ServerPool randomPool(List<Server> servers) {
        return new RandomServerPool(servers);
    }

    public static ServerPool minConnectionPool(List<Server> servers) {
        return new MinConnectionServerPool(servers);
    }
}
