package com.authentication.domain.mapper;

import com.authentication.controller.usuario.request.UsuarioRequest;
import com.authentication.controller.usuario.response.UsuarioResponse;
import com.authentication.domain.dto.UsuarioDTO;
import com.authentication.domain.entity.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioDTO entityToDTO(Usuario entity);

    Usuario dtoToEntity(UsuarioDTO usuarioDTO);

    UsuarioResponse dtoToResponse(UsuarioDTO usuarioDTO);

    UsuarioDTO requestToDto(UsuarioRequest request);
}
