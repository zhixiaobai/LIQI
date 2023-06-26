package com.zxb.liqi.core.cache;

import com.zxb.liqi.core.cache.lru.LRUCache;

import java.util.Map;

/**
 * @author Mr.M
 * @date 2023/6/8
 * @Description 缓存
 */
public class Cache {

    public static LRUCache<Class<?>, Map<String, String>> lruCache = new LRUCache<>(100);
}
