package com.edutech.educationalresourcedistributionsystem.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.edutech.educationalresourcedistributionsystem.entity.Event;
public interface EventRepository extends JpaRepository<Event,Long>  {}