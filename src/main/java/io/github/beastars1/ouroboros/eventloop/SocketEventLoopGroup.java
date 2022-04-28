package io.github.beastars1.ouroboros.eventloop;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SocketEventLoopGroup {
    private final List<SocketEventLoop> eventLoops;
    private int position = 0;

    public SocketEventLoopGroup(int size) throws IOException, InterruptedException {
        this.eventLoops = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(size);
        for (int i = 0; i < size; i++) {
            SocketEventLoop eventLoop = new SocketEventLoop(latch);
            Thread thread = new Thread(eventLoop::loop);
            thread.setName("event-loop-" + i);
            thread.start();
            this.eventLoops.add(eventLoop);
        }
        if (!latch.await(10, TimeUnit.SECONDS)) {
            throw new RuntimeException("count down latch await timeout");
        }
    }

    /**
     * 将 socketChannel 轮询分配给 eventLoop
     *
     * @param socketChannel         要处理的 channel
     * @param socketHandlerProvider 处理策略
     */
    public void dispatch(SocketChannel socketChannel, SocketHandlerProvider socketHandlerProvider) throws IOException {
        if (position >= this.eventLoops.size()) {
            position = 0;
        }
        SocketEventLoop socketEventLoop = this.eventLoops.get(position);
        socketEventLoop.add(socketChannel, socketHandlerProvider);
        position++;
    }

    public void addEventLoop() throws IOException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        SocketEventLoop socketEventLoop = new SocketEventLoop(latch);
        Thread thread = new Thread(socketEventLoop::loop);
        thread.setName("event-loop-" + this.eventLoops.size());
        thread.start();
        if (!latch.await(10, TimeUnit.SECONDS)) {
            throw new RuntimeException("count down latch await timeout");
        }
    }
}
