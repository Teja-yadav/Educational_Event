package com.edutech.educationalresourcedistributionsystem.dto;

import java.time.LocalDateTime;
import java.util.List;

public class EventDto {
    private Long id;
    private String name;
    private String description;
    private String materials;
    private LocalDateTime eventDateTime;

    private List<ResourceDto> resourceAllocations;

    public EventDto(Long id, String name, String description, String materials, LocalDateTime eventDateTime,
            List<ResourceDto> resourceAllocations) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.materials = materials;
        this.eventDateTime = eventDateTime;
        this.resourceAllocations = resourceAllocations;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getMaterials() {
        return materials;
    }

    public LocalDateTime getEventDateTime() {
        return eventDateTime;
    }

    public List<ResourceDto> getResourceAllocations() {
        return resourceAllocations;
    }
}