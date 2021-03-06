package io.github.beastars1.ouroboros.loadbalance;

import io.github.beastars1.ouroboros.loadbalance.serverPool.RoundRobinServerPool;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoundRobinServerPoolTest {
    @Test
    void getServerTest() {
        List<Server> servers = new ArrayList<>();
        servers.add(new Server("localhost", 8080));
        servers.add(new Server("localhost", 8081));
        servers.add(new Server("localhost", 8082));
        RoundRobinServerPool serverPool = new RoundRobinServerPool(servers);
        Server s1 = serverPool.getServer();
        Server s2 = serverPool.getServer();
        Server s3 = serverPool.getServer();
        assertEquals(8080, s1.getPort());
        assertEquals(8081, s2.getPort());
        assertEquals(8082, s3.getPort());
        Server s4 = serverPool.getServer();
        assertEquals(8080, s4.getPort());
    }

}