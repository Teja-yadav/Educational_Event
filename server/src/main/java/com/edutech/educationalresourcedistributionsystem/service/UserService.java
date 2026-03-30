package com.edutech.educationalresourcedistributionsystem.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.edutech.educationalresourcedistributionsystem.entity.User;
import com.edutech.educationalresourcedistributionsystem.repository.UserRepository;
@Service
public class UserService {
   @Autowired
   private UserRepository userRepository;
   private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
   public User registerUser(User user) {
      user.setPassword(encoder.encode(user.getPassword()));
       return userRepository.save(user);
   }
   public User login(String username, String password) {
       User user = userRepository.findByUsername(username);
       if (user != null && encoder.matches(password, user.getPassword())) {
           return user;
       }
       return null;
   }
}