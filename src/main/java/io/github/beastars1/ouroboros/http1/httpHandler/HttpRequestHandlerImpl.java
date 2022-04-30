package io.github.beastars1.ouroboros.http1.httpHandler;

import io.github.beastars1.ouroboros.http1.HttpContext;

import java.io.IOException;

public class HttpRequestHandlerImpl implements HttpRequestHandler {
    @Override
    public void handle(HttpContext ctx) {
        String uri = ctx.getHttpRequest().uri();
        try {
            if (uri.equalsIgnoreCase("/hello")) {
                ctx.status("200").write("Hello");
            } else {
                ctx.status("404").write("Not Found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
