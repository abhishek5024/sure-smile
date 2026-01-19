package com.clinic.authservice.service;

import com.clinic.authservice.dto.AuthResponse;
import com.clinic.authservice.dto.LoginRequest;
import com.clinic.authservice.dto.RegisterRequest;
import com.clinic.authservice.entity.User;
import com.clinic.authservice.entity.User.Role;
import com.clinic.authservice.repository.UserRepository;
import com.clinic.authservice.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.PATIENT);
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getRole().name(), user.getId());
    }

    public AuthResponse loginDoctor(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            // Debug: Print authentication details
            System.out.println("[DEBUG] Authenticated: " + authentication.isAuthenticated());
            System.out.println("[DEBUG] Principal: " + authentication.getPrincipal());
            System.out.println("[DEBUG] Authorities: " + authentication.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            System.out.println("[DEBUG] User loaded: " + user.getEmail() + ", role: " + user.getRole());
            if (!user.getRole().name().equals("DOCTOR")) {
                System.out.println("[DEBUG] Not a doctor account, role found: " + user.getRole());
                throw new RuntimeException("Not a doctor account");
            }
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
            System.out.println("[DEBUG] JWT generated: " + token);
            return new AuthResponse(token, user.getRole().name(), user.getId());
        } catch (Exception e) {
            System.out.println("[DEBUG] Authentication failed: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            throw e;
        }
    }

    @PostConstruct
    public void printDoctorPasswordHash() {
        String hash = passwordEncoder.encode("password123");
        System.out.println("[DEBUG] Bcrypt hash for password123: " + hash);
    }
}
