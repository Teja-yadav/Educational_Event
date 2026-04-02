package com.edutech.educationalresourcedistributionsystem.dto;

import java.util.List;

import com.edutech.educationalresourcedistributionsystem.entity.Event;
import com.edutech.educationalresourcedistributionsystem.entity.Resource;

public class EventDto {
    private Long id;
    private String name;
    private String description;
    private String materials;
    private List<Resource> resources;

    public EventDto() {
    }

    public EventDto(Long id, String name, String description, String materials, List<Resource> resources) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.materials = materials;
        this.resources = resources;
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

    public void setId(Long id) {
        this.id = id;
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

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

}