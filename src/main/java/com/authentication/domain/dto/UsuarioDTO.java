package com.authentication.domain.dto;

import com.authentication.domain.enums.UserRole;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    private Long id;
    private String login;
    private String senha;
    private UserRole role;

    public UsuarioDTO(String login, String senhaCriptografada, UserRole role) {
        this.login = login;
        this.senha = senhaCriptografada;
        this.role = role;
    }
}
