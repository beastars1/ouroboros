package io.github.beastars1.ouroboros.http1;

import io.github.beastars1.ouroboros.collection.ByteArray;
import io.github.beastars1.ouroboros.eventloop.SocketContext;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * 一个 http 连接的上下文
 */
public class HttpContext {
    private final HttpRequest httpRequest;
    private final HttpResponse httpResponse;
    private final SocketContext socketContext;

    public HttpContext(HttpRequest httpRequest, HttpResponse httpResponse, SocketContext socketContext) {
        this.httpRequest = httpRequest;
        this.httpResponse = httpResponse;
        this.socketContext = socketContext;
    }

    public HttpContext status(String status) {
        httpResponse.setStatusCode(status);
        httpResponse.setReasonPhrase(toReason(status));
        return this;
    }

    public void close() throws IOException {
        socketContext.close();
    }

    /**
     * 写入 response 到 channel
     *
     * @param s 响应体
     */
    public void write(String s) throws IOException {
        httpResponse.setBody(new ByteArray().add(s.getBytes(StandardCharsets.US_ASCII)));
        httpResponse.addHeader("content-type", "application/json");
        byte[] bytes = httpResponse.getBytes();
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        socketContext.socketChannel().write(buf);
    }

    private String toReason(String status) {
        switch (status) {
            case "200" -> {
                return "OK";
            }
            case "404" -> {
                return "Not Found";
            }
        }
        return "OK";
    }

    public HttpRequest getHttpRequest() {
        return httpRequest;
    }
}
