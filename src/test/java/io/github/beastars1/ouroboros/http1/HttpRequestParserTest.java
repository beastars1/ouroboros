package io.github.beastars1.ouroboros.http1;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestParserTest {
    @Test
    void getRequestTest() {
        HttpRequestParser parser = new HttpRequestParser();
        String s = """
                GET / HTTP/1.1\r
                host: beastars1.github.io\r
                cache-control: max-age=0\r
                \r
                """;
        ByteBuffer buf = ByteBuffer.wrap(s.getBytes(StandardCharsets.UTF_8));
        parser.read(buf);
        assertTrue(parser.ready());

        HttpRequest httpRequest = parser.getHttpRequest();
        assertEquals(HttpMethod.GET, httpRequest.httpMethod());
        assertEquals("/", httpRequest.uri());
        assertEquals("HTTP/1.1", httpRequest.version());

        HttpHeaders headers = httpRequest.httpHeaders();
        assertEquals(2, headers.size());
        assertEquals("beastars1.github.io", headers.get("host"));
        assertEquals("max-age=0", headers.get("cache-control"));

        assertNull(httpRequest.body());
    }

    @Test
    void postRequestTest() {
        HttpRequestParser parser = new HttpRequestParser();
        String s = """
                POST /hello HTTP/1.1\r
                host: beastars1.github.io\r
                content-length: 34\r
                content-type: application/json\r
                \r
                {"user":"jack", "pwd":"beastars1"}
                """;
        ByteBuffer buf = ByteBuffer.wrap(s.getBytes(StandardCharsets.UTF_8));
        parser.read(buf);
        assertTrue(parser.ready());

        HttpRequest httpRequest = parser.getHttpRequest();
        assertEquals(HttpMethod.POST, httpRequest.httpMethod());
        assertEquals("/hello", httpRequest.uri());
        assertEquals("HTTP/1.1", httpRequest.version());

        HttpHeaders headers = httpRequest.httpHeaders();
        assertEquals(3, headers.size());
        assertEquals("beastars1.github.io", headers.get("host"));
        assertEquals("34", headers.get("content-length"));
        assertEquals("application/json", headers.get("content-type"));

        assertArrayEquals("{\"user\":\"jack\", \"pwd\":\"beastars1\"}".getBytes(StandardCharsets.UTF_8), httpRequest.body());
    }

    @Test
    void deleteRequestTest() {
        HttpRequestParser parser = new HttpRequestParser();
        String s = "DELETE / HTTP/1.1\r\n"
                + "host: beastars1.github.io\r\n"
                + "\r\n";
        ByteBuffer buf = ByteBuffer.wrap(s.getBytes(StandardCharsets.UTF_8));
        parser.read(buf);
        assertTrue(parser.ready());

        HttpRequest httpRequest = parser.getHttpRequest();
        assertEquals(HttpMethod.DELETE, httpRequest.httpMethod());
        assertEquals("/", httpRequest.uri());
        assertEquals("HTTP/1.1", httpRequest.version());

        HttpHeaders headers = httpRequest.httpHeaders();
        assertEquals(1, headers.size());
        assertEquals("beastars1.github.io", headers.get("host"));

        assertNull(httpRequest.body());
    }
}