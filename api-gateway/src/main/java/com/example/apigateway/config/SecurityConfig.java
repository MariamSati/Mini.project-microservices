package com.example.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .authorizeExchange(exchanges -> exchanges
                .anyExchange().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt()
            );
        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder(@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuer) {
        String jwkSetUri = issuer.endsWith("/") ? issuer + "protocol/openid-connect/certs" : issuer + "/protocol/openid-connect/certs";
        NimbusReactiveJwtDecoder jwtDecoder = NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();

        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        OAuth2TokenValidator<Jwt> audienceValidator = new OAuth2TokenValidator<Jwt>() {
            @Override
            public OAuth2TokenValidatorResult validate(Jwt token) {
                Object aud = token.getClaims().get("aud");
                boolean ok = false;
                if (aud instanceof java.util.List) {
                    ok = ((java.util.List<?>) aud).contains("backend-services");
                } else if (aud != null) {
                    ok = aud.toString().contains("backend-services");
                }
                if (ok) return OAuth2TokenValidatorResult.success();
                return OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "The required audience is missing", "https://tools.ietf.org/html/rfc6750"));
            }
        };

        DelegatingOAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);
        jwtDecoder.setJwtValidator(validator);
        return jwtDecoder;
    }
}
