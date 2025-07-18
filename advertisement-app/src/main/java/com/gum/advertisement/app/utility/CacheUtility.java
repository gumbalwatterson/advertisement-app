package com.gum.advertisement.app.utility;

import com.gum.advertisement.app.model.Announcement;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public final class CacheUtility {

    private static final ConcurrentHashMap<Long, Integer> cacheMap = new ConcurrentHashMap<>();

    private CacheUtility() {
    }

    public static void incrementAnnouncementViewCount(Announcement announcement) {
        cacheMap.computeIfAbsent(announcement.getId(), key -> announcement.getViewNum());
        cacheMap.computeIfPresent(announcement.getId(), (key, view) -> view + 1);
    }

    public static void clearAnnouncementViewCount(Long id) {
        cacheMap.remove(id);
    }

    public static int getViewNum(Long key) {
        return cacheMap.get(key);
    }

    public static Map<Long, Integer> clearViewsCache() {
        Map<Long, Integer> cachedViews = cacheMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        cacheMap.clear();
        return cachedViews;
    }
}
