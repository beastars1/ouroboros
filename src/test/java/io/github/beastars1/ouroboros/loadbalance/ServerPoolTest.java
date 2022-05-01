package io.github.beastars1.ouroboros.loadbalance;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServerPoolTest {
    @Test
    void getServerTest() {
        List<Server> servers = new ArrayList<>();
        servers.add(new Server("localhost", 8080));
        servers.add(new Server("localhost", 8081));
        servers.add(new Server("localhost", 8082));
        ServerPool serverPool = new ServerPool(servers);
        Server s1 = serverPool.getServer();
        Server s2 = serverPool.getServer();
        Server s3 = serverPool.getServer();
        assertEquals(8080, s1.port());
        assertEquals(8081, s2.port());
        assertEquals(8082, s3.port());
        Server s4 = serverPool.getServer();
        assertEquals(8080, s4.port());
    }

}