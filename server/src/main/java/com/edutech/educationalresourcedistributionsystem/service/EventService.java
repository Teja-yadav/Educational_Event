package com.edutech.educationalresourcedistributionsystem.service;

import com.edutech.educationalresourcedistributionsystem.entity.Event;
import java.util.List;

public interface EventService {
    Event createEvent(Event event);
    List<Event> getAllEvents();
    void deleteEvent(Long eventId);
    Event updateEvent(Long eventId, Event updatedEvent);
    Event allocateResource(Long eventId, Long resourceId);
}