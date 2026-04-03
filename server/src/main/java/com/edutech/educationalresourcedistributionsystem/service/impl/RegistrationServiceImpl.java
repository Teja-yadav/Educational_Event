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
public EventRegistration registerStudent(Long eventId, String studentId) {

    Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Event not found"));

    boolean alreadyRegistered = registrationRepository
            .findByStudentIdAndEvent_Id(studentId, eventId)
            .isPresent();

    if (alreadyRegistered) {
        throw new RuntimeException("Student already registered for this event");
    }
    
    if (event.getEventDateTime() != null && event.getEventDateTime().isBefore(java.time.LocalDateTime.now())) {
        throw new RuntimeException("Event time already passed. Cannot register.");
    }


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

    @Override
    public List<EventRegistration> getStatus(String studentId) {
        return registrationRepository.findByStudentId(studentId);
    }
}