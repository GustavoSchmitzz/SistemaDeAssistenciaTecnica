package com.assistencia.dto;

public record ClienteResponseDTO(
        int id,
        String nome,
        String email,
        String telefone
) {}
