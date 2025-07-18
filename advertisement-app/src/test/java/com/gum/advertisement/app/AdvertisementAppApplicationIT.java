package com.gum.advertisement.app;

import com.gum.advertisement.app.dto.AnnouncementDto;
import com.gum.advertisement.app.model.Announcement;
import com.gum.advertisement.app.scheduler.CacheScheduler;
import com.gum.advertisement.app.utility.CacheUtility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static com.gum.advertisement.app.constant.AppConstants.CACHE_NAME;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

@MockitoBean(types = {CacheScheduler.class})
@EnableConfigurationProperties
@EnableCaching
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")

public class AdvertisementAppApplicationIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CacheManager cacheManager;

    @Test
    void shouldReturnAnnouncement() {
        String uri = "/api/an/announcement/%d".formatted(1L);
        ResponseEntity<AnnouncementDto> responseEntity = testRestTemplate.exchange(uri, HttpMethod.GET,
                null, AnnouncementDto.class);
        AnnouncementDto dto = responseEntity.getBody();

        assertNotNull(dto);
        assertEquals("t1", dto.text());
        assertEquals(1L, dto.id());
        assertEquals(3, dto.viewNum());

    }

    @Test
    void shouldAddAnnouncement() {
        String createUri = "/api/an/announcement";
        AnnouncementDto newAnnouncement = createAnnouncementDto("sample text");
        AnnouncementDto response = testRestTemplate.postForObject(createUri, newAnnouncement, AnnouncementDto.class);

        assertNotNull(response);
        assertEquals("sample text", response.text());
        assertEquals(0, response.viewNum());

        String findUri = "/api/an/announcement/%d".formatted(response.id());
        AnnouncementDto found = testRestTemplate.getForObject(findUri, AnnouncementDto.class);

        assertNotNull(found);
        assertEquals("sample text", found.text());
        assertEquals(1, found.viewNum());
    }

    @Test
    void shouldUpdateAnnouncement() {
        String updateUri = "/api/an/announcement/%d".formatted(1L);
        AnnouncementDto newAnnouncement = createAnnouncementDto("new text");
        ResponseEntity<Void> responseEntity =
                testRestTemplate.exchange(updateUri, HttpMethod.PUT, new HttpEntity<>(newAnnouncement), Void.class);

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertNull(responseEntity.getBody());
    }

    @Test
    void shouldDeleteAnnouncement() {
        String createUri = "/api/an/announcement";
        AnnouncementDto newAnnouncementDto = createAnnouncementDto("deletion test");
        AnnouncementDto response = testRestTemplate.postForObject(createUri, newAnnouncementDto, AnnouncementDto.class);

        String deleteUri = "/api/an/announcement/%d".formatted(response.id());
        ResponseEntity<Void> responseEntity =
                testRestTemplate.exchange(deleteUri, HttpMethod.DELETE, null, Void.class);

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertNull(responseEntity.getBody());
    }


    @Test
    void shouldReturnFromCache() {
        Cache springCache = cacheManager.getCache(CACHE_NAME);
        if (springCache != null) springCache.clear();

        String createUri = "/api/an/announcement";
        AnnouncementDto newAnnouncementDto = createAnnouncementDto("cache test");
        AnnouncementDto response = testRestTemplate.postForObject(createUri, newAnnouncementDto, AnnouncementDto.class);

        String getUri = "/api/an/announcement/%d".formatted(response.id());
        ResponseEntity<AnnouncementDto> responseEntity =
                testRestTemplate.exchange(getUri, HttpMethod.GET, null, AnnouncementDto.class);
        AnnouncementDto resultDto = responseEntity.getBody();

        var springCacheMap = (ConcurrentHashMap<?, ?>) Objects.requireNonNull(springCache).getNativeCache();

        assertTrue(springCacheMap.entrySet().stream().anyMatch(a -> a.getKey() == resultDto.id()
                && ((Announcement) a.getValue()).getText().equals(resultDto.text())));

        assertEquals(CacheUtility.getViewNum(resultDto.id()), resultDto.viewNum());
    }

    private AnnouncementDto createAnnouncementDto(String text) {
        return new AnnouncementDto(null, text, null, 0);
    }
}
