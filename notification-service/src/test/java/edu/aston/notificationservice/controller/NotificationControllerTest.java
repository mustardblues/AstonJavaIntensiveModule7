package edu.aston.notificationservice.controller;

import edu.aston.event.UserEvent;
import edu.aston.notificationservice.service.EmailNotificationService;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@WebMvcTest(NotificationController.class)
public class NotificationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmailNotificationService emailNotificationService;

    @Test
    void testCreate() throws Exception {
        final UserEvent userEvent = new UserEvent("CREATE", "test@email.com");

        doNothing().when(emailNotificationService).sendMessage(userEvent.getAction(), userEvent.getEmail());

        mockMvc.perform(post("/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userEvent)))
                .andExpect(status().isOk());

        verify(emailNotificationService).sendMessage("CREATE", "test@email.com");
    }

    @Test
    void testDelete() throws Exception {
        final UserEvent userEvent = new UserEvent("DELETE", "test@email.com");

        doNothing().when(emailNotificationService).sendMessage(userEvent.getAction(), userEvent.getEmail());

        mockMvc.perform(post("/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userEvent)))
                .andExpect(status().isOk());

        verify(emailNotificationService).sendMessage("DELETE", "test@email.com");
    }
}
