package com.edutech.educationalresourcedistributionsystem.entity;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
@Entity
public class Event {
   @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   private String name;
   private String description;
   private String materials;
  @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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
}

 
 