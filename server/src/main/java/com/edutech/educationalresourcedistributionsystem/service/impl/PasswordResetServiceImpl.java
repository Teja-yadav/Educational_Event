package com.edutech.educationalresourcedistributionsystem.service.impl;

import com.edutech.educationalresourcedistributionsystem.entity.PasswordResetOtp;
import com.edutech.educationalresourcedistributionsystem.entity.User;
import com.edutech.educationalresourcedistributionsystem.repository.PasswordResetOtpRepository;
import com.edutech.educationalresourcedistributionsystem.repository.UserRepository;
import com.edutech.educationalresourcedistributionsystem.service.EmailService;
import com.edutech.educationalresourcedistributionsystem.service.PasswordResetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

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
        User user = userRepository.findByEmail(email);
        if (user == null) {
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

        String subject = "OTP for Password Reset";
        String body =
                "Hi " + user.getUsername() + ",\n\n" +
                "Your OTP for password reset is: " + otp + "\n\n" +
                "This OTP will expire in 10 minutes.\n\n" +
                "Thanks,\nEduTech Team";

        emailService.sendSimpleMail(email, subject, body);
    }

    @Override
    public void verifyOtp(String email, String otp) {
        PasswordResetOtp latest = otpRepository.findTopByEmailOrderByIdDesc(email)
                .orElseThrow(() -> new RuntimeException("OTP not requested"));

        if (latest.isUsed()) throw new RuntimeException("OTP already used");
        if (latest.getExpiresAt().isBefore(LocalDateTime.now())) throw new RuntimeException("OTP expired");
        if (!passwordEncoder.matches(otp, latest.getOtpHash())) throw new RuntimeException("Invalid OTP");

        latest.setVerified(true);
        otpRepository.save(latest);
    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
        User user = userRepository.findByEmail(email);
        if (user == null) throw new RuntimeException("Email not registered");

        PasswordResetOtp latest = otpRepository.findTopByEmailOrderByIdDesc(email)
                .orElseThrow(() -> new RuntimeException("OTP not requested"));

        if (latest.isUsed()) throw new RuntimeException("OTP already used");
        if (latest.getExpiresAt().isBefore(LocalDateTime.now())) throw new RuntimeException("OTP expired");
        if (!passwordEncoder.matches(otp, latest.getOtpHash())) throw new RuntimeException("Invalid OTP");
        if (!latest.isVerified()) throw new RuntimeException("OTP not verified");

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        latest.setUsed(true);
        otpRepository.save(latest);

        String subject = "Password Reset Successful";
        String body =
                "Hi " + user.getUsername() + ",\n\n" +
                "Your password has been reset successfully.\n\n" +
                "If you did not perform this action, please contact support immediately.\n\n" +
                "Thanks,\nEduTech Team";

        emailService.sendSimpleMail(email, subject, body);
    }
}