package com.edutech.educationalresourcedistributionsystem.dto;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EventDto {

    private Long id;
    private Long institutionId;
    private Long educatorId;

    @NotBlank(message = "Event name is required")
    @Size(min = 3, max = 100, message = "Event name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Event description is required")
    @Size(min = 5, max = 500, message = "Description must be between 5 and 500 characters")
    private String description;

    @NotBlank(message = "Materials are required")
    @Size(min = 2, max = 300, message = "Materials must be between 2 and 300 characters")
    private String materials;

    @NotNull(message = "Event date and time is required")
    private LocalDateTime eventDateTime;

    @NotBlank(message = "Venue is required")
    @Size(min = 2, max = 150, message = "Venue must be between 2 and 150 characters")
    private String venue;

    @Valid
    private List<ResourceDto> resourceAllocations;

    public EventDto() {
    }

    public EventDto(Long id, Long institutionId, Long educatorId, String name, String description, String materials,
                    LocalDateTime eventDateTime, String venue, List<ResourceDto> resourceAllocations) {
        this.id = id;
        this.institutionId = institutionId;
        this.educatorId = educatorId;
        this.name = name;
        this.description = description;
        this.materials = materials;
        this.eventDateTime = eventDateTime;
        this.venue = venue;
        this.resourceAllocations = resourceAllocations;
    }

    public Long getId() {
        return id;
    }

    public Long getInstitutionId() {
        return institutionId;
    }

    public Long getEducatorId() {
        return educatorId;
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

    public String getVenue() {
        return venue;
    }

    public List<ResourceDto> getResourceAllocations() {
        return resourceAllocations;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setInstitutionId(Long institutionId) {
        this.institutionId = institutionId;
    }

    public void setEducatorId(Long educatorId) {
        this.educatorId = educatorId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }

    public void setEventDateTime(LocalDateTime eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setResourceAllocations(List<ResourceDto> resourceAllocations) {
        this.resourceAllocations = resourceAllocations;
    }
}