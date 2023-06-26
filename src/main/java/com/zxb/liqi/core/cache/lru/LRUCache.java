package com.zxb.liqi.core.cache.lru;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Mr.M
 * @date 2023/6/9
 * @Description
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;

    public LRUCache(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}
