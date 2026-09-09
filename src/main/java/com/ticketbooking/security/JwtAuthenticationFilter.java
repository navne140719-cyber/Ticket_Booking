package com.ticketbooking.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        System.out.println("=================================");
        System.out.println(
                "REQUEST = "
                        + request.getMethod()
                        + " "
                        + request.getRequestURI()
        );

        System.out.println(
                "AUTH HEADER PRESENT = "
                        + (authHeader != null)
        );

        // No JWT
        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            System.out.println("NO JWT FOUND");

            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        System.out.println("JWT FOUND");

        try {

            // Validate JWT first
            if (!jwtService.isTokenValid(token)) {

                System.out.println("JWT IS INVALID");

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(null);

                filterChain.doFilter(request, response);
                return;
            }

            String email = jwtService.extractEmail(token);
            String role = jwtService.extractRole(token);

            System.out.println(
                    "EMAIL FROM JWT = " + email
            );

            System.out.println(
                    "ROLE FROM JWT = " + role
            );

            if (email != null &&
                    !email.isBlank() &&
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null) {

                if (role == null || role.isBlank()) {
                    role = "USER";
                }

                String authority;

                if (role.startsWith("ROLE_")) {
                    authority = role;
                } else {
                    authority = "ROLE_" + role;
                }

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                Collections.singletonList(
                                        new SimpleGrantedAuthority(
                                                authority
                                        )
                                )
                        );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);

                System.out.println(
                        "USER AUTHENTICATED = " + email
                );

                System.out.println(
                        "AUTHORITY = " + authority
                );
            }

        } catch (Exception e) {

            System.out.println("JWT VALIDATION FAILED");
            System.out.println(
                    "REASON = " + e.getMessage()
            );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(null);
        }

        filterChain.doFilter(request, response);
    }
}