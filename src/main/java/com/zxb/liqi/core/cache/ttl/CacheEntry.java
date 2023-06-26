package com.zxb.liqi.core.cache.ttl;

/**
 * @author Mr.M
 * @date 2023/6/9
 * @Description
 */
public class CacheEntry<K, V> {
    private final K key;
    private final V value;
    private final long expireTime;

    public CacheEntry(K key, V value, long expireTime) {
        this.key = key;
        this.value = value;
        this.expireTime = expireTime;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expireTime;
    }
}

