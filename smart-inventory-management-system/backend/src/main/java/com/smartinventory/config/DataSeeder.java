package com.smartinventory.config;

import com.smartinventory.entity.Role;
import com.smartinventory.entity.User;
import com.smartinventory.repository.RoleRepository;
import com.smartinventory.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Seeds the in-memory H2 database with default roles and an admin user
 * so the application is usable immediately after startup.
 *
 * Credentials:
 * Username: admin
 * Password: admin2026
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // --- Seed Roles ---
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> {
                    Role role = new Role("ADMIN", "Administrator with full access");
                    log.info("Creating default role: ADMIN");
                    return roleRepository.save(role);
                });

        roleRepository.findByName("STAFF")
                .orElseGet(() -> {
                    Role role = new Role("STAFF", "Staff with limited access");
                    log.info("Creating default role: STAFF");
                    return roleRepository.save(role);
                });

        // --- Seed Admin User ---
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@smartinventory.com");
            admin.setPasswordHash(passwordEncoder.encode("admin2026"));
            admin.setFullName("System Administrator");
            admin.setPhone("+1-000-000-0000");
            admin.setStatus("ACTIVE");
            admin.setRole(adminRole);
            userRepository.save(admin);
            log.info("Default admin user created  →  username: admin  |  password: admin2026");
        } else {
            log.info("Admin user already exists — skipping seed.");
        }
    }
}
