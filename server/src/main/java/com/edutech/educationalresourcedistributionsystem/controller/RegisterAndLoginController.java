package com.edutech.educationalresourcedistributionsystem.controller;
import com.edutech.educationalresourcedistributionsystem.dto.LoginRequest;
import com.edutech.educationalresourcedistributionsystem.dto.LoginResponse;
import com.edutech.educationalresourcedistributionsystem.dto.UserDto;
import com.edutech.educationalresourcedistributionsystem.dto.DtoMapper;
import com.edutech.educationalresourcedistributionsystem.entity.User;
import com.edutech.educationalresourcedistributionsystem.jwt.JwtUtil;
import com.edutech.educationalresourcedistributionsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/user")
public class RegisterAndLoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        User user = userService.login(request.getUsername(), request.getPassword());
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        LoginResponse response = new LoginResponse(
                token,
                user.getUsername(),
                user.getRole()
        );
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
   @PostMapping("/register")
public ResponseEntity<UserDto> register(@RequestBody User user) {
    try {
        System.out.println("=== REGISTER REQUEST RECEIVED ===");
        System.out.println("Username: " + user.getUsername());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Role: " + user.getRole());

        User savedUser = userService.registerUser(user);

        System.out.println("=== USER SAVED SUCCESSFULLY ===");

        return ResponseEntity.status(201)
                .body(DtoMapper.toUserDTO(savedUser));

    } catch (Exception e) {
        System.out.println("=== REGISTRATION FAILED ===");
        e.printStackTrace();   // 🔥 THIS IS THE KEY
        return ResponseEntity.status(500).build();
    }
}
}