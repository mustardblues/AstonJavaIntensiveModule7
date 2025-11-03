package edu.aston.notificationservice.service;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.Message;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration"
})
public class EmailNotificationServiceTest {

    private static final GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP);

    @Autowired
    private EmailNotificationService emailNotificationService;

    @TestConfiguration
    static class TestMailConfig {
        @Bean
        public JavaMailSender javaMailSender() {
            JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
            javaMailSender.setHost("localhost");
            javaMailSender.setPort(3025);
            javaMailSender.getJavaMailProperties().put("mail.smtp.auth", "false");
            javaMailSender.getJavaMailProperties().put("mail.smtp.starttls.enable", "false");
            return javaMailSender;
        }
    }

    @BeforeAll
    static void startMailServer() {
        greenMail.start();
    }

    @AfterAll
    static void stopMailServer() {
        greenMail.stop();
    }

    @Test
    void testCreateMessage() throws Exception {
        emailNotificationService.sendMessage("CREATE", "test@email.com");

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();

        Message message = receivedMessages[1];
        assertEquals("Здравствуйте! Ваш аккаунт был успешно создан.", message.getContent().toString());
        assertEquals("test@email.com", message.getAllRecipients()[0].toString());
    }

    @Test
    void testDeleteMessage() throws Exception {
        emailNotificationService.sendMessage("DELETE", "test@email.com");

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();

        Message message = receivedMessages[0];

        assertEquals("Здравствуйте! Ваш аккаунт был удалён.", message.getContent().toString());
        assertEquals("test@email.com", message.getAllRecipients()[0].toString());
    }

    @Test
    void testException() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> emailNotificationService.sendMessage("", "test@email.com"));
    }
}
