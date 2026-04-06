package com.edutech.educationalresourcedistributionsystem.service.impl;

import com.edutech.educationalresourcedistributionsystem.entity.User;
import com.edutech.educationalresourcedistributionsystem.repository.UserRepository;
import com.edutech.educationalresourcedistributionsystem.service.EmailService;
import com.edutech.educationalresourcedistributionsystem.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Value("${app.mail.admin:}")
    private String adminEmail;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public User registerUser(User user) {

        log.info("User registration started. username={}, email={}",
                user.getUsername(), user.getEmail());

        if (userRepository.existsByEmail(user.getEmail())) {
            log.warn("Registration blocked: email already exists. email={}", user.getEmail());
            throw new IllegalArgumentException("User with same email already exists");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        User saved = userRepository.save(user);

        log.info("User saved successfully. userId={}, username={}, role={}",
                saved.getId(), saved.getUsername(), saved.getRole());

        String to = null;
        try {
            to = saved.getEmail();
        } catch (Exception e) {
            log.warn("Failed to read user email after registration. userId={}", saved.getId(), e);
        }

        if (to != null && !to.isBlank()) {
            String subject = "Welcome to Educational Resource Distribution System";
            String body =
                    "Hi " + saved.getUsername() + ",\n\n" +
                    "Your account has been created successfully.\n" +
                    "Role: " + saved.getRole() + "\n\n" +
                    "You can now login and access your dashboard.\n\n" +
                    "Thanks,\nEduTech Team";
            try {
                emailService.sendSimpleMail(to, subject, body);
                log.info("Welcome email sent to user. email={}", to);
            } catch (Exception e) {
                log.error("Failed to send welcome email to user. email={}", to, e);
            }
        } else if (adminEmail != null && !adminEmail.isBlank()) {
            String subject = "New User Registered: " + saved.getUsername();
            String body =
                    "A new user registered.\n\n" +
                    "Username: " + saved.getUsername() + "\n" +
                    "Role: " + saved.getRole() + "\n" +
                    "Note: User email not available in entity.\n";
            try {
                emailService.sendSimpleMail(adminEmail, subject, body);
                log.info("Admin notified about new user. adminEmail={}", adminEmail);
            } catch (Exception e) {
                log.error("Failed to notify admin about new user. adminEmail={}", adminEmail, e);
            }
        }

        return saved;
    }

    @Override
    public User getUserByUsername(String username) {
        log.info("Fetching user by username={}", username);
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("Loading user for authentication. username={}", username);

        User user = userRepository.findByUsername(username);

        if (user == null) {
            log.warn("Authentication failed: user not found. username={}", username);
            throw new UsernameNotFoundException("User not found");
        }

        log.info("User loaded for authentication. username={}, role={}",
                user.getUsername(), user.getRole());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}