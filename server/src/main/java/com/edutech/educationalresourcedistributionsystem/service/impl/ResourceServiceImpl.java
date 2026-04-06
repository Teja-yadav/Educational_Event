package com.edutech.educationalresourcedistributionsystem.service.impl;

import com.edutech.educationalresourcedistributionsystem.entity.Resource;
import com.edutech.educationalresourcedistributionsystem.entity.User;
import com.edutech.educationalresourcedistributionsystem.repository.ResourceRepository;
import com.edutech.educationalresourcedistributionsystem.repository.UserRepository;
import com.edutech.educationalresourcedistributionsystem.service.EmailService;
import com.edutech.educationalresourcedistributionsystem.service.ResourceService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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
            log.warn("Unauthorized access: missing principal");
            throw new RuntimeException("Unauthorized");
        }

        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.warn("Unauthorized access: user not found. username={}", username);
            throw new RuntimeException("Unauthorized");
        }

        return user;
    }

    @Override
    public Resource createResource(Resource resource) {

        User institution = currentUser();
        log.info("Create resource requested. userId={}, role={}, resourceType={}",
                institution.getId(), institution.getRole(), resource.getResourceType());

        if (!"INSTITUTION".equals(institution.getRole())) {
            log.warn("Forbidden createResource. userId={}, role={}",
                    institution.getId(), institution.getRole());
            throw new RuntimeException("Forbidden");
        }

        resource.setInstitutionId(institution.getId());
        Resource saved = resourceRepository.save(resource);

        log.info("Resource created successfully. resourceId={}, institutionId={}",
                saved.getId(), institution.getId());

        return saved;
    }

    @Override
    public List<Resource> getAllResources() {

        User institution = currentUser();
        log.info("Fetch resources requested. userId={}, role={}",
                institution.getId(), institution.getRole());

        if (!"INSTITUTION".equals(institution.getRole())) {
            log.warn("Forbidden getAllResources. userId={}, role={}",
                    institution.getId(), institution.getRole());
            throw new RuntimeException("Forbidden");
        }

        List<Resource> list = resourceRepository.findByInstitutionId(institution.getId());
        log.info("Resources fetched successfully. institutionId={}, count={}",
                institution.getId(), list.size());

        return list;
    }
}
