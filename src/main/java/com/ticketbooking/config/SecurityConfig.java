
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

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .cors(cors ->
                        cors.configurationSource(corsConfigurationSource())
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .exceptionHandling(exception ->
                        exception.accessDeniedHandler(accessDeniedHandler())
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/error").permitAll()

                        // Public user endpoints
                        .requestMatchers("/users", "/users/login").permitAll()

                        // Public movie read endpoints
                        .requestMatchers(
                                HttpMethod.GET,
                                "/movies",
                                "/movies/**"
                        ).permitAll()

                        // Admin movie endpoints
                        .requestMatchers(HttpMethod.POST, "/movies").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/movies/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/movies/**").hasRole("ADMIN")

                        // Seat hold endpoints
                        .requestMatchers(HttpMethod.POST, "/seat-holds").authenticated()
                        .requestMatchers(HttpMethod.GET, "/seat-holds/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/seat-holds/**").authenticated()

                        // Booking endpoints
                        .requestMatchers(HttpMethod.GET, "/bookings").authenticated()
                        .requestMatchers(HttpMethod.POST, "/bookings").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/bookings/**").authenticated()

                        // Keep other endpoints protected
                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, exception) -> {
            System.out.println("==============================");
            System.out.println("ACCESS DENIED");
            System.out.println("METHOD = " + request.getMethod());
            System.out.println("URI = " + request.getRequestURI());
            System.out.println("REASON = " + exception.getMessage());
            System.out.println("==============================");

            response.sendError(
                    HttpStatus.FORBIDDEN.value(),
                    "Access denied"
            );
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "https://glittery-empanada-015d82.netlify.app",
                "http://localhost:5173",
                "http://localhost:5174"
        ));

        configuration.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
