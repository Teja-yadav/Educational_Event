package com.edutech.educationalresourcedistributionsystem.service.impl;

import com.edutech.educationalresourcedistributionsystem.entity.Event;
import com.edutech.educationalresourcedistributionsystem.entity.Resource;
import com.edutech.educationalresourcedistributionsystem.repository.EventRepository;
import com.edutech.educationalresourcedistributionsystem.repository.ResourceRepository;
import com.edutech.educationalresourcedistributionsystem.service.EventService;
import com.edutech.educationalresourcedistributionsystem.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private EmailService emailService;

    // Put institution/admin email in application.properties:
    // app.mail.admin=someone@domain.com
    @Value("${app.mail.admin:}")
    private String adminEmail;

    @Override
    public Event createEvent(Event event) {
        Event saved = eventRepository.save(event);

        return saved;
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event updateEvent(Long eventId, Event updatedEvent) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        event.setName(updatedEvent.getName());
        event.setDescription(updatedEvent.getDescription());
        event.setMaterials(updatedEvent.getMaterials());

        Event saved = eventRepository.save(event);

        return saved;
    }

    @Override
    public Event allocateResource(Long eventId, Long resourceId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("Resource not found"));

        resource.setEvent(event);
        resourceRepository.save(resource);

        if (event.getResourceAllocations() == null) {
            event.setResourceAllocations(new ArrayList<>());
        }
        event.getResourceAllocations().add(resource);

        Event saved = eventRepository.save(event);

        return saved;
    }
}