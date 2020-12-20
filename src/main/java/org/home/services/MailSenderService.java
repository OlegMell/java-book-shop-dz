package org.home.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService {

    @Value("${spring.mail.username}")
    private String sender;

    private final JavaMailSender mailSender;
    public MailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send (String addressee, String subject, String message) {
        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setFrom(sender);
        msg.setTo(addressee);
        msg.setSubject(subject);
        msg.setText(message);

        mailSender.send(msg);
    }
}
