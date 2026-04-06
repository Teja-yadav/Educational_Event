package com.edutech.educationalresourcedistributionsystem.controller;

import com.edutech.educationalresourcedistributionsystem.dto.EventRegistrationDto;
import com.edutech.educationalresourcedistributionsystem.entity.Event;
import com.edutech.educationalresourcedistributionsystem.entity.EventRegistration;
import com.edutech.educationalresourcedistributionsystem.service.EventService;
import com.edutech.educationalresourcedistributionsystem.service.RegistrationService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private EventService eventService;

    @PostMapping("/register/{eventId}")
    public EventRegistration registerStudent(
            @PathVariable Long eventId,
            @Valid @RequestBody EventRegistrationDto dto
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth == null ? null : String.valueOf(auth.getPrincipal());

        log.info("Student registration request. eventId={}, username={}", eventId, username);

        if (username == null) {
            log.error("Unauthorized registration attempt");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        if (!username.equalsIgnoreCase(dto.getStudentId().trim())) {
            log.warn("Username mismatch during registration. tokenUser={}, requestUser={}",
                    username, dto.getStudentId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You can register only with your own username");
        }

        try {
            EventRegistration saved = registrationService.registerStudent(eventId, username);
            log.info("Student registered successfully. registrationId={}", saved.getId());
            return saved;
        } catch (RuntimeException e) {
            log.error("Error during student registration", e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @GetMapping("/my-bookings")
    public List<EventRegistration> myBookings() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth == null ? null : String.valueOf(auth.getPrincipal());

        log.info("Fetching bookings for student. username={}", username);

        if (username == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        return registrationService.getStatus(username);
    }

    @GetMapping("/events")
    public List<Event> getAllEventsForStudent() {
        log.info("Fetching all events for student");
        return eventService.getAllEvents();
    }
}