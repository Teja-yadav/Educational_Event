package com.edutech.educationalresourcedistributionsystem.controller;

import com.edutech.educationalresourcedistributionsystem.dto.LoginRequest;
import com.edutech.educationalresourcedistributionsystem.dto.LoginResponse;
import com.edutech.educationalresourcedistributionsystem.entity.User;
import com.edutech.educationalresourcedistributionsystem.jwt.JwtUtil;
import com.edutech.educationalresourcedistributionsystem.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class RegisterAndLoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        log.info("User registration request. username={}", user.getUsername());
        User saved = userService.registerUser(user);
        log.info("User registered successfully. username={}", saved.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login attempt. username={}", loginRequest.getUsername());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            User user = userService.getUserByUsername(loginRequest.getUsername());
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

            log.info("Login successful. username={}, role={}", user.getUsername(), user.getRole());
            return ResponseEntity.ok(new LoginResponse(token, user.getUsername(), user.getRole()));

        } catch (AuthenticationException e) {
            log.error("Login failed. username={}", loginRequest.getUsername(), e);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
    }
}