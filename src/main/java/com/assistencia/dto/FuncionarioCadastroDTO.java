package com.assistencia.dto;

public record FuncionarioCadastroDTO(
        String nome,
        String email,
        String senha,
        String telefone,
        String especialidade
) {}
