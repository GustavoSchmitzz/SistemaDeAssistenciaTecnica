package com.assistencia.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record FuncionarioLoginDTO(
        @NotBlank(message = "o email nao pode ser vazio.")
        @Size(max = 50)
        @Email(message = "senha ou email invalido.")
        String email,
        @NotBlank
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$",
        message = "senha ou email invalido")
        String senha
) {}
