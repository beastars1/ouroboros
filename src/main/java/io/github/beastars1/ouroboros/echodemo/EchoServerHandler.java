package io.github.beastars1.ouroboros.echodemo;

import io.github.beastars1.ouroboros.eventloop.SocketContext;
import io.github.beastars1.ouroboros.eventloop.SocketHandler;
import io.github.beastars1.ouroboros.logging.Logger;
import io.github.beastars1.ouroboros.logging.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

public class EchoServerHandler implements SocketHandler {
    private final static Logger log = LoggerFactory.getLogger(EchoServerHandler.class);

    private final ByteBuffer buffer = ByteBuffer.allocate(512);
    private final SocketContext ctx;

    public EchoServerHandler(SocketContext socketContext) {
        this.ctx = socketContext;
    }

    @Override
    public void onRead() throws IOException {
        int len = ctx.socketChannel().read(buffer);
        if (len == -1) {
            ctx.socketChannel().close();
            log.info("ID: " + ctx.connectionId() + ", connection closed");
            return;
        }
        log.info("ID: " + ctx.connectionId() + ", read " + len + " bytes");
        ctx.selectionKey().interestOps(SelectionKey.OP_WRITE);
    }

    @Override
    public void onWrite() throws IOException {
        buffer.flip();
        int len = ctx.socketChannel().write(buffer);
        if (buffer.hasRemaining()) {
            buffer.compact();
        } else {
            buffer.clear();
        }
        log.info("ID: " + ctx.connectionId() + ", write " + len + " bytes");
        ctx.selectionKey().interestOps(SelectionKey.OP_READ);
    }
}
