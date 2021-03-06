package io.github.beastars1.ouroboros.loadbalance.serverPool;

import io.github.beastars1.ouroboros.loadbalance.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 随机选择服务
 */
public class RandomServerPool implements ServerPool{
    private final List<Server> servers = new ArrayList<>();
    private final Random random = new Random();

    public RandomServerPool(List<Server> servers) {
        if (servers.size() == 0) {
            throw new RuntimeException("servers size should be greater than 0");
        }
        this.servers.addAll(servers);
    }

    @Override
    public Server getServer() {
        int i = random.nextInt(servers.size());
        return servers.get(i);
    }
}
