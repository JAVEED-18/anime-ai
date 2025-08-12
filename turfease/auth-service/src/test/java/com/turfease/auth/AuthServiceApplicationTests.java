package com.turfease.auth;

import com.turfease.auth.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceApplicationTests {

    @Autowired
    JwtUtil jwtUtil;

    @Test
    void contextLoads() { }

    @Test
    void jwtIncludesRoleAndUid() {
        String token = jwtUtil.generateToken("user@turfease.com", "USER", 123L);
        assertEquals("user@turfease.com", jwtUtil.getUsernameFromToken(token));
        assertEquals("USER", jwtUtil.getRoleFromToken(token));
        assertEquals(123L, jwtUtil.getUserIdFromToken(token));
    }
}