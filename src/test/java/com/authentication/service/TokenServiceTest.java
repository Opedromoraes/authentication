package com.authentication.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.authentication.domain.entity.Usuario;
import com.authentication.domain.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class TokenServiceTest {

    private static final String SECRET = "unit-test-secret";

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        ReflectionTestUtils.setField(tokenService, "secret", SECRET);
    }

    @Test
    void generatedTokenIsValidAndCarriesTheLogin() {
        Usuario usuario = Usuario.builder().login("alice").senha("x").role(UserRole.USUARIO).build();

        String token = tokenService.gerarToken(usuario);

        assertThat(tokenService.validarToken(token)).isEqualTo("alice");
    }

    @Test
    void generatedTokenExpiresInOneHour() {
        Usuario usuario = Usuario.builder().login("alice").senha("x").role(UserRole.USUARIO).build();

        Instant expiresAt = JWT.decode(tokenService.gerarToken(usuario)).getExpiresAtAsInstant();

        assertThat(expiresAt).isBetween(Instant.now().plusSeconds(3590), Instant.now().plusSeconds(3610));
    }

    @Test
    void expiredTokenIsRejected() {
        String expired = JWT.create()
                .withIssuer(TokenService.ISSUER)
                .withSubject("alice")
                .withExpiresAt(Instant.now().minusSeconds(60))
                .sign(Algorithm.HMAC256(SECRET));

        assertThat(tokenService.validarToken(expired)).isEmpty();
    }

    @Test
    void tokenSignedWithAnotherSecretIsRejected() {
        String forged = JWT.create()
                .withIssuer(TokenService.ISSUER)
                .withSubject("alice")
                .withExpiresAt(Instant.now().plusSeconds(60))
                .sign(Algorithm.HMAC256("another-secret"));

        assertThat(tokenService.validarToken(forged)).isEmpty();
    }

    @Test
    void malformedTokenIsRejected() {
        assertThat(tokenService.validarToken("not-a-jwt")).isEmpty();
    }
}
