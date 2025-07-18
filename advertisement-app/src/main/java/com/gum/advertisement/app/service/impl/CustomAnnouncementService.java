package com.gum.advertisement.app.service.impl;

import com.gum.advertisement.app.dto.AnnouncementDto;
import com.gum.advertisement.app.exception.AnnouncementException;
import com.gum.advertisement.app.model.Announcement;
import com.gum.advertisement.app.repository.AnnouncementRepo;
import com.gum.advertisement.app.service.AnnouncementService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.gum.advertisement.app.constant.AppConstants.*;
import static com.gum.advertisement.app.utility.MapperUtility.mapToEntity;


@Service
@AllArgsConstructor
public class CustomAnnouncementService implements AnnouncementService {

    private final AnnouncementRepo repository;

    @Override
    @Cacheable(cacheNames = {CACHE_NAME}, key = C_KEY)
    public Announcement getAnnouncement(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new AnnouncementException(id, MISS_ANN, HttpStatus.NOT_FOUND));
    }

    @Override
    public Announcement createNewAnnouncement(AnnouncementDto dto) {
        return repository.save(mapToEntity(dto));
    }

    @Override
    @CachePut(cacheNames = {CACHE_NAME}, key = C_KEY)
    public Announcement updateAnnouncement(Long id, AnnouncementDto newAnnouncement) {
        Announcement announcement = repository.findById(id)
                .orElseThrow(() -> new AnnouncementException(id, MISS_ANN, HttpStatus.NOT_FOUND));
        announcement.setText(newAnnouncement.text());
        announcement.setPublicationDate(LocalDateTime.now().withNano(0));
        announcement.setViewNum(0);
        return repository.save(announcement);
    }

    @Override
    @CacheEvict(cacheNames = {CACHE_NAME}, key = C_KEY)
    public void deleteAnnouncement(Long id) {
        repository.deleteById(id);
    }
}



