package com.assistencia.dto;

public record PecaCadastroDTO(
        String nome,
        double valor,
        Integer idFornecedor,
        int estoque
) {
}
