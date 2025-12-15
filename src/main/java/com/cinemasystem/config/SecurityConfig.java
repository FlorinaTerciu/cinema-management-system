package com.cinemasystem.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity // enables @PreAuthorize
@RequiredArgsConstructor
public class SecurityConfig {

    private final KeycloakRoleConverter keycloakRoleConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/api/showtimes/conflicts/**").hasAuthority("EMPLOYEE")

                        .requestMatchers(HttpMethod.GET,
                                "/api/movies/**",
                                "/api/rooms/**",
                                "/api/seats/**",
                                "/api/showtimes/**"
                        ).permitAll()

                        .requestMatchers(HttpMethod.POST,
                                "/api/movies/**",
                                "/api/rooms/**",
                                "/api/seats/**",
                                "/api/showtimes/**"
                        ).hasAuthority("EMPLOYEE")

                        .requestMatchers(HttpMethod.PUT,
                                "/api/movies/**",
                                "/api/rooms/**",
                                "/api/seats/**",
                                "/api/showtimes/**"
                        ).hasAuthority("EMPLOYEE")

                        .requestMatchers(HttpMethod.DELETE,
                                "/api/movies/**",
                                "/api/rooms/**",
                                "/api/seats/**",
                                "/api/showtimes/**"
                        ).hasAuthority("EMPLOYEE")

                        .requestMatchers(HttpMethod.GET, "/api/users/me").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/users/me").authenticated()
                        .requestMatchers("/api/users/**").hasAuthority("EMPLOYEE")
                        .requestMatchers("/api/tickets/**").authenticated()

                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth ->
                        oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );

        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(keycloakRoleConverter);
        return converter;
    }
}
