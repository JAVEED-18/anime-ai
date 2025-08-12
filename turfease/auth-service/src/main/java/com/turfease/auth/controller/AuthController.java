package com.turfease.auth.controller;

import com.turfease.auth.dto.AuthDtos;
import com.turfease.auth.model.Role;
import com.turfease.auth.model.User;
import com.turfease.auth.repository.UserRepository;
import com.turfease.auth.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/user/signup")
    public ResponseEntity<?> userSignup(@Valid @RequestBody AuthDtos.SignupRequest request) {
        return signup(request, Role.USER);
    }

    @PostMapping("/admin/signup")
    public ResponseEntity<?> adminSignup(@Valid @RequestBody AuthDtos.SignupRequest request) {
        return signup(request, Role.ADMIN);
    }

    private ResponseEntity<?> signup(AuthDtos.SignupRequest request, Role role) {
        if (userRepository.existsByEmail(request.email)) {
            return ResponseEntity.badRequest().body("Email already in use");
        }
        User user = new User();
        user.setEmail(request.email);
        user.setPassword(passwordEncoder.encode(request.password));
        user.setFullName(request.fullName);
        user.setRole(role);
        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());
        return ResponseEntity.ok(new AuthDtos.JwtResponse(token, user.getRole().name(), user.getEmail(), user.getId()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthDtos.LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email, request.password));
        User user = userRepository.findByEmail(request.email).orElseThrow();
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());
        return ResponseEntity.ok(new AuthDtos.JwtResponse(token, user.getRole().name(), user.getEmail(), user.getId()));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null) return ResponseEntity.status(401).build();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        return ResponseEntity.ok(new AuthDtos.JwtResponse(null, user.getRole().name(), user.getEmail(), user.getId()));
    }
}