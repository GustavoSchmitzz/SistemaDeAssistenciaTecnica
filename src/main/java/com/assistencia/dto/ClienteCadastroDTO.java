package com.assistencia.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteCadastroDTO(
        @NotBlank(message = "o nome nao pode ser vazio.")
        @Size(max = 100,message = "o nome nao pode ter mais que 100 caracteres.")
        String nome,
        @NotBlank(message = "o telefone nao pode ser vazio.")
        @Pattern(regexp = "^[0-9]{10,11}$", message = "telefone invalido.")
        String telefone,
        @NotBlank(message = "o email nao pode ser vazio.")
        @Size(max = 50)
        @Email(message = "email invalido.")
        String email
) {}
