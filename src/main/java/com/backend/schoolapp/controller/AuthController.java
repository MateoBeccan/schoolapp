package com.backend.schoolapp.controller;

import com.backend.schoolapp.model.LoginRequest;
import com.backend.schoolapp.model.LoginResponse;
import com.backend.schoolapp.model.User;
import com.backend.schoolapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            User user = userService.findByUsername(request.getUsername());
            
            if (user != null && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                LoginResponse response = new LoginResponse(
                        "simple-token-" + user.getUsername(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole().name()
                );
                return ResponseEntity.ok(response);
            }
            
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User userRequest) {
        try {
            userService.createUser(userRequest.getUsername(), userRequest.getPassword(), userRequest.getEmail());
            return ResponseEntity.ok("User created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok("Token is valid");
    }
}