package io.github.beastars1.ouroboros.loadbalance;

import java.util.ArrayList;
import java.util.List;

public class ServerPool {
    private final List<Server> servers = new ArrayList<>();
    private int i = 0;

    public ServerPool(List<Server> servers) {
        if (servers.size() == 0) {
            throw new RuntimeException("servers size should be greater than 0");
        }
        this.servers.addAll(servers);
    }

    public Server getServer() {
        if (i >= servers.size()) {
            i = 0;
        }
        Server server = servers.get(i);
        i++;
        return server;
    }
}
