package io.github.beastars1.ouroboros.echodemo;

import io.github.beastars1.ouroboros.eventloop.SocketContext;
import io.github.beastars1.ouroboros.eventloop.SocketHandler;
import io.github.beastars1.ouroboros.logging.Logger;
import io.github.beastars1.ouroboros.logging.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.charset.StandardCharsets;

public class EchoClientHandler implements SocketHandler {
    private static final Logger log = LoggerFactory.getLogger(EchoClientHandler.class);

    private final ByteBuffer buffer = ByteBuffer.allocate(1024);
    private final SocketContext ctx;

    public EchoClientHandler(SocketContext socketContext) {
        this.ctx = socketContext;
    }

    @Override
    public void onRegistered() throws IOException {
        String s = "hello, world!";
        buffer.put(s.getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        ctx.socketChannel().write(buffer);
        if (buffer.hasRemaining()) {
            buffer.compact();
        } else {
            buffer.clear();
        }
        log.info("onRegistered");
        ctx.selectionKey().interestOps(SelectionKey.OP_READ);
    }

    @Override
    public void onRead() throws IOException {
        int len = ctx.socketChannel().read(buffer);
        if (len == -1) {
            log.info("ID: " + ctx.connectionId() + ", connection closed");
        }
        log.info("onRead");
        ctx.close();
    }

    @Override
    public void onWrite() throws IOException {
    }
}
