package io.github.beastars1.ouroboros.http1;

import java.util.Arrays;

/**
 * GET /hello HTTP/1.1\r\n
 * Headers k:v\r\n
 * \r\n
 * Body
 */
public record HttpRequest(HttpMethod httpMethod,
                          String uri,
                          String version,
                          HttpHeaders httpHeaders,
                          byte[] body) {
    @Override
    public String toString() {
        return "HttpRequest{" +
                "httpMethod=" + httpMethod +
                ", uri='" + uri + '\'' +
                ", version='" + version + '\'' +
                ", httpHeaders=" + httpHeaders +
                ", body=" + Arrays.toString(body) +
                '}';
    }
}
