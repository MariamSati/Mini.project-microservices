package com.example.commande.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt().jwtAuthenticationConverter(jwtAuthenticationConverter())
            );

        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(this::extractAuthorities);
        return converter;
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        Object realmAccess = jwt.getClaim("realm_access");
        if (realmAccess instanceof java.util.Map) {
            Object roles = ((java.util.Map<?, ?>) realmAccess).get("roles");
            if (roles instanceof java.util.Collection) {
                return ((java.util.Collection<?>) roles).stream()
                        .map(Object::toString)
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                        .collect(Collectors.toList());
            }
        }
        return List.of();
    }

    @Bean
    public org.springframework.security.oauth2.jwt.JwtDecoder jwtDecoder(@org.springframework.beans.factory.annotation.Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuer) {
        org.springframework.security.oauth2.jwt.NimbusJwtDecoder jwtDecoder = (org.springframework.security.oauth2.jwt.NimbusJwtDecoder) org.springframework.security.oauth2.jwt.JwtDecoders.fromIssuerLocation(issuer);
        org.springframework.security.oauth2.core.OAuth2TokenValidator<Jwt> withIssuer = org.springframework.security.oauth2.jwt.JwtValidators.createDefaultWithIssuer(issuer);
        org.springframework.security.oauth2.core.OAuth2TokenValidator<Jwt> audienceValidator = new org.springframework.security.oauth2.core.OAuth2TokenValidator<Jwt>() {
            @Override
            public org.springframework.security.oauth2.core.OAuth2TokenValidatorResult validate(Jwt token) {
                Object aud = token.getClaims().get("aud");
                boolean ok = false;
                if (aud instanceof java.util.List) {
                    ok = ((java.util.List<?>)aud).contains("backend-services");
                } else if (aud != null) {
                    ok = aud.toString().contains("backend-services");
                }
                if (ok) return org.springframework.security.oauth2.core.OAuth2TokenValidatorResult.success();
                return org.springframework.security.oauth2.core.OAuth2TokenValidatorResult.failure(new org.springframework.security.oauth2.core.OAuth2Error("invalid_token","The required audience is missing","https://tools.ietf.org/html/rfc6750"));
            }
        };
        org.springframework.security.oauth2.core.OAuth2TokenValidator<Jwt> validator = new org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);
        jwtDecoder.setJwtValidator(validator);
        return jwtDecoder;
    }
}
