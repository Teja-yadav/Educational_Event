package com.edutech.educationalresourcedistributionsystem.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edutech.educationalresourcedistributionsystem.entity.Resource;
import com.edutech.educationalresourcedistributionsystem.repository.ResourceRepository;
import java.util.List;
@Service
public class ResourceService {
   @Autowired
   private ResourceRepository resourceRepository;
   public Resource createResource(Resource resource) {
       return resourceRepository.save(resource);
   }
   public List<Resource> getAllResources() {
       return resourceRepository.findAll();
   }
}