package com.edutech.educationalresourcedistributionsystem.dto;

import com.edutech.educationalresourcedistributionsystem.entity.Event;
import com.edutech.educationalresourcedistributionsystem.entity.EventRegistration;
import com.edutech.educationalresourcedistributionsystem.entity.Resource;
import com.edutech.educationalresourcedistributionsystem.entity.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DtoMapper {

    public static EventDto toEventDTO(Event event) {
        List<ResourceDto> resources = event.getResourceAllocations() == null
                ? Collections.emptyList()
                : event.getResourceAllocations().stream().map(DtoMapper::toResourceDTO).collect(Collectors.toList());

        return new EventDto(
                event.getId(),
                event.getInstitutionId(),
                event.getEducatorId(),
                event.getName(),
                event.getDescription(),
                event.getMaterials(),
                event.getEventDateTime(),
                event.getVenue(),
                resources
        );
    }

    public static ResourceDto toResourceDTO(Resource resource) {
        return new ResourceDto(
                resource.getId(),
                resource.getInstitutionId(),
                resource.getResourceType(),
                resource.getDescription()
        );
    }

    public static UserDto toUserDTO(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }

    public static EventRegistrationDto toRegistrationDTO(EventRegistration registration) {
        return new EventRegistrationDto(
                registration.getId(),
                registration.getStatus(),
                registration.getStudentId(),
                registration.getEvent().getId()
        );
    }

    public static Event toEventEntity(EventDto dto) {
        Event event = new Event();
        event.setId(dto.getId());
        event.setInstitutionId(dto.getInstitutionId());
        event.setEducatorId(dto.getEducatorId());
        event.setName(dto.getName());
        event.setDescription(dto.getDescription());
        event.setMaterials(dto.getMaterials());
        event.setEventDateTime(dto.getEventDateTime());
        event.setVenue(dto.getVenue());
        return event;
    }

    public static Resource toResourceEntity(ResourceDto dto) {
        Resource resource = new Resource();
        resource.setId(dto.getId());
        resource.setInstitutionId(dto.getInstitutionId());
        resource.setResourceType(dto.getResourceType());
        resource.setDescription(dto.getDescription());
        return resource;
    }

    public static User toUserEntity(UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        return user;
    }

    public static EventRegistration toRegistrationEntity(EventRegistrationDto dto, Event event) {
        EventRegistration registration = new EventRegistration();
        registration.setId(dto.getId());
        registration.setStatus(dto.getStatus());
        registration.setStudentId(dto.getStudentId());
        registration.setEvent(event);
        return registration;
    }
}