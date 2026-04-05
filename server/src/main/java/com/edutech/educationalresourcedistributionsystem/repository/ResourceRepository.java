package com.edutech.educationalresourcedistributionsystem.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.edutech.educationalresourcedistributionsystem.entity.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

    List<Resource> findByEvent_Id(Long eventId);

    List<Resource> findByInstitutionId(Long institutionId);

    Optional<Resource> findByIdAndInstitutionId(Long id, Long institutionId);
}