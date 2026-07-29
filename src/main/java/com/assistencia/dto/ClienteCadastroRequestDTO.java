package com.assistencia.dto;

public record ClienteCadastroRequestDTO(
        String nome,
        String telefone,
        String email
) {}
