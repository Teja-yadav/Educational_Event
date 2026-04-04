package com.edutech.educationalresourcedistributionsystem.service.impl;

import com.edutech.educationalresourcedistributionsystem.entity.User;
import com.edutech.educationalresourcedistributionsystem.repository.UserRepository;
import com.edutech.educationalresourcedistributionsystem.service.EmailService;
import com.edutech.educationalresourcedistributionsystem.service.UserService;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

        if(userRepository.existsByEmail(user.getEmail())){
            throw new IllegalArgumentException("User with same email already exists");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        User saved = userRepository.save(user);

        String to = null;
        try {
            to = saved.getEmail();
        } catch (Exception ignored) {}

        if (to != null && !to.isBlank()) {
            String subject = "Welcome to Educational Resource Distribution System";
            String body =
                    "Hi " + saved.getUsername() + ",\n\n" +
                    "Your account has been created successfully.\n" +
                    "Role: " + saved.getRole() + "\n\n" +
                    "You can now login and access your dashboard.\n\n" +
                    "Thanks,\nEduTech Team";
            emailService.sendSimpleMail(to, subject, body);
        } else if (adminEmail != null && !adminEmail.isBlank()) {
            String subject = "New User Registered: " + saved.getUsername();
            String body =
                    "A new user registered.\n\n" +
                    "Username: " + saved.getUsername() + "\n" +
                    "Role: " + saved.getRole() + "\n" +
                    "Note: User email not available in entity.\n";
            emailService.sendSimpleMail(adminEmail, subject, body);
        }

        return saved;
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}