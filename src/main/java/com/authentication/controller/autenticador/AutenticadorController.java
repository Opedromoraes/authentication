package com.authentication.controller.autenticador;

import com.authentication.controller.autenticador.request.AutenticadorRequest;
import com.authentication.controller.autenticador.response.AutenticadorResponse;
import com.authentication.domain.entity.Usuario;
import com.authentication.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("autenticador")
public class AutenticadorController {

    // A validação das senhas vai ser feita através do Hash (criptografado após o usuário lançar sua senha)...
    //... comparado com o Hash do banco de dados (uma vez cadastrado anteriormente -> Admin ou User)

    private final AuthenticationManager authenticationManager;
    private final TokenService service;

    @PostMapping("/login") // enpoint para descobrir se a senha passada pelo usuário é a que está salva no banco
    public ResponseEntity login(@RequestBody @Valid AutenticadorRequest request){

        var usernamePassword = new UsernamePasswordAuthenticationToken(request.getLogin(),request.getSenha()); // criptografando
        var autenticador = authenticationManager.authenticate(usernamePassword); // validando a senha

        var token = service.gerarToken((Usuario) autenticador.getPrincipal()); // pegando o objeto principal e fazendo um cast para Usuario

        return ResponseEntity.ok(new AutenticadorResponse(token)); // retornando o token para o usuário
    }

}
