package com.gum.advertisement.app.service;

import com.gum.advertisement.app.dto.AnnouncementDto;
import com.gum.advertisement.app.model.Announcement;

import com.gum.advertisement.app.repository.AnnouncementRepo;
import com.gum.advertisement.app.service.impl.CustomAnnouncementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomAnnouncementServiceTest {

    @Mock
    private AnnouncementRepo repository;
    @InjectMocks
    private CustomAnnouncementService service;

    @Test
    void shouldReturnAnnouncement() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(createAnnouncement("test", 1)));
        Announcement announcement = service.getAnnouncement(1L);
        assertNotNull(announcement);
        assertEquals("test", announcement.getText());
        verify(repository, times(1)).findById(anyLong());
    }


    @Test
    void shouldCallSaveAndReturnAnnouncement() {
        when(repository.save(any())).thenReturn(createAnnouncement("test", 0));
        Announcement announcement = service.createNewAnnouncement(createAnnouncementDto("test", 0));
        assertNotNull(announcement);
        assertEquals("test", announcement.getText());
        verify(repository, times(1)).save(any());
    }

    @Test
    void shouldCallUpdateAndReturnAnnouncement() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(createAnnouncement("test", 2)));
        when(repository.save(any())).thenReturn(createAnnouncement("newTest", 0));
        Announcement announcement = service.updateAnnouncement(1L,createAnnouncementDto("newTest", 0));
        assertNotNull(announcement);
        assertEquals("newTest", announcement.getText());
        verify(repository, times(1)).save(any());
        verify(repository, times(1)).findById(anyLong());

    }

    @Test
    void shouldCallDeleteAnnouncement() {
        doNothing().when(repository).deleteById(anyLong());
        service.deleteAnnouncement(1L);
        verify(repository, times(1)).deleteById(anyLong());
    }

    private AnnouncementDto createAnnouncementDto(String text, int viewNum) {
        return new AnnouncementDto(null, text, null, viewNum);
    }

    private Announcement createAnnouncement(String text, int vieNum) {
        return new Announcement(text, LocalDateTime.now().withNano(0), vieNum);
    }
}
