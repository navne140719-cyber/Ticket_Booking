package com.ticketbooking.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
        System.out.println("REQUEST = " + request.getMethod() + " " + request.getRequestURI());
        System.out.println("AUTH HEADER = " + authHeader);

        // No Authorization header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            System.out.println("NO JWT FOUND");

            filterChain.doFilter(request, response);
            return;
        }

        // Remove "Bearer "
        String token = authHeader.substring(7);

        System.out.println("JWT FOUND");

        try {

            // Extract email from JWT
            String email = jwtService.extractEmail(token);

            System.out.println("EMAIL FROM JWT = " + email);

            // Authenticate only if there isn't already authentication
            if (email != null &&
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                Collections.emptyList()
                        );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);

                System.out.println("USER AUTHENTICATED = " + email);
            }

        } catch (Exception e) {

            System.out.println("JWT VALIDATION FAILED");
            System.out.println("REASON = " + e.getMessage());

            // Remove invalid authentication if any
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(null);
        }

        filterChain.doFilter(request, response);
    }
}