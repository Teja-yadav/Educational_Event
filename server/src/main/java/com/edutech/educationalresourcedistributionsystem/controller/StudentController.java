package com.edutech.educationalresourcedistributionsystem.controller;
import com.edutech.educationalresourcedistributionsystem.entity.Event;
import com.edutech.educationalresourcedistributionsystem.entity.EventRegistration;
import com.edutech.educationalresourcedistributionsystem.service.EventService;
import com.edutech.educationalresourcedistributionsystem.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/student")
public class StudentController {
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private EventService eventService;   
@PostMapping(value = "/register/{eventId}", consumes = "application/json", produces = "application/json")
public EventRegistration registerStudent(@PathVariable Long eventId, @RequestBody EventRegistration registration) {
    try {
        return registrationService.registerStudent(eventId, registration.getStudentId());
    } catch (RuntimeException e) {
        throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.CONFLICT,
                e.getMessage()
        );
    }
}
    @GetMapping(value = "/registration-status/{studentId}",produces = "application/json")
    public List<EventRegistration> getStatus(@PathVariable String studentId) {
        return registrationService.getStatus(studentId);
    }
    @GetMapping(value = "/events", produces = "application/json")
    public List<Event> getAllEventsForStudent() {
        return eventService.getAllEvents();   
    }
}
