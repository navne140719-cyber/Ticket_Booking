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

        // Don't print the actual JWT
        System.out.println(
                "AUTH HEADER PRESENT = "
                        + (authHeader != null)
        );


        // ==============================
        // NO JWT
        // ==============================

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            System.out.println("NO JWT FOUND");

            filterChain.doFilter(request, response);
            return;
        }


        // ==============================
        // GET TOKEN
        // ==============================

        String token = authHeader.substring(7);

        System.out.println("JWT FOUND");


        try {

            // ==============================
            // EXTRACT EMAIL
            // ==============================

            String email =
                    jwtService.extractEmail(token);

            // ==============================
            // EXTRACT ROLE
            // ==============================

            String role =
                    jwtService.extractRole(token);

            System.out.println(
                    "EMAIL FROM JWT = " + email
            );

            System.out.println(
                    "ROLE FROM JWT = " + role
            );


            // ==============================
            // AUTHENTICATE USER
            // ==============================

            if (email != null &&
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null) {

                /*
                 * Spring Security expects authorities
                 * in the form:
                 *
                 * ROLE_USER
                 * ROLE_ADMIN
                 */

                String authority =
                        "ROLE_" + role;

                UsernamePasswordAuthenticationToken
                        authentication =
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
                        .setAuthentication(
                                authentication
                        );

                System.out.println(
                        "USER AUTHENTICATED = "
                                + email
                );

                System.out.println(
                        "AUTHORITY = "
                                + authority
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "JWT VALIDATION FAILED"
            );
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