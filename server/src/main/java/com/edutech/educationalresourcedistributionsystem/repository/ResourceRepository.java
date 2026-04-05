package com.edutech.educationalresourcedistributionsystem.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.edutech.educationalresourcedistributionsystem.entity.Resource;

public interface ResourceRepository extends JpaRepository<Resource,Long>{
    List<Resource> findByEvent_Id(Long eventId);
}