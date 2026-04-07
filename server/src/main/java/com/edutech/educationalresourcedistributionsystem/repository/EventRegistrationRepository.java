package com.edutech.educationalresourcedistributionsystem.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.edutech.educationalresourcedistributionsystem.entity.EventRegistration;

public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {
    List<EventRegistration> findByStudentId(String studentId);
    Optional<EventRegistration> findByStudentIdAndEvent_Id(String studentId, Long eventId);
    void deleteByEvent_Id(Long eventId);
    boolean existsByEvent_Id(Long eventId);
    List<EventRegistration> findByEvent_InstitutionId(Long institutionId);
}
