package com.edutech.educationalresourcedistributionsystem.controller;

import com.edutech.educationalresourcedistributionsystem.dto.DtoMapper;
import com.edutech.educationalresourcedistributionsystem.dto.EventDto;
import com.edutech.educationalresourcedistributionsystem.dto.ResourceDto;
import com.edutech.educationalresourcedistributionsystem.dto.UserDto;
import com.edutech.educationalresourcedistributionsystem.entity.Event;
import com.edutech.educationalresourcedistributionsystem.entity.EventRegistration;
import com.edutech.educationalresourcedistributionsystem.entity.Resource;
import com.edutech.educationalresourcedistributionsystem.entity.User;
import com.edutech.educationalresourcedistributionsystem.repository.UserRepository;
import com.edutech.educationalresourcedistributionsystem.service.EventService;
import com.edutech.educationalresourcedistributionsystem.service.RegistrationService;
import com.edutech.educationalresourcedistributionsystem.service.ResourceService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/institution")
public class InstitutionController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserRepository userRepository;

    private Long currentInstitutionId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth == null ? null : String.valueOf(auth.getPrincipal());
        if (username == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        if (!"INSTITUTION".equals(user.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }
        return user.getId();
    }

    @GetMapping("/educators")
    public ResponseEntity<List<UserDto>> getAllEducators() {
        log.info("Fetching all educators");
        return ResponseEntity.ok(
                userRepository.findByRoleOrderByUsernameAsc("EDUCATOR")
                        .stream()
                        .map(DtoMapper::toUserDTO)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping(value = "/event", consumes = "application/json", produces = "application/json")
    public ResponseEntity<EventDto> createEvent(@Valid @RequestBody EventDto eventDto) {

        log.info("Create event request received. eventName={}", eventDto.getName());

        Event event = new Event();
        event.setName(eventDto.getName());
        event.setDescription(eventDto.getDescription());
        event.setMaterials(eventDto.getMaterials());
        event.setEventDateTime(eventDto.getEventDateTime());
        event.setVenue(eventDto.getVenue());

        if (eventDto.getEducatorUsername() != null && !eventDto.getEducatorUsername().trim().isEmpty()) {
            User educator = userRepository.findByUsername(eventDto.getEducatorUsername().trim());
            if (educator == null || !"EDUCATOR".equals(educator.getRole())) {
                log.warn("Invalid educator username provided: {}", eventDto.getEducatorUsername());
                throw new RuntimeException("Invalid educator username");
            }
            event.setEducatorId(educator.getId());
        } else {
            event.setEducatorId(eventDto.getEducatorId());
        }

        Event saved = eventService.createEvent(event);
        log.info("Event created successfully. eventId={}", saved.getId());

        return ResponseEntity.ok(DtoMapper.toEventDTO(saved));
    }

    @DeleteMapping("/event/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long eventId) {
        log.warn("Delete event request received. eventId={}", eventId);
        eventService.deleteEvent(eventId);
        return ResponseEntity.ok("Event deleted successfully");
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventDto>> getAllEvents() {
        log.info("Fetching all events");
        return ResponseEntity.ok(
                eventService.getAllEvents()
                        .stream()
                        .map(DtoMapper::toEventDTO)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping(value = "/resource", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResourceDto> createResource(@Valid @RequestBody ResourceDto resourceDto) {
        log.info("Create resource request. resourceType={}", resourceDto.getResourceType());

        Resource resource = new Resource();
        resource.setResourceType(resourceDto.getResourceType());
        resource.setDescription(resourceDto.getDescription());

        return ResponseEntity.ok(DtoMapper.toResourceDTO(resourceService.createResource(resource)));
    }

    @GetMapping("/resources")
    public ResponseEntity<List<ResourceDto>> getAllResources() {
        log.info("Fetching all resources");
        return ResponseEntity.ok(
                resourceService.getAllResources()
                        .stream()
                        .map(DtoMapper::toResourceDTO)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping(value = "/event/allocate-resources", produces = "application/json")
    public ResponseEntity<EventDto> allocateResources(@RequestParam Long eventId, @RequestParam Long resourceId) {
        log.info("Allocate resource request. eventId={}, resourceId={}", eventId, resourceId);
        return ResponseEntity.ok(DtoMapper.toEventDTO(eventService.allocateResource(eventId, resourceId)));
    }

    @GetMapping("/registrations/count")
    public ResponseEntity<Long> getRegistrationCount() {
        log.info("Fetching registration count");
        return ResponseEntity.ok(registrationService.getRegistrationCount());
    }

    @GetMapping(value = "/registrations", produces = "application/json")
    public ResponseEntity<List<EventRegistration>> getAllRegistrationsForInstitution() {
        Long institutionId = currentInstitutionId();
        log.info("Fetching all registrations for institution. institutionId={}", institutionId);
        return ResponseEntity.ok(registrationService.getInstitutionRegistrations(institutionId));
    }
}