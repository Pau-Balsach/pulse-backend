package com.pulse.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationService {

    private final JavaMailSender mailSender;

    @Value("${notification.email.to}")
    private String emailTo;

    public void sendIncidentAlert(String serviceName, String serviceUrl, String errorMessage) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailTo);
            message.setSubject("[Pulse] Incident OPEN — " + serviceName);
            message.setText(
                    "An incident has been detected.\n\n" +
                            "Service: " + serviceName + "\n" +
                            "URL: " + serviceUrl + "\n" +
                            "Error: " + (errorMessage != null ? errorMessage : "No response") + "\n\n" +
                            "Check your Pulse dashboard for more details."
            );
            mailSender.send(message);
            log.info("Incident alert sent for service: {}", serviceName);
        } catch (Exception e) {
            log.error("Failed to send email notification: {}", e.getMessage());
        }
    }
}