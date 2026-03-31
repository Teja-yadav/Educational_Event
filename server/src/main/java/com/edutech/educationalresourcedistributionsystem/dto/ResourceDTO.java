package com.edutech.educationalresourcedistributionsystem.dto;
public class ResourceDTO {
    private Long id;
    private String resourceType;
    private String description;
    public ResourceDTO() {}
    public ResourceDTO(Long id, String resourceType, String description) {
        this.id = id;
        this.resourceType = resourceType;
        this.description = description;
    }
    public Long getId() { return id; }
    public String getResourceType() { return resourceType; }
    public String getDescription() { return description; }
}