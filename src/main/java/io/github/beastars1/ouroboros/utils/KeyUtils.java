package io.github.beastars1.ouroboros.utils;

import static java.nio.channels.SelectionKey.OP_READ;
import static java.nio.channels.SelectionKey.OP_WRITE;

public class KeyUtils {
    public static boolean isReadable(int readyOps) {
        return (readyOps & OP_READ) != 0;
    }

    public static boolean isWritable(int readyOps) {
        return (readyOps & OP_WRITE) != 0;
    }
}
