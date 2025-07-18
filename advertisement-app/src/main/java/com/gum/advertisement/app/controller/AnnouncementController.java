package com.gum.advertisement.app.controller;

import com.gum.advertisement.app.dto.AnnouncementDto;
import com.gum.advertisement.app.model.Announcement;
import com.gum.advertisement.app.service.AnnouncementService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.gum.advertisement.app.constant.AppConstants.*;
import static com.gum.advertisement.app.utility.CacheUtility.*;
import static com.gum.advertisement.app.utility.MapperUtility.*;

@Slf4j
@RestController
@RequestMapping(ANN_CONT)
@Validated
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService service;

    @GetMapping(ANN_ID_ENDPOINT)
    public ResponseEntity<AnnouncementDto> findAnnouncement(@PathVariable
                                                            @Positive(message = ARG_ID) Long id) {
        Announcement announcement = service.getAnnouncement(id);
        incrementAnnouncementViewCount(announcement);
        return ResponseEntity.ok(createDtoWithNewViewCount(announcement, getViewNum(announcement.getId())));
    }

    @PostMapping(ANN_ENDPOINT)
    public ResponseEntity<AnnouncementDto> addAnnouncement(@RequestBody @Valid AnnouncementDto dto) {
        Announcement announcement = service.createNewAnnouncement(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDto(announcement));
    }

    @PutMapping(ANN_ID_ENDPOINT)
    public ResponseEntity<Void> changeAnnouncement(@RequestBody @Valid AnnouncementDto dto,
                                                   @PathVariable
                                                   @Positive(message = ARG_ID)
                                                   Long id) {
        Announcement announcement = service.updateAnnouncement(id, dto);
        clearAnnouncementViewCount(announcement.getId());
        log.debug("Updated cache for key: {}", id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(ANN_ID_ENDPOINT)
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable
                                                   @Positive(message = ARG_ID) Long id) {
        service.deleteAnnouncement(id);
        clearAnnouncementViewCount(id);
        log.debug("Cleared cache for key: {}", id);
        return ResponseEntity.noContent().build();
    }
}
