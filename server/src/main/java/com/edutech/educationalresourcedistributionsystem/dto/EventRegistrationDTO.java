package com.edutech.educationalresourcedistributionsystem.dto;
public class EventRegistrationDTO {
    private Long id;
    private String status;
    private Long studentId;
    private Long eventId;
    public EventRegistrationDTO() {}
    public EventRegistrationDTO(Long id, String status, Long studentId, Long eventId) {
        this.id = id;
        this.status = status;
        this.studentId = studentId;
        this.eventId = eventId;
    }
    public Long getId() { return id; }
    public String getStatus() { return status; }
    public Long getStudentId() { return studentId; }
    public Long getEventId() { return eventId; }
}
