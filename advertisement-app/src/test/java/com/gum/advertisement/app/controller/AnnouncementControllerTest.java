package com.gum.advertisement.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gum.advertisement.app.dto.AnnouncementDto;
import com.gum.advertisement.app.model.Announcement;
import com.gum.advertisement.app.service.AnnouncementService;
import com.gum.advertisement.app.utility.CacheUtility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@ExtendWith(SpringExtension.class)
@WebMvcTest(AnnouncementController.class)
public class AnnouncementControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AnnouncementService service;
    @Autowired
    private ObjectMapper mapper;

    @Test
    public void shouldReturnAnnouncement() throws Exception {
        try (MockedStatic<CacheUtility> cacheMock = Mockito.mockStatic(CacheUtility.class)) {
            cacheMock.when(() -> CacheUtility.incrementAnnouncementViewCount(any())).thenAnswer(Answers.RETURNS_DEFAULTS);
            cacheMock.when(() -> CacheUtility.getViewNum(any())).then(invocationOnMock -> 2);

            Announcement announcement = createAnnouncement("test", 2);
            when(service.getAnnouncement(anyLong())).thenReturn(announcement);
            mockMvc.perform(MockMvcRequestBuilders.get("/api/an/announcement/1"))
                    .andDo(print())
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.text").value("test"))
                    .andExpect(jsonPath("$.viewNum").value(2));
        }
    }


    @Test
    public void shouldInvokeCreateAndReturnAnnouncement() throws Exception {
        Announcement announcement = createAnnouncement("test2", 0);
        AnnouncementDto dto = createAnnouncementDto("test2", 0);
        String json = mapper.writeValueAsString(dto);
        when(service.createNewAnnouncement(any())).thenReturn(announcement);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/an/announcement")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.text").value("test2"))
                .andExpect(jsonPath("$.viewNum").value(0));

    }

    @Test
    public void shouldInvokeUpdateAnnouncement() throws Exception {
        try (MockedStatic<CacheUtility> cacheMock = Mockito.mockStatic(CacheUtility.class)) {
            cacheMock.when(() -> CacheUtility.clearAnnouncementViewCount(any())).thenAnswer(Answers.RETURNS_DEFAULTS);

            Announcement announcement = createAnnouncement("test3", 0);
            AnnouncementDto dto = createAnnouncementDto("test3", 0);
            String json = mapper.writeValueAsString(dto);
            when(service.updateAnnouncement(anyLong(), any())).thenReturn(announcement);
            mockMvc.perform(MockMvcRequestBuilders.put("/api/an/announcement/1")
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }
    }

    @Test
    public void shouldInvokeDeleteAnnouncement() throws Exception {
        try (MockedStatic<CacheUtility> cacheMock = Mockito.mockStatic(CacheUtility.class)) {
            cacheMock.when(() -> CacheUtility.clearAnnouncementViewCount(any())).thenAnswer(Answers.RETURNS_DEFAULTS);

            doNothing().when(service).deleteAnnouncement(anyLong());
            mockMvc.perform(MockMvcRequestBuilders.delete("/api/an/announcement/1"))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }
    }

    private AnnouncementDto createAnnouncementDto(String text, int viewNum) {
        return new AnnouncementDto(null, text, null, viewNum);
    }

    private Announcement createAnnouncement(String text, int vieNum) {
        return new Announcement(text, LocalDateTime.now().withNano(0), vieNum);
    }
}
