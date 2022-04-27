package io.github.beastars1.ouroboros.eventloop;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 处理 socket，自定义实现该接口
 */
public interface SocketHandler {
    default void onRegistered() throws IOException {
    }

    default void onRead() throws IOException {
    }

    default void onWrite() throws IOException {
    }

    default void close() throws IOException {
    }

    default void write(ByteBuffer buffer) throws IOException {
    }
}
