package io.github.beastars1.ouroboros.loadbalance;

import io.github.beastars1.ouroboros.eventloop.SocketContext;
import io.github.beastars1.ouroboros.eventloop.SocketHandler;
import io.github.beastars1.ouroboros.logging.Logger;
import io.github.beastars1.ouroboros.logging.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.UUID;

/**
 * 转发请求到真正的 server
 */
public class LoadBalanceHandler implements SocketHandler {
    private static final Logger log = LoggerFactory.getLogger(LoadBalanceHandler.class);

    private final SocketContext ctx;
    private final Server server;
    private final ByteBuffer buffer;
    private TrueServerHandler trueServerHandler;

    public LoadBalanceHandler(SocketContext ctx, Server server) {
        this.ctx = ctx;
        this.server = server;
        this.buffer = this.ctx.byteBufferPool().borrowObject();
    }

    @Override
    public void onRegistered() throws IOException {
        ctx.selectionKey().interestOps(SelectionKey.OP_READ);
        // lb 所在服务器连接真正服务器进行转发
        SocketChannel trueSocket = SocketChannel.open(new InetSocketAddress(server.getHost(), server.getPort()));
        trueSocket.configureBlocking(false);
        SelectionKey key = trueSocket.register(ctx.selector(), SelectionKey.OP_READ);
        TrueServerHandler trueServerHandler = new TrueServerHandler(new SocketContext(trueSocket, key, ctx.thread(),
                UUID.randomUUID().toString(), ctx.selector(), ctx.byteBufferPool()));
        key.attach(trueServerHandler);
//        trueServerHandler.onRegistered();
        trueServerHandler.setLoadBalanceHandler(this);
        this.trueServerHandler = trueServerHandler;
        ctx.selector().wakeup();
    }

    @Override
    public void onRead() throws IOException {
        int len = ctx.socketChannel().read(buffer);
        if (len == -1) {
            this.close();
            log.info("ID: " + ctx.connectionId() + ", connection closed");
            return;
        }
        log.info("need to send " + server.toString());
        buffer.flip();
        // 将请求写到真正的服务器的缓冲区
        trueServerHandler.write(buffer);
    }

    /**
     * 将响应写到客户端的缓冲区
     */
    @Override
    public void write(ByteBuffer buffer) throws IOException {
        int len = ctx.socketChannel().write(buffer);
        log.info("ID: " + ctx.connectionId() + ", client read "+ len +" bytes");
        if (buffer.hasRemaining()) {
            buffer.flip();
        } else {
            buffer.clear();
        }
    }

    @Override
    public void close() throws IOException {
        ctx.byteBufferPool().returnObject(buffer);
        ctx.socketChannel().close();
        server.release();
        log.info("release server connection: " + server);
        trueServerHandler.close();
    }
}
