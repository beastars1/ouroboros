package io.github.beastars1.ouroboros.http1;

import io.github.beastars1.ouroboros.eventloop.SocketContext;
import io.github.beastars1.ouroboros.eventloop.SocketHandler;
import io.github.beastars1.ouroboros.logging.Logger;
import io.github.beastars1.ouroboros.logging.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;

public class HttpServerHandler implements SocketHandler {
    public static final Logger log = LoggerFactory.getLogger(HttpServerHandler.class);

    private final ByteBuffer buffer = ByteBuffer.allocate(1024);
    private final SocketContext ctx;

    public HttpServerHandler(SocketContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void onRead() throws IOException {
        int len = ctx.socketChannel().read(buffer);
        if (len == -1) {
            ctx.socketChannel().close();
            log.info("ID: " + ctx.connectionId() + ", connection closed");
            return;
        }
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        // todo 处理http请求
        log.info(new String(bytes));
        buffer.clear();
    }
}