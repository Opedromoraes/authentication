package com.authentication.controller.usuario.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequest {

    @NotBlank
    private String login;
    @NotBlank
    private String senha;
    @NotBlank
    @Pattern(regexp = "ADMIN|USUARIO", message = "must be ADMIN or USUARIO")
    private String role;
}
