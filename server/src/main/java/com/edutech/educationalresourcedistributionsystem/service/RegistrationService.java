package com.edutech.educationalresourcedistributionsystem.service;

import java.util.List;
import com.edutech.educationalresourcedistributionsystem.entity.EventRegistration;

public interface RegistrationService {
    EventRegistration registerStudent(Long eventId, String studentUsername);
    List<EventRegistration> getStatus(String studentUsername);
    Long getRegistrationCount();
    List<EventRegistration> getInstitutionRegistrations(Long institutionId);
}