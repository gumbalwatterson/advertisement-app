package com.gum.advertisement.app.scheduler;

import com.gum.advertisement.app.model.Announcement;
import com.gum.advertisement.app.repository.AnnouncementRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.gum.advertisement.app.constant.AppConstants.CACHE_NAME;
import static com.gum.advertisement.app.utility.CacheUtility.clearViewsCache;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class CacheScheduler {
    private final AnnouncementRepo repo;
    private final CacheManager cacheManager;

    @Scheduled(fixedDelayString = "${ann.app.scheduler.delay:10000}")
    public void cachePersist() {
        log.info("Scheduler has started...");
        Map<Long, Integer> views = clearViewsCache();
        List<Announcement> announcements = repo.findAll();
        announcements.stream()
                .filter(a -> views.containsKey(a.getId()))
                .forEach(a -> a.setViewNum(views.get(a.getId())));
        repo.saveAllAndFlush(announcements);
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) cache.clear();
        log.info("Scheduler completed.");
    }

}
