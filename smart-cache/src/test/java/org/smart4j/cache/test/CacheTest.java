package org.smart4j.cache.test;

import org.junit.Test;
import org.smart4j.cache.DefaultCacheManager;
import org.smart4j.cache.SmartCache;
import org.smart4j.cache.SmartCacheManager;

public class CacheTest {

    @Test
    public void test() {
        SmartCacheManager cacheManager = new DefaultCacheManager();
        SmartCache<String, Object> cache = cacheManager.getCache("cache_name");

        for (int i = 1; i <= 100; i++) {
            cache.put("key" + i, "value" + i);
        }

        for (int i = 1; i <= 100; i++) {
            Object value = cache.get("key" + i);
            System.out.println("key" + i + " => " + value);
        }
    }
}
