package com.edutech.educationalresourcedistributionsystem.service.impl;

import com.edutech.educationalresourcedistributionsystem.entity.Resource;
import com.edutech.educationalresourcedistributionsystem.repository.ResourceRepository;
import com.edutech.educationalresourcedistributionsystem.service.EmailService;
import com.edutech.educationalresourcedistributionsystem.service.ResourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private EmailService emailService;

    @Value("${app.mail.admin:}")
    private String adminEmail;

    @Override
    public Resource createResource(Resource resource) {
        Resource saved = resourceRepository.save(resource);

        return saved;
    }

    @Override
    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }
}