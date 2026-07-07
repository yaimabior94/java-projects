package com.smartinventory.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtVerificationTest {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Test
    public void testTokenGenerationAndValidation() {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "admin",
                "hashedpassword",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);

        String username = jwtService.extractUsername(token);
        assertEquals("admin", username);

        boolean isValid = jwtService.isTokenValid(token, userDetails);
        assertTrue(isValid);
    }
}
