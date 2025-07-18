package com.gum.advertisement.app.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="announcement")
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    private String text;
    @Column(name="publication_Date")
    private LocalDateTime publicationDate;
    @Column(name="view_number")
    private int viewNum;

    public Announcement(String text, LocalDateTime publicationDate, int viewNum) {
        this.text = text;
        this.publicationDate = publicationDate;
        this.viewNum = viewNum;
    }
}
