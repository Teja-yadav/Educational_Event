package com.edutech.educationalresourcedistributionsystem.controller;

import com.edutech.educationalresourcedistributionsystem.dto.DtoMapper;
import com.edutech.educationalresourcedistributionsystem.dto.EventDto;
import com.edutech.educationalresourcedistributionsystem.dto.ResourceDto;
import com.edutech.educationalresourcedistributionsystem.entity.Event;
import com.edutech.educationalresourcedistributionsystem.entity.Resource;
import com.edutech.educationalresourcedistributionsystem.service.EventService;
import com.edutech.educationalresourcedistributionsystem.service.RegistrationService;
import com.edutech.educationalresourcedistributionsystem.service.ResourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/institution")
public class InstitutionController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private RegistrationService registrationService;

    @PostMapping(value = "/event", consumes = "application/json", produces = "application/json")
    public ResponseEntity<EventDto> createEvent(@RequestBody EventDto eventDto) {
        Event event = new Event();
        event.setName(eventDto.getName());
        event.setDescription(eventDto.getDescription());
        event.setMaterials(eventDto.getMaterials());
        event.setEventDateTime(eventDto.getEventDateTime());
        event.setVenue(eventDto.getVenue());
        event.setEducatorId(eventDto.getEducatorId());

        return ResponseEntity.ok(DtoMapper.toEventDTO(eventService.createEvent(event)));
    }

    @DeleteMapping(value = "/event/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.ok("Event deleted successfully");
    }

    @GetMapping(value = "/events", produces = "application/json")
    public ResponseEntity<List<EventDto>> getAllEvents() {
        return ResponseEntity.ok(
                eventService.getAllEvents().stream().map(DtoMapper::toEventDTO).collect(Collectors.toList())
        );
    }

    @PostMapping(value = "/resource", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResourceDto> createResource(@RequestBody ResourceDto resourceDto) {
        Resource resource = new Resource();
        resource.setResourceType(resourceDto.getResourceType());
        resource.setDescription(resourceDto.getDescription());
        return ResponseEntity.ok(DtoMapper.toResourceDTO(resourceService.createResource(resource)));
    }

    @GetMapping(value = "/resources", produces = "application/json")
    public ResponseEntity<List<ResourceDto>> getAllResources() {
        return ResponseEntity.ok(
                resourceService.getAllResources().stream().map(DtoMapper::toResourceDTO).collect(Collectors.toList())
        );
    }

    @PostMapping(value = "/event/allocate-resources", produces = "application/json")
    public ResponseEntity<EventDto> allocateResources(@RequestParam Long eventId, @RequestParam Long resourceId) {
        return ResponseEntity.ok(DtoMapper.toEventDTO(eventService.allocateResource(eventId, resourceId)));
    }

    @GetMapping(value = "/registrations/count", produces = "application/json")
    public ResponseEntity<Long> getRegistrationCount() {
        return ResponseEntity.ok(registrationService.getRegistrationCount());
    }
}