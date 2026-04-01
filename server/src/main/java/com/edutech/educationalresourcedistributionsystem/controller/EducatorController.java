package com.edutech.educationalresourcedistributionsystem.controller;

import com.edutech.educationalresourcedistributionsystem.dto.*;

import com.edutech.educationalresourcedistributionsystem.entity.Event;

import com.edutech.educationalresourcedistributionsystem.service.EventService;

import org.springframework.beans.factory.annotation.Autowired;

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

    @PutMapping(value = "/update-material/{eventId}",consumes = "application/json",produces = "application/json")

    public EventDto updateMaterial(@PathVariable Long eventId, @RequestBody EventDto eventDto) {

        Event event = new Event();

        event.setName(eventDto.getName());

        event.setDescription(eventDto.getDescription());

        event.setMaterials(eventDto.getMaterials());

        return DtoMapper.toEventDTO(eventService.updateEvent(eventId, event));

    }

}
 