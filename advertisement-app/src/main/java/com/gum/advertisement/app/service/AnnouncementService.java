package com.gum.advertisement.app.service;


import com.gum.advertisement.app.dto.AnnouncementDto;
import com.gum.advertisement.app.model.Announcement;

public interface AnnouncementService {
    Announcement getAnnouncement(Long id);

    Announcement createNewAnnouncement(AnnouncementDto dto);

    Announcement updateAnnouncement(Long id, AnnouncementDto newAnnouncement);

    void deleteAnnouncement(Long id);
}
