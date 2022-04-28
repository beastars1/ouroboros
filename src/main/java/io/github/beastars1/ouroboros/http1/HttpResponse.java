package io.github.beastars1.ouroboros.http1;

import java.util.Arrays;

/**
 * HTTP/1.1 200 OK
 * Header k:v
 *
 * Body
 */
public class HttpResponse {
    private final String version = "HTTP/1.1";
    private String statusCode = "200";
    private String reasonPhrase = "OK";
    private final HttpHeaders httpHeaders = new HttpHeaders();
    private byte[] body;

    public void addHeader(String key, String value) {
        httpHeaders.add(key, value);
    }

    public String getVersion() {
        return version;
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "version='" + version + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", reasonPhrase='" + reasonPhrase + '\'' +
                ", httpHeaders=" + httpHeaders +
                ", body=" + Arrays.toString(body) +
                '}';
    }
}
