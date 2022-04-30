package io.github.beastars1.ouroboros.http1.httpHandler;

import io.github.beastars1.ouroboros.http1.HttpContext;

/**
 * 处理 socket 的 http 请求
 */
public interface HttpRequestHandler {
    void handle(HttpContext ctx);
}
