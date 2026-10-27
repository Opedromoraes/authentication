package com.authentication.controller.autenticador.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AutenticadorRequest {

    @NotBlank
    private String login;
    @NotBlank
    private String senha;
}

