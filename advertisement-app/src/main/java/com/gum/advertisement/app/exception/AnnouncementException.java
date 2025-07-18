package com.gum.advertisement.app.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AnnouncementException extends RuntimeException {

    private final HttpStatus status;
    private final Long id;

    public AnnouncementException(Long id,String msg, HttpStatus status) {
        super(msg);
        this.status = status;
        this.id = id;
    }
}
