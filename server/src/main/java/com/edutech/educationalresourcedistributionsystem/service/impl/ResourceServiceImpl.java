package com.edutech.educationalresourcedistributionsystem.service.impl;

import com.edutech.educationalresourcedistributionsystem.entity.Resource;
import com.edutech.educationalresourcedistributionsystem.entity.User;
import com.edutech.educationalresourcedistributionsystem.repository.ResourceRepository;
import com.edutech.educationalresourcedistributionsystem.repository.UserRepository;
import com.edutech.educationalresourcedistributionsystem.service.EmailService;
import com.edutech.educationalresourcedistributionsystem.service.ResourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Value("${app.mail.admin:}")
    private String adminEmail;

    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth == null ? null : String.valueOf(auth.getPrincipal());
        if (username == null) {
            throw new RuntimeException("Unauthorized");
        }
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("Unauthorized");
        }
        return user;
    }

    @Override
    public Resource createResource(Resource resource) {
        User institution = currentUser();

        if (!"INSTITUTION".equals(institution.getRole())) {
            throw new RuntimeException("Forbidden");
        }

        resource.setInstitutionId(institution.getId());
        return resourceRepository.save(resource);
    }

    @Override
    public List<Resource> getAllResources() {
        User institution = currentUser();

        if (!"INSTITUTION".equals(institution.getRole())) {
            throw new RuntimeException("Forbidden");
        }

        return resourceRepository.findByInstitutionId(institution.getId());
    }
}