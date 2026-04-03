package com.edutech.educationalresourcedistributionsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.edutech.educationalresourcedistributionsystem.entity.EventRegistration;
import java.util.List;
import java.util.Optional;

public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {

    List<EventRegistration> findByStudentId(String studentId);

    Optional<EventRegistration> findByStudentIdAndEvent_Id(String studentId, Long eventId);
}