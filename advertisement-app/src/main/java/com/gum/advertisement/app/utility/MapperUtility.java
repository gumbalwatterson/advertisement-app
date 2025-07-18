package com.gum.advertisement.app.utility;

import com.gum.advertisement.app.dto.AnnouncementDto;
import com.gum.advertisement.app.model.Announcement;

import java.time.LocalDateTime;

public final class MapperUtility {

    private MapperUtility() {
    }

    public static AnnouncementDto mapToDto(Announcement announcement) {
        return new AnnouncementDto(announcement.getId(), announcement.getText(), announcement.getPublicationDate(), announcement.getViewNum());
    }

    public static Announcement mapToEntity(AnnouncementDto annDto) {
        return new Announcement(annDto.text(), LocalDateTime.now().withNano(0), 0);
    }

    public static AnnouncementDto createDtoWithNewViewCount(Announcement announcement, int newViewCount) {
        return new AnnouncementDto(announcement.getId(), announcement.getText(), announcement.getPublicationDate(), newViewCount);
    }

}
