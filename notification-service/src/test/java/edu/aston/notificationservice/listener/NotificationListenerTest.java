package edu.aston.notificationservice.listener;

import edu.aston.event.UserEvent;
import edu.aston.notificationservice.service.EmailNotificationService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationListenerTest {
    @Mock
    private EmailNotificationService emailNotificationService;

    @InjectMocks
    private NotificationListener notificationListener;

    @Test
    void testListen() {
        this.notificationListener.listen(new UserEvent("CREATE", "test@mail.com"));

        verify(emailNotificationService).sendMessage("CREATE", "test@mail.com");
    }
}
