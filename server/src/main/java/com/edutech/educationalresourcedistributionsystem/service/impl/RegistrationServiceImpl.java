package com.edutech.educationalresourcedistributionsystem.service.impl;

import com.edutech.educationalresourcedistributionsystem.entity.Event;
import com.edutech.educationalresourcedistributionsystem.entity.EventRegistration;
import com.edutech.educationalresourcedistributionsystem.repository.EventRegistrationRepository;
import com.edutech.educationalresourcedistributionsystem.repository.EventRepository;
import com.edutech.educationalresourcedistributionsystem.service.RegistrationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventRegistrationRepository registrationRepository;

    @Override
    public EventRegistration registerStudent(Long eventId, Long studentId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        EventRegistration registration = new EventRegistration();
        registration.setStudentId(studentId);
        registration.setStatus("REGISTERED");
        registration.setEvent(event);

        return registrationRepository.save(registration);
    }

    @Override
    public List<EventRegistration> getAllRegistrations() {
        return registrationRepository.findAll();
    }

    // ✅ IMPLEMENT REQUIRED METHOD
    @Override
    public List<EventRegistration> getStatus(Long studentId) {
        return registrationRepository.findByStudentId(studentId);
    }
}