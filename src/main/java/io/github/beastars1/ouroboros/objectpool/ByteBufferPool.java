package io.github.beastars1.ouroboros.objectpool;

import java.nio.ByteBuffer;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * warn：线程不安全
 */
public class ByteBufferPool {
    // IdentityHashMap 通过"=="比较 key，也就是比较对象地址值，value 中 false 表示未使用
    private final Map<ByteBuffer, Boolean> map = new IdentityHashMap<>();
    private static final int BUFFER_SIZE = 1024;
    // 已使用的 byteBuffer 总数量
    private int totalCount = 0;
    // 正在使用的 byteBuffer 数量
    private int usedCount = 0;

    public int getSize() {
        return map.size();
    }

    public int getUsedCount() {
        return usedCount;
    }

    /**
     * 索要 byteBuffer
     */
    public ByteBuffer borrowObject() {
        Optional<ByteBuffer> optional = map
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(false)) // 寻找未被使用的 byteBuffer
                .map(Map.Entry::getKey)
                .findAny();
        usedCount++;
        if (optional.isPresent()) {
            // 如果存在，返回 byteBuffer，设为 true
            ByteBuffer byteBuffer = optional.get();
            map.put(byteBuffer, true); // 直接覆盖
            return byteBuffer;
        }
        // 如果没有，新分配一个
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
        map.put(byteBuffer, true);
        totalCount++;
        return byteBuffer;
    }

    /**
     * 返还 byteBuffer
     *
     * @return ture 表示返还成功，false 表示没有这个 byteBuffer
     */
    public boolean returnObject(ByteBuffer byteBuffer) {
        if (!map.containsKey(byteBuffer)) {
            // 不存在直接返回 false
            return false;
        }
        // 重置
        byteBuffer.clear();
        map.put(byteBuffer, false);
        usedCount--;
        return true;
    }

    @Override
    public String toString() {
        return "ByteBufferPool{" +
                ", totalCount=" + totalCount +
                ", usedCount=" + usedCount +
                '}';
    }
}
