package io.github.beastars1.ouroboros.loadbalance.serverPool;

import io.github.beastars1.ouroboros.loadbalance.Server;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MinConnectionServerPoolTest {
    @Test
    void getServerTest() {
        List<Server> servers = new ArrayList<>();
        servers.add(new Server("localhost", 8080));
        servers.add(new Server("localhost", 8081));
        servers.add(new Server("localhost", 8082));
        ServerPool serverPool = new MinConnectionServerPool(servers);
        Server s1 = serverPool.getServer();
        assertEquals(8080, s1.getPort());
        s1.addConnection();
        Server s2 = serverPool.getServer();
        assertEquals(8081, s2.getPort());
        s2.addConnection();
        Server s3 = serverPool.getServer();
        assertEquals(8082, s3.getPort());
        s3.addConnection();
        s1.release();
        Server s4 = serverPool.getServer();
        assertEquals(8080, s4.getPort());
    }
}