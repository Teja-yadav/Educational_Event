package com.edutech.educationalresourcedistributionsystem.service;

import com.edutech.educationalresourcedistributionsystem.entity.User;

public interface UserService {
    User registerUser(User user);
    User login(String username, String password);
}