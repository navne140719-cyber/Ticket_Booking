package com.ticketbooking.config;

import com.ticketbooking.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter =
                jwtAuthenticationFilter;
    }


    // ==============================
    // PASSWORD ENCODER
    // ==============================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


    // ==============================
    // SECURITY CONFIGURATION
    // ==============================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http

                // Disable CSRF
                .csrf(csrf -> csrf.disable())

                // Enable CORS
                .cors(cors -> cors.configurationSource(
                        corsConfigurationSource()
                ))

                // Authorization rules
                .authorizeHttpRequests(auth -> auth

                        // ==============================
                        // CORS PREFLIGHT
                        // ==============================

                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        ).permitAll()


                        // ==============================
                        // PUBLIC USER ENDPOINTS
                        // ==============================

                        // Anyone can create an account
                        // and login
                        .requestMatchers(
                                "/users",
                                "/users/login"
                        ).permitAll()


                        // ==============================
                        // PUBLIC MOVIE READ ENDPOINTS
                        // ==============================

                        // Anyone can view movies
                        .requestMatchers(
                                HttpMethod.GET,
                                "/movies",
                                "/movies/**"
                        ).permitAll()
                        // ADMIN MOVIE ENDPOINTS
                        // Only ADMIN can add a movie
                        .requestMatchers(
                                HttpMethod.POST,
                                "/movies"
                        ).hasRole("ADMIN")
                        // Only ADMIN can update a movie
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/movies/**"
                        ).hasRole("ADMIN")
                        // Only ADMIN can delete a movie
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/movies/**"
                        ).hasRole("ADMIN")
                        // EVERYTHING ELSE
                        .anyRequest()
                        .authenticated()
                )

                // ==============================
                // JWT FILTER
                // ==============================
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }


    // ==============================
    // CORS CONFIGURATION
    // ==============================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:5173",
                        "http://localhost:5174",
                        "https://glittery-empanada-015d82.netlify.app"
                )
        );
        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )
        );
        configuration.setAllowedHeaders(
                List.of("*")
        );
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();


        source.registerCorsConfiguration(
                "/**",
                configuration
        );
        return source;
    }
}