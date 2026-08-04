package com.assistencia.dto;

public record PecaResponseDTO(
        Integer id,
        String nome,
        double valor,
        Integer idFornecedor,
        int estoque
) {}
