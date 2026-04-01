package com.edutech.educationalresourcedistributionsystem.dto;

public class ResourceDto {

    private Long id;

    private String resourceType;

    private String description;

    public ResourceDto() {}

    public ResourceDto(Long id, String resourceType, String description) {

        this.id = id;

        this.resourceType = resourceType;

        this.description = description;

    }

    public Long getId() { return id; }

    public String getResourceType() { return resourceType; }

    public String getDescription() { return description; }

}
 