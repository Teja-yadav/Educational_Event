// package com.edutech.educationalresourcedistributionsystem.controller;
// import com.edutech.educationalresourcedistributionsystem.dto.LoginRequest;
// import com.edutech.educationalresourcedistributionsystem.entity.User;
// import com.edutech.educationalresourcedistributionsystem.jwt.JwtUtil;
// import com.edutech.educationalresourcedistributionsystem.service.UserService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import java.util.HashMap;
// import java.util.Map;
// @RestController
// @RequestMapping("/api/user")
// public class RegisterAndLoginController {
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private JwtUtil jwtUtil;
//    @PostMapping(value = "/login",consumes = "application/json",produces = "application/json")
//    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
//        User user = userService.login(request.getUsername(),request.getPassword());
//        if (user == null) {
//            return ResponseEntity.status(401).build();
//        }
//        String token = jwtUtil.generateToken(user.getUsername(),user.getRole());
//        Map<String, Object> response = new HashMap<>();
//       response.put("token", token);
//       response.put("username", user.getUsername());
//       response.put("role", user.getRole());
//        return ResponseEntity.ok(response);
//    }
//    @PostMapping(value = "/register",consumes = "application/json",produces = "application/json")
//    public ResponseEntity<User> register(@RequestBody User user) {
//        User savedUser = userService.registerUser(user);
//        return ResponseEntity.ok(savedUser);
//    }
// }


package com.edutech.educationalresourcedistributionsystem.controller;

import com.edutech.educationalresourcedistributionsystem.dto.LoginRequest;
import com.edutech.educationalresourcedistributionsystem.entity.User;
import com.edutech.educationalresourcedistributionsystem.jwt.JwtUtil;
import com.edutech.educationalresourcedistributionsystem.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class RegisterAndLoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping(
            value = "/login",
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {

        User user = userService.login(request.getUsername(), request.getPassword());

        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("username", user.getUsername());
        response.put("role", user.getRole());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping(
            value = "/register",
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<User> register(@RequestBody User user) {

        User saved = userService.registerUser(user);

        return ResponseEntity.status(201)
                .contentType(MediaType.APPLICATION_JSON)
                .body(saved);
    }
}