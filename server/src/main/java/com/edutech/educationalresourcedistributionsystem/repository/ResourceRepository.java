package com.edutech.educationalresourcedistributionsystem.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.edutech.educationalresourcedistributionsystem.entity.Resource;
public interface ResourceRepository extends JpaRepository<Resource,Long>{}