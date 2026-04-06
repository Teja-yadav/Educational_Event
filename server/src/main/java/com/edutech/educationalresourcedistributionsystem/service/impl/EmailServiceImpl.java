package com.edutech.educationalresourcedistributionsystem.service.impl;

import com.edutech.educationalresourcedistributionsystem.service.EmailService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendSimpleMail(String to, String subject, String body) {

        log.info("Preparing to send email. to={}", to);

        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(body);

            mailSender.send(msg);

            log.info("Email sent successfully. to={}", to);

        } catch (Exception e) {
            log.error("Failed to send email. to={}", to, e);
            throw e;
        }
    }
}