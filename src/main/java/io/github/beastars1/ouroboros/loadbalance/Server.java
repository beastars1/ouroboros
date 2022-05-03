package io.github.beastars1.ouroboros.loadbalance;

import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    private final String host;
    private final int port;
    // 当前服务的连接数
    private final AtomicInteger connectionCount = new AtomicInteger(0);

    public Server(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static Server parseServer(String s) {
        String[] split = s.split(":");
        String host = split[0].trim();
        int port = Integer.parseInt(split[1].trim());
        return new Server(host, port);
    }

    public void addConnection() {
        connectionCount.incrementAndGet();
    }

    public void release() {
        connectionCount.decrementAndGet();
    }

    public int getConnectionCount() {
        return connectionCount.get();
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "Server{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", connectionCount=" + connectionCount +
                '}';
    }
}
