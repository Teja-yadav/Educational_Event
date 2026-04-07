package com.edutech.educationalresourcedistributionsystem.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class EventRegistrationDto {

    private Long id;

    @NotBlank(message = "Status is required")
    @Size(min = 3, max = 30, message = "Status must be between 3 and 30 characters")
    private String status;

    @NotBlank(message = "Student ID is required")
    @Size(min = 3, max = 20, message = "Student ID must be between 3 and 20 characters")
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "Student ID can contain only letters, numbers, underscore and hyphen")
    private String studentId;
    
    private Long eventId;

    public EventRegistrationDto() {
    }

    public EventRegistrationDto(Long id, String status, String studentId, Long eventId) {
        this.id = id;
        this.status = status;
        this.studentId = studentId;
        this.eventId = eventId;
    }

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getStudentId() {
        return studentId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }
}
