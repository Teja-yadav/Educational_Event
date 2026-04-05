package com.edutech.educationalresourcedistributionsystem.controller;

import com.edutech.educationalresourcedistributionsystem.dto.ForgotPasswordRequest;
import com.edutech.educationalresourcedistributionsystem.dto.ResetPasswordRequest;
import com.edutech.educationalresourcedistributionsystem.dto.VerifyOtpRequest;
import com.edutech.educationalresourcedistributionsystem.service.PasswordResetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping(value = "/forgot-password", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest req) {
        try {
            passwordResetService.sendOtp(req.getEmail());
            return ResponseEntity.ok("OTP sent successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping(value = "/verify-otp", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyOtpRequest req) {
        try {
            passwordResetService.verifyOtp(req.getEmail(), req.getOtp());
            return ResponseEntity.ok("OTP verified");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping(value = "/reset-password", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest req) {
        try {
            passwordResetService.resetPassword(req.getEmail(), req.getOtp(), req.getNewPassword());
            return ResponseEntity.ok("Password reset successful");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}