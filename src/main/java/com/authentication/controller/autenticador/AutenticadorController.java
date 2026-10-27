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

    private final AuthenticationManager authenticationManager;
    private final TokenService service;

    @PostMapping("/login")
    public ResponseEntity<AutenticadorResponse> login(@RequestBody @Valid AutenticadorRequest request){

        var usernamePassword = new UsernamePasswordAuthenticationToken(request.getLogin(),request.getSenha());
        var autenticador = authenticationManager.authenticate(usernamePassword);

        var token = service.gerarToken((Usuario) autenticador.getPrincipal());

        return ResponseEntity.ok(new AutenticadorResponse(token));
    }

}
