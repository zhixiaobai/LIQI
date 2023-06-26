package com.zxb.liqi.core.cache.ttl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Mr.M
 * @date 2023/6/9
 * @Description
 */
public class TTLCache<K, V> {
    private final ConcurrentHashMap<K, CacheEntry<K, V>> cache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);

    public TTLCache(long ttl) {
        scheduler.scheduleAtFixedRate(() -> {
            long now = System.currentTimeMillis();
            cache.forEach((k, v) -> {
                if (v.isExpired()) {
                    cache.remove(k);
                }
            });
        }, ttl, ttl, TimeUnit.MILLISECONDS);
    }

    public void put(K key, V value, long ttl) {
        cache.put(key, new CacheEntry<>(key, value, System.currentTimeMillis() + ttl));
    }

    public V get(K key) {
        CacheEntry<K, V> entry = cache.get(key);
        return (entry != null && !entry.isExpired()) ? entry.getValue() : null;
    }

    public void remove(K key) {
        cache.remove(key);
    }

    public int size() {
        return cache.size();
    }

    public void clear() {
        cache.clear();
    }
}

