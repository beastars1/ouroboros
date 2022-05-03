package io.github.beastars1.ouroboros.loadbalance.serverPool;

import io.github.beastars1.ouroboros.loadbalance.Server;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * 选择连接数最少的一个服务
 */
public class MinConnectionServerPool implements ServerPool{
    private final List<Server> servers = new ArrayList<>();

    public MinConnectionServerPool(List<Server> servers) {
        if (servers.size() == 0) {
            throw new RuntimeException("servers size should be greater than 0");
        }
        this.servers.addAll(servers);
    }

    @Override
    public Server getServer() {
        Optional<Server> optionalServer = servers
                .stream()
                .sorted(Comparator.comparing(Server::getConnectionCount))
                .peek(server -> System.out.print(server.getPort() + " - "))
                .limit(1)
                .findAny();
        return optionalServer.orElse(servers.get(0));
    }
}
