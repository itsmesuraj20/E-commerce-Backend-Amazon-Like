package com.ecommerce.config;

import com.ecommerce.user.entity.User;
import com.ecommerce.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedAdminUser();
    }

    private void seedAdminUser() {
        // Check if admin already exists
        if (userRepository.findByEmail("admin@ecommerce.com").isEmpty()) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@ecommerce.com")
                    .password(passwordEncoder.encode("admin123"))
                    .firstName("System")
                    .lastName("Administrator")
                    .phone("+1234567890")
                    .role(User.Role.ADMIN)
                    .isActive(true)
                    .emailVerified(true)
                    .build();

            userRepository.save(admin);
            log.info("Admin user created successfully with email: admin@ecommerce.com");
            log.info("Admin credentials - Email: admin@ecommerce.com, Password: admin123");
        } else {
            log.info("Admin user already exists");
        }
    }
}