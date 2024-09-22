package com.frigotermica.tema.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail,
                          String subject,
                          String message) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setFrom("frigotermicasrl1@gmail.com");
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(message, true);

        mailSender.send(mimeMessage);

        System.out.println("Mail sent to: " + toEmail);
    }
}