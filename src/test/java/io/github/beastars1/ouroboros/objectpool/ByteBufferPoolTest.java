package io.github.beastars1.ouroboros.objectpool;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.*;

public class ByteBufferPoolTest {
    @Test
    void borrowObjectTest() {
        ByteBufferPool pool = new ByteBufferPool();
        ByteBuffer b1 = pool.borrowObject();
        boolean f = pool.returnObject(b1);
        assertTrue(f);
        ByteBuffer b2 = pool.borrowObject();
        assertEquals(b1, b2);
        assertEquals(1, pool.getUsedCount());
        assertEquals(1, pool.getSize());
        ByteBuffer b3 = pool.borrowObject();
        ByteBuffer b4 = pool.borrowObject();
        pool.returnObject(b3);
        assertEquals(2, pool.getUsedCount());
        assertEquals(3, pool.getSize());
    }

    @Test
    void resizeTest() {
        ByteBufferPool pool = new ByteBufferPool();
        ByteBuffer b1 = pool.borrowObject();
        ByteBuffer b2 = pool.borrowObject();
        ByteBuffer b3 = pool.borrowObject();
        assertEquals(3, pool.getUsedCount());
        assertEquals(3, pool.getSize());
        pool.returnObject(b1);
        pool.returnObject(b2);
        assertEquals(1, pool.getUsedCount());
        assertEquals(2, pool.getSize());
    }
}