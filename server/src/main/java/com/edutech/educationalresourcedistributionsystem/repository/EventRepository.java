package com.edutech.educationalresourcedistributionsystem.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.edutech.educationalresourcedistributionsystem.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByInstitutionId(Long institutionId);

    List<Event> findByEducatorId(Long educatorId);

    Optional<Event> findByIdAndInstitutionId(Long id, Long institutionId);

    Optional<Event> findByIdAndEducatorId(Long id, Long educatorId);
}