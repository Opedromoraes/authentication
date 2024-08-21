package com.authentication.controller.usuario;

import com.authentication.controller.usuario.request.UsuarioRequest;
import com.authentication.controller.usuario.response.UsuarioResponse;
import com.authentication.domain.dto.UsuarioDTO;
import com.authentication.domain.mapper.UsuarioMapper;
import com.authentication.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("usuario")
public class UsuarioController {

    private final UsuarioService service;
    private final UsuarioMapper mapper;

    @PostMapping("/registrar")
    public ResponseEntity<UsuarioResponse> registrarUsuario(@RequestBody @Valid UsuarioRequest request){
        UsuarioDTO usuarioDTO = service.registrarUsuario(mapper.requestToDto(request));
        UsuarioResponse response = mapper.dtoToResponse(usuarioDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
