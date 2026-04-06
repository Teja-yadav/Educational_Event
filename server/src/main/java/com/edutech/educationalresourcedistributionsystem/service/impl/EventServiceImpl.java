package com.edutech.educationalresourcedistributionsystem.service.impl;

import com.edutech.educationalresourcedistributionsystem.entity.Event;
import com.edutech.educationalresourcedistributionsystem.entity.Resource;
import com.edutech.educationalresourcedistributionsystem.entity.User;
import com.edutech.educationalresourcedistributionsystem.repository.EventRegistrationRepository;
import com.edutech.educationalresourcedistributionsystem.repository.EventRepository;
import com.edutech.educationalresourcedistributionsystem.repository.ResourceRepository;
import com.edutech.educationalresourcedistributionsystem.repository.UserRepository;
import com.edutech.educationalresourcedistributionsystem.service.EmailService;
import com.edutech.educationalresourcedistributionsystem.service.EventService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;

@Slf4j
@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventRegistrationRepository eventRegistrationRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Value("${app.mail.admin:}")
    private String adminEmail;

    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth == null ? null : String.valueOf(auth.getPrincipal());

        if (username == null) {
            log.warn("Unauthorized access: missing principal in SecurityContext");
            throw new RuntimeException("Unauthorized");
        }

        User user = userRepository.findByUsername(username);

        if (user == null) {
            log.warn("Unauthorized access: user not found for username={}", username);
            throw new RuntimeException("Unauthorized");
        }

        return user;
    }

    @Override
    public Event createEvent(Event event) {
        User institution = currentUser();
        log.info("Create event requested. userId={}, role={}, eventName={}", institution.getId(), institution.getRole(), event.getName());

        if (!"INSTITUTION".equals(institution.getRole())) {
            log.warn("Forbidden createEvent. userId={}, role={}", institution.getId(), institution.getRole());
            throw new RuntimeException("Forbidden");
        }

        if (event.getEducatorId() == null) {
            log.warn("Create event blocked: educatorId missing. userId={}", institution.getId());
            throw new RuntimeException("Educator is required");
        }

        User educator = userRepository.findById(event.getEducatorId()).orElse(null);
        if (educator == null || !"EDUCATOR".equals(educator.getRole())) {
            log.warn("Create event blocked: invalid educator. educatorId={}", event.getEducatorId());
            throw new RuntimeException("Invalid educator");
        }

        event.setInstitutionId(institution.getId());
        Event saved = eventRepository.save(event);

        log.info("Event created successfully. eventId={}, institutionId={}, educatorId={}", saved.getId(), saved.getInstitutionId(), saved.getEducatorId());
        return saved;
    }

    @Override
    public List<Event> getAllEvents() {
        User user = currentUser();
        log.info("Fetch events requested. userId={}, role={}", user.getId(), user.getRole());

        if ("INSTITUTION".equals(user.getRole())) {
            List<Event> list = eventRepository.findByInstitutionId(user.getId());
            log.info("Events fetched for institution. institutionId={}, count={}", user.getId(), list.size());
            return list;
        }

        if ("EDUCATOR".equals(user.getRole())) {
            List<Event> list = eventRepository.findByEducatorId(user.getId());
            log.info("Events fetched for educator. educatorId={}, count={}", user.getId(), list.size());
            return list;
        }

        List<Event> list = eventRepository.findAll();
        log.info("Events fetched for other role. userId={}, count={}", user.getId(), list.size());
        return list;
    }

    @Override
    public Event updateEvent(Long eventId, Event updatedEvent) {
        User educator = currentUser();
        log.info("Update event requested. eventId={}, userId={}, role={}", eventId, educator.getId(), educator.getRole());

        if (!"EDUCATOR".equals(educator.getRole())) {
            log.warn("Forbidden updateEvent. eventId={}, userId={}, role={}", eventId, educator.getId(), educator.getRole());
            throw new RuntimeException("Forbidden");
        }

        Event event = eventRepository.findByIdAndEducatorId(eventId, educator.getId())
                .orElseThrow(() -> {
                    log.warn("Update blocked: event not found or not assigned. eventId={}, educatorId={}", eventId, educator.getId());
                    return new RuntimeException("Event not found");
                });

        if (updatedEvent.getMaterials() != null) {
            event.setMaterials(updatedEvent.getMaterials());
        }

        Event saved = eventRepository.save(event);
        log.info("Event updated successfully. eventId={}, educatorId={}", saved.getId(), educator.getId());
        return saved;
    }

    @Transactional
    @Override
    public void deleteEvent(Long eventId) {
        User institution = currentUser();
        log.warn("Delete event requested. eventId={}, userId={}, role={}", eventId, institution.getId(), institution.getRole());

        if (!"INSTITUTION".equals(institution.getRole())) {
            log.warn("Forbidden deleteEvent. eventId={}, userId={}, role={}", eventId, institution.getId(), institution.getRole());
            throw new RuntimeException("Forbidden");
        }

        Event event = eventRepository.findByIdAndInstitutionId(eventId, institution.getId())
                .orElseThrow(() -> {
                    log.warn("Delete blocked: event not found or not owned. eventId={}, institutionId={}", eventId, institution.getId());
                    return new RuntimeException("Event not found");
                });

        eventRegistrationRepository.deleteByEvent_Id(eventId);
        log.info("Deleted registrations for event. eventId={}", eventId);

        List<Resource> allocated = resourceRepository.findByEvent_Id(eventId);
        for (Resource r : allocated) {
            r.setEvent(null);
        }
        resourceRepository.saveAll(allocated);
        log.info("Cleared resource allocations for event. eventId={}, resourcesCleared={}", eventId, allocated.size());

        eventRepository.delete(event);
        log.info("Event deleted successfully. eventId={}, institutionId={}", eventId, institution.getId());
    }

    @Override
    public Event allocateResource(Long eventId, Long resourceId) {
        User institution = currentUser();
        log.info("Allocate resource requested. eventId={}, resourceId={}, userId={}, role={}", eventId, resourceId, institution.getId(), institution.getRole());

        if (!"INSTITUTION".equals(institution.getRole())) {
            log.warn("Forbidden allocateResource. eventId={}, resourceId={}, userId={}, role={}", eventId, resourceId, institution.getId(), institution.getRole());
            throw new RuntimeException("Forbidden");
        }

        Event event = eventRepository.findByIdAndInstitutionId(eventId, institution.getId())
                .orElseThrow(() -> {
                    log.warn("Allocate blocked: event not found or not owned. eventId={}, institutionId={}", eventId, institution.getId());
                    return new RuntimeException("Event not found");
                });

        Resource resource = resourceRepository.findByIdAndInstitutionId(resourceId, institution.getId())
                .orElseThrow(() -> {
                    log.warn("Allocate blocked: resource not found or not owned. resourceId={}, institutionId={}", resourceId, institution.getId());
                    return new RuntimeException("Resource not found");
                });

        resource.setEvent(event);
        resourceRepository.save(resource);

        if (event.getResourceAllocations() == null) {
            event.setResourceAllocations(new ArrayList<>());
        }
        event.getResourceAllocations().add(resource);

        Event saved = eventRepository.save(event);
        log.info("Resource allocated successfully. eventId={}, resourceId={}, institutionId={}", eventId, resourceId, institution.getId());
        return saved;
    }
}