package io.github.beastars1.ouroboros.objectpool;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class ByteBufferPoolTest {
    @Test
    void borrowObjectTest() {
        ByteBufferPool pool = new ByteBufferPool();
        ByteBuffer b1 = pool.borrowObject();
        boolean f = pool.returnObject(b1);
        Assertions.assertTrue(f);
        ByteBuffer b2 = pool.borrowObject();
        Assertions.assertEquals(b1, b2);
        Assertions.assertEquals(1, pool.getUsedCount());
        Assertions.assertEquals(1, pool.getSize());
        ByteBuffer b3 = pool.borrowObject();
        ByteBuffer b4 = pool.borrowObject();
        pool.returnObject(b3);
        Assertions.assertEquals(2, pool.getUsedCount());
        Assertions.assertEquals(3, pool.getSize());
    }
}