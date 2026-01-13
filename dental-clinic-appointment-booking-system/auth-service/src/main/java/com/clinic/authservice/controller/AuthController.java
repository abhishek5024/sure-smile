package com.clinic.authservice.controller;

import com.clinic.authservice.dto.AuthResponse;
import com.clinic.authservice.dto.LoginRequest;
import com.clinic.authservice.dto.RegisterRequest;
import com.clinic.authservice.entity.User;
import com.clinic.authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // Only allow PATIENT registration
        if (request.getEmail() != null && request.getEmail().toLowerCase().contains("doctor")) {
            return ResponseEntity.badRequest().body("Doctor registration not allowed");
        }
        authService.register(request);
        return ResponseEntity.ok("Registration successful");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/doctor/login")
    public ResponseEntity<AuthResponse> doctorLogin(@RequestBody LoginRequest request) {
        AuthResponse response = authService.loginDoctor(request);
        return ResponseEntity.ok(response);
    }
}
