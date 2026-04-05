package com.edutech.educationalresourcedistributionsystem.controller;
import com.edutech.educationalresourcedistributionsystem.dto.*;
import com.edutech.educationalresourcedistributionsystem.entity.Event;
import com.edutech.educationalresourcedistributionsystem.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/educator")
public class EducatorController {
    @Autowired
    private EventService eventService;
    @GetMapping(value = "/agenda", produces = "application/json")
    public List<EventDto> getAgenda() {
        return eventService.getAllEvents()
                .stream()
                .map(DtoMapper::toEventDTO)
                .collect(Collectors.toList()); 
    }
@PutMapping(value = "/update-material/{eventId}", consumes = "application/json", produces = "application/json")
public ResponseEntity<EventDto> updateEvent(@PathVariable Long eventId, @RequestBody EventDto eventDto) {

    Event updated = new Event();
    updated.setName(eventDto.getName());
    updated.setDescription(eventDto.getDescription());
    updated.setMaterials(eventDto.getMaterials());

    // ✅ ADD THESE TWO LINES
    updated.setEventDateTime(eventDto.getEventDateTime());
    updated.setVenue(eventDto.getVenue());

    Event saved = eventService.updateEvent(eventId, updated);

    return ResponseEntity.ok(DtoMapper.toEventDTO(saved));
}
}