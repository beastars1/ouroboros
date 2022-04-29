package io.github.beastars1.ouroboros.http1;

import io.github.beastars1.ouroboros.collection.ByteArray;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * HTTP/1.1 200 OK  \r\n
 * Header k:v  \r\n
 * \r\n  \r\n
 * Body  \r\n
 */
public class HttpResponse {
    private final String version = "HTTP/1.1";
    private String statusCode = "200";
    private String reasonPhrase = "OK";
    private final HttpHeaders httpHeaders = new HttpHeaders();
    private ByteArray body;

    public void addHeader(String key, String value) {
        httpHeaders.add(key, value);
    }

    public byte[] getBytes() {
        httpHeaders.remove("content-length");
        if (body != null) {
            httpHeaders.add("content-length", String.valueOf(body.size()));
        }
        // headers
        String s = httpHeaders.getHeaders()
                .stream()
                .map(header -> header.key() + ": " + header.value())
                .collect(Collectors.joining("\r\n"))
                + "\r\n\r\n";
        // HTTP/1.1 200 OK
        ByteArray byteArray = new ByteArray();
        byteArray.add((version + " ").getBytes(StandardCharsets.US_ASCII))
                .add((statusCode + " ").getBytes(StandardCharsets.US_ASCII))
                .add((reasonPhrase + "\r\n").getBytes(StandardCharsets.US_ASCII));
        // Headers
        byteArray.add(s.getBytes(StandardCharsets.US_ASCII));
        // Body
        if (body != null) {
            byteArray.add(body.getBytes());
        }
        byteArray.add("\r\n".getBytes(StandardCharsets.US_ASCII));
        return byteArray.getBytes();
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

    public ByteArray getBody() {
        return body;
    }

    public void setBody(ByteArray body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "version='" + version + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", reasonPhrase='" + reasonPhrase + '\'' +
                ", httpHeaders=" + httpHeaders +
                ", body=" + body +
                '}';
    }
}
