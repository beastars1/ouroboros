package io.github.beastars1.ouroboros.http1;

import io.github.beastars1.ouroboros.collection.ByteArray;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * 解析二进制请求数据，转化为 HttpRequest 形式
 */
public class HttpRequestParser {
    private final ByteArray byteArray = new ByteArray();
    private HttpMethod method;
    private String uri;
    private String version;
    private HttpHeaders headers;
    private byte[] body;
    private boolean ready = false;

    public void read(ByteBuffer buffer) {
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        byteArray.add(bytes);
        if (method == null) {
            parseMethod();
        }
        if (method != null && uri == null) {
            parseUri();
        }
        if (method != null && uri != null && version == null) {
            parseVersion();
        }
        if (method != null && uri != null && version != null && headers == null) {
            parseHeaders();
        }
        if (method != null && uri != null && version != null && headers != null
                && headers.containsKey("content-length") && body == null) {
            parseBody();
            ready = true;
        } else if (method != null && uri != null && version != null && headers != null
                && !headers.containsKey("content-length")) {
            ready = true;
        }
    }

    public HttpRequest getHttpRequest() {
        return new HttpRequest(method, uri, version, headers, body);
    }

    public boolean ready() {
        return ready;
    }

    private void parseMethod() {
        _parseMethod();
        if (method != null) {
            int len = method.name().length();
            if (byteArray.getChar(len) == ' ') {
                // GET /hello HTTP/1.1\r\n -> /hello HTTP/1.1\r\n
                byteArray.remove(0, len + 1);
            } else {
                method = null;
            }
        }
    }

    private void parseUri() {
        StringBuilder sb = new StringBuilder();
        boolean flag = false;
        // /hello HTTP/1.1\r\n
        for (int i = 0; i < byteArray.size(); i++) {
            char c = byteArray.getChar(i);
            if (c == ' ') {
                flag = true;
                break;
            }
            sb.append(c);
        }
        if (!flag) {
            return;
        }
        uri = sb.toString();
        // /hello HTTP/1.1\r\n -> HTTP/1.1\r\n
        byteArray.remove(0, sb.length() + 1);
    }

    private void parseVersion() {
        // HTTP/1.1\r\n
        if (byteArray.size() < 10) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        // /hello HTTP/1.1\r\n
        for (int i = 0; i < 10; i++) {
            char c = byteArray.getChar(i);
            sb.append(c);
        }
        if (!sb.toString().equals("HTTP/1.1\r\n")) {
            return;
        }
        version = "HTTP/1.1";
        byteArray.remove(0, 10);
    }

    private void parseHeaders() {
        // Headers k:v\r\n
        // \r\n
        // Body
        int size = byteArray.size();
        StringBuilder sb = new StringBuilder();
        boolean flag = false;
        int i = 0;
        for (; i < size; i++) {
            if (i + 3 < size
                    && byteArray.getChar(i) == '\r' && byteArray.getChar(i + 1) == '\n'
                    && byteArray.getChar(i + 2) == '\r' && byteArray.getChar(i + 3) == '\n') {
                flag = true;
                break;
            }
            sb.append(byteArray.getChar(i));
        }
        if (!flag) {
            return;
        }
        String headersStr = sb.toString().trim();
        headers = new HttpHeaders();
        Arrays.stream(headersStr.split("\r\n"))
                .forEach(header -> {
                    String[] kv = header.split(":");
                    String key = kv[0].trim();
                    String value = kv[1].trim();
                    headers.add(key, value);
                });
        // 删除 \r\n\r\n
        byteArray.remove(0, i + 4);
    }

    private void parseBody() {
        int length = Integer.parseInt(headers.get("content-length"));
        if (byteArray.size() < length) {
            return;
        }
        byte[] bytes = byteArray.getBytes();
        body = new byte[length];
        System.arraycopy(bytes, 0, body, 0, length);
        byteArray.clear();
    }

    private void _parseMethod() {
        int size = byteArray.size();
        if (size < 3) {
            return;
        }
        char c1 = byteArray.getChar(0);
        char c2 = byteArray.getChar(1);
        char c3 = byteArray.getChar(2);
        if (c1 == 'G' && c2 == 'E' && c3 == 'T') {
            method = HttpMethod.GET;
            return;
        }
        if (c1 == 'P' && c2 == 'U' && c3 == 'T') {
            method = HttpMethod.PUT;
            return;
        }
        if (size < 4) {
            return;
        }
        char c4 = byteArray.getChar(3);
        if (c1 == 'P' && c2 == 'O' && c3 == 'S' && c4 == 'T') {
            method = HttpMethod.POST;
            return;
        }
        if (c1 == 'H' && c2 == 'E' && c3 == 'A' && c4 == 'D') {
            method = HttpMethod.HEAD;
            return;
        }
        if (size < 5) {
            return;
        }
        char c5 = byteArray.getChar(4);
        if (c1 == 'T' && c2 == 'R' && c3 == 'A' && c4 == 'C' && c5 == 'E') {
            method = HttpMethod.TRACE;
            return;
        }
        if (c1 == 'P' && c2 == 'A' && c3 == 'T' && c4 == 'C' && c5 == 'H') {
            method = HttpMethod.PATCH;
            return;
        }
        if (size < 6) {
            return;
        }
        char c6 = byteArray.getChar(5);
        if (c1 == 'D' && c2 == 'E' && c3 == 'L' && c4 == 'E' && c5 == 'T' && c6 == 'E') {
            method = HttpMethod.DELETE;
            return;
        }
        if (size < 7) {
            return;
        }
        char c7 = byteArray.getChar(6);
        if (c1 == 'C' && c2 == 'O' && c3 == 'N' && c4 == 'N' && c5 == 'E' && c6 == 'C' && c7 == 'T') {
            method = HttpMethod.CONNECT;
            return;
        }
        if (c1 == 'O' && c2 == 'P' && c3 == 'T' && c4 == 'I' && c5 == 'O' && c6 == 'N' && c7 == 'S') {
            method = HttpMethod.OPTIONS;
        }
    }
}
