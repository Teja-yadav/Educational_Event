package com.edutech.educationalresourcedistributionsystem.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.edutech.educationalresourcedistributionsystem.entity.User;
public interface UserRepository extends JpaRepository<User,Long>  {   
   User findByUsername(String username);
}
 