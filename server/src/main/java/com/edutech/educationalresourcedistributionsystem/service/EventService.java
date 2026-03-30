package com.edutech.educationalresourcedistributionsystem.service;
import com.edutech.educationalresourcedistributionsystem.entity.Event;
import com.edutech.educationalresourcedistributionsystem.entity.Resource;
import com.edutech.educationalresourcedistributionsystem.repository.EventRepository;
import com.edutech.educationalresourcedistributionsystem.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
@Service
public class EventService {
   @Autowired
   private EventRepository eventRepository;
   @Autowired
   private ResourceRepository resourceRepository;
   public Event createEvent(Event event) {
       return eventRepository.save(event);
   }
   public List<Event> getAllEvents() {
       return eventRepository.findAll();
   }
   public Event updateEvent(Long eventId, Event updatedEvent) {
       Event event = eventRepository.findById(eventId)
          .orElseThrow(() -> new RuntimeException("Event not found"));
      event.setName(updatedEvent.getName());
      event.setDescription(updatedEvent.getDescription());
      event.setMaterials(updatedEvent.getMaterials());
       return eventRepository.save(event);
   }
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
 