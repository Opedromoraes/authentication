package com.authentication.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.authentication.domain.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${api.security.token.secret")
    private String secret;

    public String gerarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret); // algorítimo escolhido recebe um "secret" para que os algoritimos recebam HASHS (chaves) >>ÚNICOS<<
            String token = JWT.create()
                    .withIssuer("auth-api") // nome da aplicação
                    .withSubject(usuario.getLogin()) //usuario que recebe o token
                    .withExpiresAt(LocalDateTime.now()
                            .plusHours(1)
                            .toInstant(ZoneOffset.of("-03:00"))) //  indicando para o instante -> FUSO HORÁRIO -3 = horário de brasilia, BR
                    .sign(algorithm); // assinatura e geração final

            return token;
        } catch (JWTCreationException exception) { // se der erro retorna um erro "bonitinho" pro usuário
            throw new RuntimeException("Erro na geração do token", exception);
        }
    }

    public String validarToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token) // decriptografando o token
                    .getSubject();  //peguei o subject que salvei dentro do token
        } catch (JWTVerificationException exception){ // token inválido
            return ""; // retornar string vazia, pois ométodo que receber uma spring vazia, vai retornar o erro de não autorizado
        }
    }
}
