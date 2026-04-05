package com.edutech.educationalresourcedistributionsystem.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "resource_allocation")
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "institution_id")
    private Long institutionId;

    @Column(name = "resource_type", nullable = false)
    private String resourceType;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "event_id")
    private Event event;

    public Resource() {
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

    public Event getEvent() {
        return event;
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

    public void setEvent(Event event) {
        this.event = event;
    }
}
