package com.edutech.educationalresourcedistributionsystem.service.impl;

import com.edutech.educationalresourcedistributionsystem.entity.Event;
import com.edutech.educationalresourcedistributionsystem.entity.EventRegistration;
import com.edutech.educationalresourcedistributionsystem.repository.EventRegistrationRepository;
import com.edutech.educationalresourcedistributionsystem.repository.EventRepository;
import com.edutech.educationalresourcedistributionsystem.service.EmailService;
import com.edutech.educationalresourcedistributionsystem.service.RegistrationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventRegistrationRepository registrationRepository;

    @Autowired
    private EmailService emailService;

    @Value("${app.mail.admin:}")
    private String adminEmail;

    @Override
    public EventRegistration registerStudent(Long eventId, String studentId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        boolean alreadyRegistered = registrationRepository
                .findByStudentIdAndEvent_Id(studentId, eventId)
                .isPresent();

        if (alreadyRegistered) {
            if (adminEmail != null && !adminEmail.isBlank()) {
                String subject = "Duplicate Registration Blocked: " + event.getName();
                String body =
                        "Duplicate registration attempt blocked.\n\n" +
                        "Student ID: " + studentId + "\n" +
                        "Event ID: " + eventId + "\n" +
                        "Event Name: " + event.getName() + "\n" +
                        "Time: " + LocalDateTime.now() + "\n";
                emailService.sendSimpleMail(adminEmail, subject, body);
            }
            throw new RuntimeException("Student already registered for this event");
        }

        if (event.getEventDateTime() != null && event.getEventDateTime().isBefore(LocalDateTime.now())) {
            if (adminEmail != null && !adminEmail.isBlank()) {
                String subject = "Registration Blocked (Event Passed): " + event.getName();
                String body =
                        "Registration blocked because event time already passed.\n\n" +
                        "Student ID: " + studentId + "\n" +
                        "Event ID: " + eventId + "\n" +
                        "Event Name: " + event.getName() + "\n" +
                        "Event DateTime: " + event.getEventDateTime() + "\n" +
                        "Time: " + LocalDateTime.now() + "\n";
                emailService.sendSimpleMail(adminEmail, subject, body);
            }
            throw new RuntimeException("Event time already passed. Cannot register.");
        }

        EventRegistration registration = new EventRegistration();
        registration.setStudentId(studentId);
        registration.setStatus("REGISTERED");
        registration.setEvent(event);

        EventRegistration saved = registrationRepository.save(registration);

        if (adminEmail != null && !adminEmail.isBlank()) {
            String subject = "Student Registered Successfully: " + event.getName();
            String body =
                    "A student successfully registered for an event.\n\n" +
                    "Student ID: " + studentId + "\n" +
                    "Event ID: " + eventId + "\n" +
                    "Event Name: " + event.getName() + "\n" +
                    "Event DateTime: " + event.getEventDateTime() + "\n" +
                    "Status: " + saved.getStatus() + "\n";
            emailService.sendSimpleMail(adminEmail, subject, body);
        }

        return saved;
    }

    @Override
    public List<EventRegistration> getAllRegistrations() {
        return registrationRepository.findAll();
    }

    @Override
    public List<EventRegistration> getStatus(String studentId) {
        return registrationRepository.findByStudentId(studentId);
    }

    @Override
    public long getRegistrationCount() {
        return registrationRepository.count();
    }
}