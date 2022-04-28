package io.github.beastars1.ouroboros.eventloop;

import io.github.beastars1.ouroboros.logging.Logger;
import io.github.beastars1.ouroboros.logging.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ServerBootstrap {
    private static final Logger log = LoggerFactory.getLogger(ServerBootstrap.class);

    private int port;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private SocketEventLoopGroup eventLoopGroup;
    private SocketHandlerProvider socketHandlerProvider;

    /**
     * 传入服务端的处理逻辑
     *
     * @param socketHandlerProvider 处理逻辑
     */
    public ServerBootstrap provide(SocketHandlerProvider socketHandlerProvider) {
        this.socketHandlerProvider = socketHandlerProvider;
        return this;
    }

    public void bind(int port) throws IOException, InterruptedException {
        if (this.socketHandlerProvider == null) {
            throw new RuntimeException("socketHandlerProvider is null");
        }

        this.selector = Selector.open();
        this.serverSocketChannel = ServerSocketChannel.open();
        this.eventLoopGroup = new SocketEventLoopGroup(Runtime.getRuntime().availableProcessors());
        this.port = port;

        this.serverSocketChannel
                .bind(new InetSocketAddress(this.port))
                .configureBlocking(false)
                .register(this.selector, SelectionKey.OP_ACCEPT);
        log.info("server started on " + this.port + " port");
        while (!Thread.interrupted()) {
            this.selector.select();
            // 遍历所有注册的 channel，如果有新连接，就分配给主线程去处理
            Set<SelectionKey> keys = this.selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    log.info("new connection");
                    // main reactor --> sub reactor
                    this.eventLoopGroup.dispatch(socketChannel, this.socketHandlerProvider);
                }
                iterator.remove();
            }
        }
    }
}
