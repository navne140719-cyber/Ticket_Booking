package com.ticketbooking.config;

import com.ticketbooking.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
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

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
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

                // ==============================
                // DISABLE CSRF
                // ==============================

                .csrf(csrf -> csrf.disable())

                // ==============================
                // ENABLE CORS
                // ==============================

                .cors(cors -> cors.configurationSource(
                        corsConfigurationSource()
                ))

                // ==============================
                // JWT APPLICATION IS STATELESS
                // ==============================

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // ==============================
                // ACCESS DENIED HANDLER
                // ==============================

                .exceptionHandling(exception ->
                        exception.accessDeniedHandler(
                                accessDeniedHandler()
                        )
                )

                // ==============================
                // AUTHORIZATION
                // ==============================

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

                        .requestMatchers(
                                "/users",
                                "/users/login"
                        ).permitAll()

                        // ==============================
                        // PUBLIC MOVIE READ
                        // ==============================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/movies",
                                "/movies/**"
                        ).permitAll()

                        // ==============================
                        // ADMIN MOVIE ENDPOINTS
                        // ==============================

                        .requestMatchers(
                                HttpMethod.POST,
                                "/movies"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/movies/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/movies/**"
                        ).hasRole("ADMIN")

                        // ==============================
                        // BOOKING ENDPOINTS
                        // ==============================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/bookings"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/bookings"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/bookings/**"
                        ).authenticated()

                        // ==============================
                        // EVERYTHING ELSE
                        // ==============================

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
    // ACCESS DENIED HANDLER
    // ==============================

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {

        return (request, response, exception) -> {

            System.out.println("==============================");
            System.out.println("🔥 ACCESS DENIED");
            System.out.println(
                    "METHOD = " + request.getMethod()
            );
            System.out.println(
                    "URI = " + request.getRequestURI()
            );
            System.out.println(
                    "REASON = " + exception.getMessage()
            );

            response.sendError(
                    HttpStatus.FORBIDDEN.value(),
                    "Access denied"
            );
        };
    }

    // ==============================
    // CORS CONFIGURATION
    // ==============================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOriginPatterns(
                List.of(
                        "http://localhost:5173",
                        "http://localhost:5174",
                        "https://glittery-empanada-015d82.netlify.app",
                        "https://*.netlify.app"
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