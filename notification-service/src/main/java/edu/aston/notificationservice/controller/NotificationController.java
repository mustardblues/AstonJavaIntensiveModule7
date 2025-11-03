package edu.aston.notificationservice.controller;

import edu.aston.event.UserEvent;

import edu.aston.notificationservice.service.EmailNotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/send")
public class NotificationController {
    private final EmailNotificationService emailNotificationService;

    public NotificationController(final EmailNotificationService emailNotificationService) {
        this.emailNotificationService = emailNotificationService;
    }

    @PostMapping
    public ResponseEntity<?> sendNotification(@RequestBody final UserEvent userEventDTO) {
        emailNotificationService.sendMessage(userEventDTO.getAction(), userEventDTO.getEmail());
        return ResponseEntity.ok().build();
    }
}
