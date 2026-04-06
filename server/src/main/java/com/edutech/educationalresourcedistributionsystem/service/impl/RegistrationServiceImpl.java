package com.edutech.educationalresourcedistributionsystem.service.impl;

import com.edutech.educationalresourcedistributionsystem.entity.Event;
import com.edutech.educationalresourcedistributionsystem.entity.EventRegistration;
import com.edutech.educationalresourcedistributionsystem.repository.EventRegistrationRepository;
import com.edutech.educationalresourcedistributionsystem.repository.EventRepository;
import com.edutech.educationalresourcedistributionsystem.service.EmailService;
import com.edutech.educationalresourcedistributionsystem.service.RegistrationService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
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
    public EventRegistration registerStudent(Long eventId, String studentUsername) {

        log.info("Registration workflow started. eventId={}, student={}", eventId, studentUsername);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    log.warn("Registration blocked: event not found. eventId={}", eventId);
                    return new RuntimeException("Event not found");
                });

        boolean alreadyRegistered = registrationRepository
                .findByStudentIdAndEvent_Id(studentUsername, eventId)
                .isPresent();

        if (alreadyRegistered) {
            log.warn("Duplicate registration attempt blocked. eventId={}, student={}", eventId, studentUsername);
            safeMail(
                    "Duplicate Registration Blocked: " + event.getName(),
                    "Duplicate registration attempt blocked.\n\n" +
                            "Student Username: " + studentUsername + "\n" +
                            "Event ID: " + eventId + "\n" +
                            "Event Name: " + event.getName() + "\n" +
                            "Time: " + LocalDateTime.now() + "\n"
            );
            throw new RuntimeException("Student already registered for this event");
        }

        if (event.getEventDateTime() != null && event.getEventDateTime().isBefore(LocalDateTime.now())) {
            log.warn("Registration blocked: event already passed. eventId={}, student={}, eventDateTime={}",
                    eventId, studentUsername, event.getEventDateTime());
            safeMail(
                    "Registration Blocked (Event Passed): " + event.getName(),
                    "Registration blocked because event time already passed.\n\n" +
                            "Student Username: " + studentUsername + "\n" +
                            "Event ID: " + eventId + "\n" +
                            "Event Name: " + event.getName() + "\n" +
                            "Event DateTime: " + event.getEventDateTime() + "\n" +
                            "Time: " + LocalDateTime.now() + "\n"
            );
            throw new RuntimeException("Event time already passed. Cannot register.");
        }

        EventRegistration registration = new EventRegistration();
        registration.setStudentId(studentUsername);
        registration.setStatus("REGISTERED");
        registration.setEvent(event);

        EventRegistration saved = registrationRepository.save(registration);

        log.info("Registration saved successfully. registrationId={}, eventId={}, student={}",
                saved.getId(), eventId, studentUsername);

        safeMail(
                "Student Registered Successfully: " + event.getName(),
                "A student successfully registered for an event.\n\n" +
                        "Student Username: " + studentUsername + "\n" +
                        "Event ID: " + eventId + "\n" +
                        "Event Name: " + event.getName() + "\n" +
                        "Event DateTime: " + event.getEventDateTime() + "\n" +
                        "Status: " + saved.getStatus() + "\n"
        );

        return saved;
    }

    @Override
    public List<EventRegistration> getStatus(String studentUsername) {
        log.info("Fetching registrations for student. student={}", studentUsername);
        List<EventRegistration> list = registrationRepository.findByStudentId(studentUsername);
        log.info("Registrations fetched. student={}, count={}", studentUsername, list.size());
        return list;
    }

    @Override
    public Long getRegistrationCount() {
        log.info("Fetching registration count");
        return registrationRepository.count();
    }

    private void safeMail(String subject, String body) {
        if (adminEmail == null || adminEmail.isBlank()) {
            return;
        }
        try {
            emailService.sendSimpleMail(adminEmail, subject, body);
            log.info("Admin email sent. subject={}", subject);
        } catch (Exception e) {
            log.error("Admin email failed. subject={}", subject, e);
        }
    }
}