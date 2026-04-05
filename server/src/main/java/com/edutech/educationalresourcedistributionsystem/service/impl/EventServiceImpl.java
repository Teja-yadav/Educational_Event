package com.edutech.educationalresourcedistributionsystem.service.impl;

import com.edutech.educationalresourcedistributionsystem.entity.Event;
import com.edutech.educationalresourcedistributionsystem.entity.Resource;
import com.edutech.educationalresourcedistributionsystem.repository.EventRegistrationRepository;
import com.edutech.educationalresourcedistributionsystem.repository.EventRepository;
import com.edutech.educationalresourcedistributionsystem.repository.ResourceRepository;
import com.edutech.educationalresourcedistributionsystem.service.EmailService;
import com.edutech.educationalresourcedistributionsystem.service.EventService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private EmailService emailService;

    @Value("${app.mail.admin:}")
    private String adminEmail;

    @Override
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    @Transactional
    @Override
    public void deleteEvent(Long eventId) {

        Event event = eventRepository.findById(eventId)
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
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

@Override
public Event updateEvent(Long eventId, Event updatedEvent) {
    Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Event not found"));

    if (updatedEvent.getName() != null) {
        event.setName(updatedEvent.getName());
    }

    if (updatedEvent.getDescription() != null) {
        event.setDescription(updatedEvent.getDescription());
    }

    if (updatedEvent.getMaterials() != null) {
        event.setMaterials(updatedEvent.getMaterials());
    }

    // ✅ update Date & Time only if sent
    if (updatedEvent.getEventDateTime() != null) {
        event.setEventDateTime(updatedEvent.getEventDateTime());
    }

    // ✅ update Venue only if sent
    if (updatedEvent.getVenue() != null) {
        event.setVenue(updatedEvent.getVenue());
    }

    return eventRepository.save(event);
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

        return eventRepository.save(event);
    }
}