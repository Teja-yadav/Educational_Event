package com.edutech.educationalresourcedistributionsystem.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ResourceDto {

    private Long id;

    @NotNull(message = "Institution ID is required")
    private Long institutionId;

    @NotBlank(message = "Resource type is required")
    @Size(min = 2, max = 60, message = "Resource type must be between 2 and 60 characters")
    private String resourceType;

    @NotBlank(message = "Description is required")
    @Size(min = 3, max = 300, message = "Description must be between 3 and 300 characters")
    private String description;

    public ResourceDto() {
    }

    public ResourceDto(Long id, Long institutionId, String resourceType, String description) {
        this.id = id;
        this.institutionId = institutionId;
        this.resourceType = resourceType;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public Long getInstitutionId() {
        return institutionId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setInstitutionId(Long institutionId) {
        this.institutionId = institutionId;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}