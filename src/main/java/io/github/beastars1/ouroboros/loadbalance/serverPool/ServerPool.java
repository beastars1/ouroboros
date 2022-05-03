package io.github.beastars1.ouroboros.loadbalance.serverPool;

import io.github.beastars1.ouroboros.loadbalance.Server;

public interface ServerPool {
    Server getServer();
}
