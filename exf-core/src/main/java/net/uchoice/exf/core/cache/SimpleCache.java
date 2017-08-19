package net.uchoice.exf.core.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class SimpleCache {

    private final Map<String, ConcurrentMap<Object, Object>> caches = Maps.newHashMap();

    @SuppressWarnings("unchecked")
    protected <T> T doCacheObject(String cacheName, Object key, Object obj) {
        ConcurrentMap<Object, Object> cache = caches.computeIfAbsent(cacheName, k -> Maps.newConcurrentMap());
        cache.put(key, obj);
        return (T) obj;
    }

    @SuppressWarnings("unchecked")
    protected <T> T getCachedEntity(String cacheName, Object key) {
        if(key == null){
            return null;
        }
        ConcurrentMap<?, ?> cache = caches.get(cacheName);
        if (null == cache)
            return null;
        return (T) cache.get(key);
    }

    protected boolean isEntitiesCached(String cacheName, Object key) {
        return null != getCachedEntity(cacheName, key);
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> getAllCacheEntities(String cacheName) {
        ConcurrentMap<?, ?> cache = caches.get(cacheName);
        if (null == cache)
            return Lists.newArrayList();
        return (List<T>) cache.entrySet().stream().map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
