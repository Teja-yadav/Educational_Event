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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;

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
            throw new RuntimeException("Unauthorized");
        }
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("Unauthorized");
        }
        return user;
    }

    @Override
    public Event createEvent(Event event) {
        User institution = currentUser();

        if (!"INSTITUTION".equals(institution.getRole())) {
            throw new RuntimeException("Forbidden");
        }

        if (event.getEducatorId() == null) {
            throw new RuntimeException("Educator is required");
        }

        User educator = userRepository.findById(event.getEducatorId()).orElse(null);
        if (educator == null || !"EDUCATOR".equals(educator.getRole())) {
            throw new RuntimeException("Invalid educator");
        }

        event.setInstitutionId(institution.getId());
        return eventRepository.save(event);
    }

    @Override
    public List<Event> getAllEvents() {
        User user = currentUser();

        if ("INSTITUTION".equals(user.getRole())) {
            return eventRepository.findByInstitutionId(user.getId());
        }

        if ("EDUCATOR".equals(user.getRole())) {
            return eventRepository.findByEducatorId(user.getId());
        }

        return eventRepository.findAll();
    }

    @Override
    public Event updateEvent(Long eventId, Event updatedEvent) {
        User educator = currentUser();

        if (!"EDUCATOR".equals(educator.getRole())) {
            throw new RuntimeException("Forbidden");
        }

        Event event = eventRepository.findByIdAndEducatorId(eventId, educator.getId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (updatedEvent.getMaterials() != null) {
            event.setMaterials(updatedEvent.getMaterials());
        }

        return eventRepository.save(event);
    }

    @Transactional
    @Override
    public void deleteEvent(Long eventId) {
        User institution = currentUser();

        if (!"INSTITUTION".equals(institution.getRole())) {
            throw new RuntimeException("Forbidden");
        }

        Event event = eventRepository.findByIdAndInstitutionId(eventId, institution.getId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        eventRegistrationRepository.deleteByEvent_Id(eventId);

        List<Resource> allocated = resourceRepository.findByEvent_Id(eventId);
        for (Resource r : allocated) {
            r.setEvent(null);
        }
        resourceRepository.saveAll(allocated);

        eventRepository.delete(event);
    }

    @Override
    public Event allocateResource(Long eventId, Long resourceId) {
        User institution = currentUser();

        if (!"INSTITUTION".equals(institution.getRole())) {
            throw new RuntimeException("Forbidden");
        }

        Event event = eventRepository.findByIdAndInstitutionId(eventId, institution.getId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Resource resource = resourceRepository.findByIdAndInstitutionId(resourceId, institution.getId())
                .orElseThrow(() -> new RuntimeException("Resource not found"));

        resource.setEvent(event);
        resourceRepository.save(resource);

        if (event.getResourceAllocations() == null) {
            event.setResourceAllocations(new ArrayList<>());
        }
        event.getResourceAllocations().add(resource);

        return eventRepository.save(event);
    }
}