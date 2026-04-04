package com.edutech.educationalresourcedistributionsystem.service;

public interface EmailService {
    void sendSimpleMail(String to, String subject, String body);
}