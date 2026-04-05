package com.edutech.educationalresourcedistributionsystem.repository;

import com.edutech.educationalresourcedistributionsystem.entity.PasswordResetOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, Long> {
    Optional<PasswordResetOtp> findTopByEmailOrderByIdDesc(String email);
}
