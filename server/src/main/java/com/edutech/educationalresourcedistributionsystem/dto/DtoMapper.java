package com.edutech.educationalresourcedistributionsystem.dto;
import com.edutech.educationalresourcedistributionsystem.entity.*;
public class DtoMapper {
    public static EventDTO toEventDTO(Event event) {
        return new EventDTO(event.getId(),event.getName(),event.getDescription(),event.getMaterials());
    }
    public static ResourceDTO toResourceDTO(Resource resource) {
        return new ResourceDTO(resource.getId(),resource.getResourceType(),resource.getDescription());
    }
    public static UserDTO toUserDTO(User user) {
        return new UserDTO(user.getId(),user.getUsername(),user.getEmail(),user.getRole());
    }
    public static EventRegistrationDTO toRegistrationDTO(EventRegistration registraion) {
        return new EventRegistrationDTO(registraion.getId(),registraion.getStatus(),registraion.getStudentId(),registraion.getEvent().getId());
    }
}
