package com.ticketbooking.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final String SECRET =
            "my-super-secret-key-for-ticket-booking-app-123456789";

    private final SecretKey key =
            Keys.hmacShaKeyFor(
                    SECRET.getBytes(StandardCharsets.UTF_8)
            );


    // ==============================
    // GENERATE JWT
    // ==============================

    public String generateToken(String email, String role) {

        return Jwts.builder()

                .subject(email)

                // Store role inside JWT
                .claim("role", role)

                .issuedAt(new Date())

                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 1000L * 60 * 60
                        )
                )

                .signWith(key)

                .compact();
    }


    // ==============================
    // EXTRACT EMAIL
    // ==============================

    public String extractEmail(String token) {

        return Jwts.parser()

                .verifyWith(key)

                .build()

                .parseSignedClaims(token)

                .getPayload()

                .getSubject();
    }


    // ==============================
    // EXTRACT ROLE
    // ==============================

    public String extractRole(String token) {

        return Jwts.parser()

                .verifyWith(key)

                .build()

                .parseSignedClaims(token)

                .getPayload()

                .get("role", String.class);
    }


    // ==============================
    // VALIDATE TOKEN
    // ==============================

    public boolean isTokenValid(String token) {

        try {

            Jwts.parser()

                    .verifyWith(key)

                    .build()

                    .parseSignedClaims(token);

            return true;

        } catch (Exception e) {

            System.out.println(
                    "JWT INVALID: " + e.getMessage()
            );

            return false;
        }
    }
}