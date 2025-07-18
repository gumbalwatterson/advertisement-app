package com.gum.advertisement.app.dto;

import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

import static com.gum.advertisement.app.constant.AppConstants.ANN_DEF_MSG;

public record AnnouncementDto(Long id, @NotEmpty(message = ANN_DEF_MSG) String text,
                              LocalDateTime publication, int viewNum) {
}
