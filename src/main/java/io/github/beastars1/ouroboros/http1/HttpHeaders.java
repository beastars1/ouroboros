package io.github.beastars1.ouroboros.http1;

import java.util.LinkedList;
import java.util.List;

public class HttpHeaders {
    private final List<HttpHeader> headers = new LinkedList<>();

    public void add(String key, String value) {
        headers.add(new HttpHeader(key.toLowerCase(), value));
    }

    public void remove(String key) {
        String k = key.toLowerCase();
        headers.removeIf(header -> header.key().equals(k));
    }

    public String get(String key) {
        key = key.toLowerCase();
        for (HttpHeader header : headers) {
            if (header.key().equals(key)) {
                return header.value();
            }
        }
        return null;
    }

    public boolean containsKey(String key) {
        key = key.toLowerCase();
        for (HttpHeader header : headers) {
            if (header.key().equals(key)) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return headers.size();
    }

    public List<HttpHeader> getHeaders() {
        return headers;
    }

    @Override
    public String toString() {
        return "HttpHeaders{" +
                "headers=" + headers +
                '}';
    }
}
