package com.gum.advertisement.app.repository;

import com.gum.advertisement.app.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AnnouncementRepo extends JpaRepository<Announcement, Long> {
}
