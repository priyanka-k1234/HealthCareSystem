package com.example.healthcare;

import com.example.healthcare.model.User;
import com.example.healthcare.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HealthcareApplication {
    public static void main(String[] args) {
        SpringApplication.run(HealthcareApplication.class, args);
    }

    @Bean
    public CommandLineRunner createAdminUser(UserService userService) {
        return args -> {
            if (userService.findByUsername("admin").isEmpty()) {
                User admin = User.builder()
                        .username("admin")
                        .email("admin@healthcare.com")
                        .role("ADMIN")
                        .build();
                userService.saveUser(admin, "admin");
                System.out.println("Admin user created: admin/admin");
            }
        };
    }
}