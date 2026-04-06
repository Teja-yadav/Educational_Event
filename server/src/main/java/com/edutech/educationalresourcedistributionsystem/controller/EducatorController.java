package com.edutech.educationalresourcedistributionsystem.controller;

import com.edutech.educationalresourcedistributionsystem.dto.DtoMapper;
import com.edutech.educationalresourcedistributionsystem.dto.EventDto;
import com.edutech.educationalresourcedistributionsystem.entity.Event;
import com.edutech.educationalresourcedistributionsystem.service.EventService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/educator")
public class EducatorController {

    @Autowired
    private EventService eventService;

    @GetMapping(value = "/agenda", produces = "application/json")
    public List<EventDto> getAgenda() {
        log.info("Educator agenda request received");
        return eventService.getAllEvents()
                .stream()
                .map(DtoMapper::toEventDTO)
                .collect(Collectors.toList());
    }

    @PutMapping(value = "/update-material/{eventId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<EventDto> updateEvent(
            @PathVariable Long eventId,
            @Valid @RequestBody EventDto eventDto
    ) {
        log.info("Educator update material request. eventId={}", eventId);

        Event updated = new Event();
        updated.setMaterials(eventDto.getMaterials());

        Event saved = eventService.updateEvent(eventId, updated);

        log.info("Educator updated materials successfully. eventId={}", eventId);
        return ResponseEntity.ok(DtoMapper.toEventDTO(saved));
    }
}
