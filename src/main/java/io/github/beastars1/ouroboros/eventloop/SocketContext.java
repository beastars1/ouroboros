package io.github.beastars1.ouroboros.eventloop;

import io.github.beastars1.ouroboros.objectpool.ByteBufferPool;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Socket 的上下文
 */
public record SocketContext(SocketChannel socketChannel,
                            SelectionKey selectionKey,
                            Thread thread,
                            String connectionId,
                            Selector selector,
                            ByteBufferPool byteBufferPool) {

    public void close() throws IOException {
        this.socketChannel.close();
        this.thread.interrupt();
    }

}
