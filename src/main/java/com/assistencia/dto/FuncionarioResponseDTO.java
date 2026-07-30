package com.assistencia.dto;

public record FuncionarioResponseDTO(
        int id,
        String nome,
        String email,
        String telefone,
        String especialidade
) {}
