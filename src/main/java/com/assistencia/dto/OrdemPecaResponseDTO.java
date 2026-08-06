package com.assistencia.dto;

public record OrdemPecaResponseDTO(
        int id,
        int quantidade,
        Integer idPeca,
        Integer idOrdemDeServico
) {
}
