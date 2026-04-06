package com.edutech.educationalresourcedistributionsystem.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.edutech.educationalresourcedistributionsystem.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRoleOrderByUsernameAsc(String role);
}