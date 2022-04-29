package io.github.beastars1.ouroboros.collection;

import java.util.Arrays;

/**
 * 封装 byte[] 操作
 */
public class ByteArray {
    private static final float RESIZE_FACTOR = 0.75F;
    private byte[] source = new byte[10];
    private int capacity = 10;
    private int size = 0;

    public ByteArray add(byte[] bytes) {
        int length = bytes.length;
        if (shouldResize(length)) {
            resize(length);
        }
        System.arraycopy(bytes, 0, source, size, length);
        size += length;
        return this;
    }

    /**
     * byte 1byte 8bit, int 4byte 32bit
     * 取出 index 索引位置的 int，也就是 [index, index+4) 位置的 byte 转换成 int
     * [高位在前,低位在后] 大端模式
     * byte[1 2 3 4] 的二进制就是 [00000001 00000010 00000011 00000100]
     * int 是 32 位，也就是这 32 位组成了一个 int: 16909060
     * <p>
     * 对 char，byte 或者 short 进行移位处理时，会先自动转换成一个 int:32bit
     */
    public int getInt(int index) {
        if (index + 4 > size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        byte first = source[index];
        byte second = source[index + 1];
        byte third = source[index + 2];
        byte forth = source[index + 3];
        return (first & 0xFF) << 24
                | (second & 0xFF) << 16
                | (third & 0xFF) << 8
                | (forth & 0xFF);
    }

    public char getChar(int index) {
        if (index + 1 > size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return (char) source[index];
    }

    public byte[] getBytes() {
        byte[] clone = new byte[size];
        System.arraycopy(source, 0, clone, 0, size);
        return clone;
    }

    public ByteArray clear() {
        source = new byte[10];
        size = 0;
        return this;
    }

    public int size() {
        return size;
    }

    private void resize(long newSize) {
        long newTotalSize = size + newSize;
        if (newTotalSize >= capacity) {
            capacity = (int) (newTotalSize << 1);
        } else {
            capacity = capacity << 1;
        }
        source = Arrays.copyOf(source, capacity);
    }

    /**
     * 大小超过容量的 0.75 倍时，扩容
     *
     * @param newSize 要添加的 byte[] 长度
     * @return 是否扩容
     */
    private boolean shouldResize(long newSize) {
        float maxCapacity = capacity * RESIZE_FACTOR;
        return size >= maxCapacity || size + newSize >= maxCapacity;
    }
}
