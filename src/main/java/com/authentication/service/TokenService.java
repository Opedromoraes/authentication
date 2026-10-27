package com.authentication.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.authentication.domain.entity.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class TokenService {

    static final String ISSUER = "auth-api";
    private static final Duration EXPIRATION = Duration.ofHours(1);

    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(Usuario usuario) {
        try {
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(usuario.getLogin())
                    .withExpiresAt(Instant.now().plus(EXPIRATION))
                    .sign(Algorithm.HMAC256(secret));
        } catch (JWTCreationException exception) {
            throw new IllegalStateException("Error while generating token", exception);
        }
    }

    /**
     * Returns the token subject (login), or an empty string when the token is invalid or expired,
     * so callers simply leave the request unauthenticated.
     */
    public String validarToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }
    }
}
