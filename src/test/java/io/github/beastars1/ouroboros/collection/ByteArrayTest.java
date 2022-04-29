package io.github.beastars1.ouroboros.collection;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ByteArrayTest {
    @Test
    void getIntTest() {
        ByteArray byteArray = new ByteArray();
        byteArray.add(new byte[]{1, 2, 3, 4});
        int i1 = byteArray.getInt(0);
        assertEquals(16909060, i1);
        byteArray.add(new byte[]{5, 6});
        int i2 = byteArray.getInt(1);
        assertEquals(33752069, i2);
        int i3 = byteArray.getInt(2);
        assertEquals(50595078, i3);
    }

    @Test
    void getCharTest() {
        ByteArray byteArray = new ByteArray();
        byteArray.add(new byte[]{36, 65, 97});
        char c1 = byteArray.getChar(0);
        char c2 = byteArray.getChar(1);
        char c3 = byteArray.getChar(2);
        assertEquals('$', c1);
        assertEquals('A', c2);
        assertEquals('a', c3);
    }

    @Test
    void removeTest() {
        ByteArray byteArray = new ByteArray();
        byteArray.add(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        byteArray.remove(1, 6);
        assertArrayEquals(new byte[]{0, 7, 8, 9}, byteArray.getBytes());
    }
}