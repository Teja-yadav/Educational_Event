package com.edutech.educationalresourcedistributionsystem.service;

import com.edutech.educationalresourcedistributionsystem.entity.Resource;
import java.util.List;

public interface ResourceService {
    Resource createResource(Resource resource);
    List<Resource> getAllResources();
}