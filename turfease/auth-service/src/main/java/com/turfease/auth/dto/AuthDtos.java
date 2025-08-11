package com.turfease.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthDtos {
    public static class SignupRequest {
        @Email
        @NotBlank
        public String email;
        @NotBlank
        public String password;
        @NotBlank
        public String fullName;
    }

    public static class LoginRequest {
        @Email
        @NotBlank
        public String email;
        @NotBlank
        public String password;
    }

    public static class JwtResponse {
        public String token;
        public String role;
        public String email;
        public Long userId;
        public JwtResponse(String token, String role, String email, Long userId) {
            this.token = token;
            this.role = role;
            this.email = email;
            this.userId = userId;
        }
    }
}