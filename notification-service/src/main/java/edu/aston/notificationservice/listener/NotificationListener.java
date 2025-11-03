package edu.aston.notificationservice.listener;

import edu.aston.event.UserEvent;

import edu.aston.notificationservice.service.EmailNotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {
    private final EmailNotificationService emailNotificationService;

    public NotificationListener(final EmailNotificationService emailNotificationService) {
        this.emailNotificationService = emailNotificationService;
    }

    @KafkaListener(topics = "user-events-topic", groupId = "notification-group")
    public void listen(final UserEvent userEvent) {
            emailNotificationService.sendMessage(userEvent.getAction(), userEvent.getEmail());
    }
}
