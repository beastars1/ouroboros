package io.github.beastars1.ouroboros.loadbalance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerPool {
    private final List<Server> servers = new ArrayList<>();
    private final AtomicInteger atomic = new AtomicInteger();

    public ServerPool(List<Server> servers) {
        this.servers.addAll(servers);
    }

    public Server getServer() {
        int i = atomic.getAndIncrement();
        Server server = servers.get(i);
        if (atomic.get() >= servers.size()) {
            atomic.set(0);
        }
        return server;
    }
}
