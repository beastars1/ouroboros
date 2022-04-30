package io.github.beastars1.ouroboros.http1.httpHandler;

import io.github.beastars1.ouroboros.http1.HttpContext;
import io.github.beastars1.ouroboros.http1.HttpRequest;

import java.io.IOException;

public class UriHandler implements HttpRequestHandler{
    @Override
    public void handle(HttpContext ctx) {
        HttpRequest request = ctx.getHttpRequest();
        String uri = request.uri();
        String res = """
                {
                    "uri": "%s"
                }
                """;
        res = String.format(res, uri);
        try {
            ctx.write(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
