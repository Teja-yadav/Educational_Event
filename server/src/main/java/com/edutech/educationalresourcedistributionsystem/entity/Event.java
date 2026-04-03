package com.edutech.educationalresourcedistributionsystem.entity;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "event_date_time", nullable = false)
    private LocalDateTime eventDateTime;
    @Column(name = "materials")
    private String materials;
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Resource> resourceAllocations;

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

    public List<Resource> getResourceAllocations() {
        return resourceAllocations;
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

    public void setResourceAllocations(List<Resource> resourceAllocations) {
        this.resourceAllocations = resourceAllocations;
    }
    public LocalDateTime getEventDateTime() {
    return eventDateTime;
}
public void setEventDateTime(LocalDateTime eventDateTime) {
    this.eventDateTime = eventDateTime;
}
}
