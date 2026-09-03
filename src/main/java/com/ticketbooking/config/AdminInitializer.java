package com.ticketbooking.config;

import com.ticketbooking.entity.User;
import com.ticketbooking.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {
    @Bean
    CommandLineRunner initializeUsers(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            // ==========================================
            // FIX OLD USERS
            // ==========================================

            for (User user : userRepository.findAll()) {

                if (user.getRole() == null ||
                        user.getRole().isBlank()) {

                    user.setRole("USER");
                    userRepository.save(user);

                    System.out.println(
                            "ROLE UPDATED TO USER FOR: "
                                    + user.getEmail()
                    );
                }
            }


            // ==========================================
            // CREATE ADMIN
            // ==========================================

            String adminEmail = "admin@gmail.com";

            if (userRepository.findByEmail(adminEmail).isEmpty()) {

                User admin = new User(
                        "Admin",
                        adminEmail,
                        passwordEncoder.encode("Admin@123456"),
                        "ADMIN"
                );

                userRepository.save(admin);

                System.out.println(
                        "================================="
                );
                System.out.println(
                        "ADMIN ACCOUNT CREATED"
                );
                System.out.println(
                        "EMAIL: admin@gmail.com"
                );
                System.out.println(
                        "ROLE: ADMIN"
                );
                System.out.println(
                        "================================="
                );

            } else {

                System.out.println(
                        "ADMIN ACCOUNT ALREADY EXISTS"
                );
            }
        };
    }
}