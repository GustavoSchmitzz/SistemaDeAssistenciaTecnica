package com.assistencia.dto;

public record ClienteCadastroResponseDTO(
        int id,
        String nome,
        String email,
        String telefone
) {}
