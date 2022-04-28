package io.github.beastars1.ouroboros.eventloop;

@FunctionalInterface
public interface SocketHandlerProvider {
    // 根据 socket 关联 handler
    SocketHandler provide(SocketContext socketContext);
}
