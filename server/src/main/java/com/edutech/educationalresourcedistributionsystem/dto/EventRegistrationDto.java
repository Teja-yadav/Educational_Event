package com.edutech.educationalresourcedistributionsystem.dto;
public class EventRegistrationDto {
    private Long id;
    private String status;
    private String studentId;
    private Long eventId;
    public EventRegistrationDto() {}
    public EventRegistrationDto(Long id, String status, String studentId, Long eventId) {
        this.id = id;
        this.status = status;
        this.studentId = studentId;
        this.eventId = eventId;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStudentId() {
        return studentId;
    }
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    public Long getEventId() {
        return eventId;
    }
    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }
}