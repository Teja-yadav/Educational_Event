package com.edutech.educationalresourcedistributionsystem.dto;
public class EventDTO {
    private Long id;
    private String name;
    private String description;
    private String materials;
    public EventDTO() {}
    public EventDTO(Long id, String name, String description, String materials) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.materials = materials;
    }
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getMaterials() { return materials; }
}