package com.edutech.educationalresourcedistributionsystem.dto;

public class ResourceDto {

    private Long id;
    private Long institutionId;
    private String resourceType;
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