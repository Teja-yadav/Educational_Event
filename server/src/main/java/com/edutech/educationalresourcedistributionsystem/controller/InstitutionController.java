package com.edutech.educationalresourcedistributionsystem.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.edutech.educationalresourcedistributionsystem.entity.Event;
import com.edutech.educationalresourcedistributionsystem.entity.Resource;
import com.edutech.educationalresourcedistributionsystem.service.EventService;
import com.edutech.educationalresourcedistributionsystem.service.ResourceService;
import java.util.List;
@RestController
@RequestMapping("/api/institution")
public class InstitutionController {
   @Autowired
   private EventService eventService;
   @Autowired
   private ResourceService resourceService;
   @PostMapping(value = "/event",consumes = "application/json",produces = "application/json")
   public ResponseEntity<Event> createEvent(@RequestBody Event event) {
       return ResponseEntity.ok(eventService.createEvent(event));
   }
   @GetMapping(value = "/events",produces = "application/json")
   public ResponseEntity<List<Event>> getAllEvents() {
       return ResponseEntity.ok(eventService.getAllEvents());
   }
   @PostMapping(value = "/resource",consumes = "application/json", produces = "application/json")
   public ResponseEntity<Resource> createResource(@RequestBody Resource resource) {
       return ResponseEntity.ok(resourceService.createResource(resource));
   }
   @GetMapping(value = "/resources",produces = "application/json")
   public ResponseEntity<List<Resource>> getAllResources() {
       return ResponseEntity.ok(resourceService.getAllResources());
   }
   @PostMapping(value = "/event/allocate-resources",produces = "application/json")
   public ResponseEntity<Event> allocateResources(@RequestParam Long eventId,@RequestParam Long resourceId) {
       Event updatedEvent =eventService.allocateResource(eventId, resourceId);
       return ResponseEntity.ok(updatedEvent);
   }
}
 