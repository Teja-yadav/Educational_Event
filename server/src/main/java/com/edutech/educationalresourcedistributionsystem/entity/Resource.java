package com.edutech.educationalresourcedistributionsystem.entity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name = "resource_allocation")
public class Resource {
   @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   private String resourceType;
   private String description;
   @ManyToOne
   @JsonIgnore
   private Event event;
   public Resource() {}
   public Long getId() { return id; }
   public void setId(Long id) { this.id = id; }
   public String getResourceType() { return resourceType; }
   public void setResourceType(String resourceType) { this.resourceType = resourceType; }
   public String getDescription() { return description; }
   public void setDescription(String description) { this.description = description; }
   public Event getEvent() { return event; }
   public void setEvent(Event event) { this.event = event; }
}