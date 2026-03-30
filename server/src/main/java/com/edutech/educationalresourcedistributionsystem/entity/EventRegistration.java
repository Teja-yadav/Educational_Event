package com.edutech.educationalresourcedistributionsystem.entity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Entity
public class EventRegistration {
   @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   private Long studentId;
   private String status;
   @ManyToOne
   @JoinColumn(name = "event_id")
   private Event event;
   public Long getId() {
       return id;
   }
   public Long getStudentId() {
       return studentId;
   }
   public void setStudentId(Long studentId) {
       this.studentId = studentId;
   }
   public String getStatus() {
       return status;
   }
   public void setStatus(String status) {
       this.status = status;
   }
   public Event getEvent() {
       return event;
   }
   public void setEvent(Event event) {
       this.event = event;
   }
}