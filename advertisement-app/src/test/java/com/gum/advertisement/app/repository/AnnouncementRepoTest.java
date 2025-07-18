package com.gum.advertisement.app.repository;


import com.gum.advertisement.app.model.Announcement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@EnableConfigurationProperties
@TestPropertySource(locations = "classpath:application-test.properties")
public class AnnouncementRepoTest {
    @Autowired
    private AnnouncementRepo repo;
    private Announcement announcement;
    private Announcement initialAnnouncement;

    @BeforeEach
    public void setUp() {
        announcement = new Announcement("test55", LocalDateTime.now().withNano(0), 0);
        initialAnnouncement = repo.save(announcement);
    }

    @AfterEach
    public void tearDown() {
        repo.delete(announcement);
    }

    @Test
    void shouldReturnAnnouncementByID() {
        Announcement savedAnnouncement = repo.findById(initialAnnouncement.getId()).orElseThrow();
        assertNotNull(savedAnnouncement);
        assertEquals(initialAnnouncement.getText(), savedAnnouncement.getText());
    }

    @Test
    void givenUser_whenUpdated_thenCanBeFoundByIdWithUpdatedData() {
        initialAnnouncement.setText("test77");
        repo.save(initialAnnouncement);

        Announcement updatedAnnouncement = repo.findById(initialAnnouncement.getId()).orElseThrow();

        assertNotNull(updatedAnnouncement);
        assertEquals("test77", updatedAnnouncement.getText());
    }
}
