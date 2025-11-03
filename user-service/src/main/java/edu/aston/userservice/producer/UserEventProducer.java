package edu.aston.userservice.producer;

import edu.aston.event.UserEvent;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

@Service
public class UserEventProducer {
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    public UserEventProducer(final KafkaTemplate<String, UserEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(final String action, final String email) {
        kafkaTemplate.send("user-events-topic", new UserEvent(action, email));
    }
}
