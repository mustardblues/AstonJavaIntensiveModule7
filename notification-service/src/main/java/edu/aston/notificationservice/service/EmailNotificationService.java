package edu.aston.notificationservice.service;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class EmailNotificationService {
    private final JavaMailSender javaMailSender;

    public EmailNotificationService(final JavaMailSender mailSender) {
        this.javaMailSender = mailSender;
    }

    public void sendMessage(final String action, final String email) {
        System.err.println(action + " " + email);
        final String message = switch(action) {
          case "CREATE" -> "Здравствуйте! Ваш аккаунт был успешно создан.";
          case "DELETE" -> "Здравствуйте! Ваш аккаунт был удалён.";
          default -> null;
        };

        if(message == null) {
            throw new IllegalArgumentException("Failed to detect event name");
        }

        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("test@email.com");
            simpleMailMessage.setTo(email);
            simpleMailMessage.setFrom("NOTIFICATION_SERVICE");
            simpleMailMessage.setText(message);
            this.javaMailSender.send(simpleMailMessage);
        }
        catch(MailException exception) {
            System.err.println(exception.getMessage());
            throw new RuntimeException("Failed to send message", exception);
        }
    }
}
