package io.github.beastars1.ouroboros.loadbalance;

import io.github.beastars1.ouroboros.eventloop.SocketContext;
import io.github.beastars1.ouroboros.eventloop.SocketHandler;
import io.github.beastars1.ouroboros.logging.Logger;
import io.github.beastars1.ouroboros.logging.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;

public class TrueServerHandler implements SocketHandler {
    private static final Logger log = LoggerFactory.getLogger(TrueServerHandler.class);

    private final SocketContext ctx;
    private final ByteBuffer buffer;
    private LoadBalanceHandler loadBalanceHandler;

    public TrueServerHandler(SocketContext ctx) {
        this.ctx = ctx;
        this.buffer = this.ctx.byteBufferPool().borrowObject();
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
        // 将响应通过代理服务器返回客户端
        loadBalanceHandler.write(buffer);
    }

    @Override
    public void close() throws IOException {
        ctx.byteBufferPool().returnObject(buffer);
        ctx.socketChannel().close();
        log.info("ID: " + ctx.connectionId() + ", connection closed");
    }

    /**
     * 将请求写到真正的服务器的缓冲区
     */
    @Override
    public void write(ByteBuffer buffer) throws IOException {
        int len = ctx.socketChannel().write(buffer);
        if (buffer.hasRemaining()) {
            buffer.flip();
        } else {
            buffer.clear();
        }
        log.info("ID: " + ctx.connectionId() + ", server read "+ len +" bytes");
    }

    public void setLoadBalanceHandler(LoadBalanceHandler loadBalanceHandler) {
        this.loadBalanceHandler = loadBalanceHandler;
    }
}
