package io.github.beastars1.ouroboros.loadbalance;

public record Server(String host, int port) {
    public static Server parseServer(String s) {
        String[] split = s.split(":");
        String host = split[0].trim();
        int port = Integer.parseInt(split[1].trim());
        return new Server(host, port);
    }
}
