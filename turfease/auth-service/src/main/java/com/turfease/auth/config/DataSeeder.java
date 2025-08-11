package com.turfease.auth.config;

import com.turfease.auth.model.Role;
import com.turfease.auth.model.User;
import com.turfease.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedUsers(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (!repo.existsByEmail("admin@turfease.com")) {
                User admin = new User();
                admin.setEmail("admin@turfease.com");
                admin.setPassword(encoder.encode("Admin@1234"));
                admin.setFullName("Admin");
                admin.setRole(Role.ADMIN);
                repo.save(admin);
            }
            if (!repo.existsByEmail("user@turfease.com")) {
                User user = new User();
                user.setEmail("user@turfease.com");
                user.setPassword(encoder.encode("User@1234"));
                user.setFullName("User");
                user.setRole(Role.USER);
                repo.save(user);
            }
        };
    }
}