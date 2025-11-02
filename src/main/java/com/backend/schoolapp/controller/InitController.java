package com.backend.schoolapp.controller;

import com.backend.schoolapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class InitController {

    private final UserService userService;

    @PostMapping("/init")
    public ResponseEntity<String> initializeAdmin() {
        try {
            userService.createUser("admin", "admin123", "admin@schoolapp.com");
            return ResponseEntity.ok("Admin user created successfully. Username: admin, Password: admin123");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Admin user already exists or error: " + e.getMessage());
        }
    }
}