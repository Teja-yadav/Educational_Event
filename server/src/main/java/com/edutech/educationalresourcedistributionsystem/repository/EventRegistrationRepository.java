package com.edutech.educationalresourcedistributionsystem.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.edutech.educationalresourcedistributionsystem.entity.EventRegistration;
import java.util.List;
public interface EventRegistrationRepository extends JpaRepository<EventRegistration,Long> {
  List<EventRegistration> findByStudentId(Long studentId);
}
 