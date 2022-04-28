package io.github.beastars1.ouroboros.eventloop;

import io.github.beastars1.ouroboros.logging.Logger;
import io.github.beastars1.ouroboros.logging.LoggerFactory;
import io.github.beastars1.ouroboros.objectpool.ByteBufferPool;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

public class SocketEventLoop {
    private static final Logger log = LoggerFactory.getLogger(SocketEventLoop.class);

    private final Selector selector;
    private final ByteBufferPool byteBufferPool;
    private final CountDownLatch latch;
    private SocketHandlerProvider socketHandlerProvider;

    public SocketEventLoop(CountDownLatch latch) throws IOException {
        this.selector = Selector.open();
        this.latch = latch;
        this.byteBufferPool = new ByteBufferPool();
    }

    public synchronized void add(SocketChannel socketChannel) throws IOException {
        this.add(socketChannel, this.socketHandlerProvider);
    }

    public void add(SocketChannel socketChannel, SocketHandlerProvider socketHandlerProvider) throws IOException {
        SelectionKey key = socketChannel
                .configureBlocking(false)
                .register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        String connectionId = UUID.randomUUID().toString();
        SocketContext socketContext = new SocketContext(socketChannel, key, Thread.currentThread(), connectionId,
                this.selector, this.byteBufferPool);
        SocketHandler handler = socketHandlerProvider.provide(socketContext);
        // 附加处理逻辑，处理时取出
        key.attach(handler);
        handler.onRegistered();
        this.selector.wakeup();
    }

    /**
     * 轮询查找可操作的 channel
     */
    public CompletableFuture<String> loop() {
        log.info("started");
        this.latch.countDown();
        while (true) {
            try {
                // 选择已经准备就绪的 channel
                this.selector.select();
                Set<SelectionKey> keys = this.selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    SocketHandler handler = (SocketHandler) key.attachment();
                    try {
                        if (key.isReadable()) {
                            handler.onRead();
                        } else if (key.isWritable()) {
                            handler.onWrite();
                        }
                    } catch (IOException e) {
                        log.error("error", e);
                        key.channel().close();
                    }
                    iterator.remove();
                }
            } catch (IOException e) {
                log.error("error", e);
                return CompletableFuture.supplyAsync(() -> {
                    throw new RuntimeException(e);
                });
            }
        }
    }

    public void setSocketHandlerProvider(SocketHandlerProvider socketHandlerProvider) {
        this.socketHandlerProvider = socketHandlerProvider;
    }
}
