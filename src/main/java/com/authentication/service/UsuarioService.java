package com.authentication.service;

import com.authentication.domain.dto.UsuarioDTO;
import com.authentication.domain.entity.Usuario;
import com.authentication.domain.mapper.UsuarioMapper;
import com.authentication.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;

    public UsuarioDTO registrarUsuario(UsuarioDTO usuarioDTO) {
        UsuarioDTO usuarioDTOCripitografado = criptografarSenha(usuarioDTO);
        Usuario usuario = repository.save(mapper.dtoToEntity(usuarioDTOCripitografado));

        return mapper.entityToDTO(usuario);
    }

    public UsuarioDTO criptografarSenha(UsuarioDTO usuarioDTO){
        String senhaCriptografada = new BCryptPasswordEncoder().encode(usuarioDTO.getSenha());
        UsuarioDTO usuarioDTOCriptografado = new UsuarioDTO(usuarioDTO.getLogin(),senhaCriptografada, usuarioDTO.getRole());
        return usuarioDTOCriptografado;
    }
}
