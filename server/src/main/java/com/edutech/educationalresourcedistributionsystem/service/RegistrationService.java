package com.edutech.educationalresourcedistributionsystem.service;

import com.edutech.educationalresourcedistributionsystem.entity.EventRegistration;
import java.util.List;

public interface RegistrationService {
    EventRegistration registerStudent(Long eventId, Long studentId);
    List<EventRegistration> getAllRegistrations();
    List<EventRegistration>getStatus(Long studentId);
}