package io.github.beastars1.ouroboros.eventloop;

@FunctionalInterface
public interface SocketHandlerProvider {
    // 为每个 socket 提供一个 handler
    SocketHandler provide(SocketContext socketContext);
}
