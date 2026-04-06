package com.edutech.educationalresourcedistributionsystem.service.impl;

import com.edutech.educationalresourcedistributionsystem.entity.PasswordResetOtp;
import com.edutech.educationalresourcedistributionsystem.entity.User;
import com.edutech.educationalresourcedistributionsystem.repository.PasswordResetOtpRepository;
import com.edutech.educationalresourcedistributionsystem.repository.UserRepository;
import com.edutech.educationalresourcedistributionsystem.service.EmailService;
import com.edutech.educationalresourcedistributionsystem.service.PasswordResetService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Slf4j
@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetOtpRepository otpRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final SecureRandom random = new SecureRandom();

    @Override
    public void sendOtp(String email) {

        log.info("Password reset OTP request received. email={}", email);

        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.warn("OTP request blocked: email not registered. email={}", email);
            throw new RuntimeException("Email not registered");
        }

        String otp = String.format("%06d", random.nextInt(1000000));

        PasswordResetOtp entity = new PasswordResetOtp();
        entity.setEmail(email);
        entity.setOtpHash(passwordEncoder.encode(otp));
        entity.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        entity.setVerified(false);
        entity.setUsed(false);

        otpRepository.save(entity);
        log.info("OTP entry created successfully. email={}, expiresAt={}", email, entity.getExpiresAt());

        String subject = "OTP for Password Reset";
        String body =
                "Hi " + user.getUsername() + ",\n\n" +
                "Your OTP for password reset is: " + otp + "\n\n" +
                "This OTP will expire in 10 minutes.\n\n" +
                "Thanks,\nEduTech Team";

        try {
            emailService.sendSimpleMail(email, subject, body);
            log.info("OTP email sent successfully. email={}", email);
        } catch (Exception e) {
            log.error("Failed to send OTP email. email={}", email, e);
            throw e;
        }
    }

    @Override
    public void verifyOtp(String email, String otp) {

        log.info("OTP verification request received. email={}", email);

        PasswordResetOtp latest = otpRepository.findTopByEmailOrderByIdDesc(email)
                .orElseThrow(() -> {
                    log.warn("OTP verification blocked: OTP not requested. email={}", email);
                    return new RuntimeException("OTP not requested");
                });

        if (latest.isUsed()) {
            log.warn("OTP verification blocked: OTP already used. email={}", email);
            throw new RuntimeException("OTP already used");
        }

        if (latest.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.warn("OTP verification blocked: OTP expired. email={}, expiresAt={}", email, latest.getExpiresAt());
            throw new RuntimeException("OTP expired");
        }

        if (!passwordEncoder.matches(otp, latest.getOtpHash())) {
            log.warn("OTP verification blocked: invalid OTP. email={}", email);
            throw new RuntimeException("Invalid OTP");
        }

        latest.setVerified(true);
        otpRepository.save(latest);

        log.info("OTP verified successfully. email={}", email);
    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {

        log.info("Password reset request received. email={}", email);

        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.warn("Password reset blocked: email not registered. email={}", email);
            throw new RuntimeException("Email not registered");
        }

        PasswordResetOtp latest = otpRepository.findTopByEmailOrderByIdDesc(email)
                .orElseThrow(() -> {
                    log.warn("Password reset blocked: OTP not requested. email={}", email);
                    return new RuntimeException("OTP not requested");
                });

        if (latest.isUsed()) {
            log.warn("Password reset blocked: OTP already used. email={}", email);
            throw new RuntimeException("OTP already used");
        }

        if (latest.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.warn("Password reset blocked: OTP expired. email={}, expiresAt={}", email, latest.getExpiresAt());
            throw new RuntimeException("OTP expired");
        }

        if (!passwordEncoder.matches(otp, latest.getOtpHash())) {
            log.warn("Password reset blocked: invalid OTP. email={}", email);
            throw new RuntimeException("Invalid OTP");
        }

        if (!latest.isVerified()) {
            log.warn("Password reset blocked: OTP not verified. email={}", email);
            throw new RuntimeException("OTP not verified");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        latest.setUsed(true);
        otpRepository.save(latest);

        log.info("Password reset successful. email={}, userId={}", email, user.getId());

        String subject = "Password Reset Successful";
        String body =
                "Hi " + user.getUsername() + ",\n\n" +
                "Your password has been reset successfully.\n\n" +
                "If you did not perform this action, please contact support immediately.\n\n" +
                "Thanks,\nEduTech Team";

        try {
            emailService.sendSimpleMail(email, subject, body);
            log.info("Password reset confirmation email sent. email={}", email);
        } catch (Exception e) {
            log.error("Failed to send password reset confirmation email. email={}", email, e);
            throw e;
        }
    }
}