package com.edutech.educationalresourcedistributionsystem.dto;

import com.edutech.educationalresourcedistributionsystem.entity.*;

public class DtoMapper {
    public static EventDto toEventDTO(Event event) {
        return new EventDto(event.getId(), event.getName(), event.getDescription(), event.getMaterials(),
                event.getResourceAllocations());
    }

    public static ResourceDto toResourceDTO(Resource resource) {
        return new ResourceDto(resource.getId(), resource.getResourceType(), resource.getDescription());
    }

    public static UserDto toUserDTO(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }

    public static EventRegistrationDto toRegistrationDTO(EventRegistration registration) {
        return new EventRegistrationDto(registration.getId(), registration.getStatus(), registration.getStudentId(),
                registration.getEvent().getId());
    }

    public static Event toEventEntity(EventDto dto) {
        Event event = new Event();
        event.setId(dto.getId());
        event.setName(dto.getName());
        event.setDescription(dto.getDescription());
        event.setMaterials(dto.getMaterials());
        return event;
    }

    public static Resource toResourceEntity(ResourceDto dto) {
        Resource resource = new Resource();
        resource.setId(dto.getId());
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