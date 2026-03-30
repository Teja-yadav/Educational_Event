package com.edutech.educationalresourcedistributionsystem.controller;
import com.edutech.educationalresourcedistributionsystem.entity.Event;
import com.edutech.educationalresourcedistributionsystem.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/educator")
public class EducatorController {
   @Autowired
   private EventService eventService;
   @GetMapping(value = "/agenda",produces = "application/json")
   public List<Event> getAgenda() {
       return eventService.getAllEvents();
   }
   @PutMapping(value = "/update-material/{eventId}",consumes = "application/json",produces = "application/json")
   public Event updateMaterial(@PathVariable Long eventId,@RequestBody Event event) {
       return eventService.updateEvent(eventId, event);
   }
}
 