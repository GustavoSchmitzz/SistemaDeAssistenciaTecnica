package com.assistencia.dto;

public record PecaComDefeitoResponseDTO(
        Integer id,
        String tipoPeca,
        String marca,
        String modelo,
        String descricao,
        Integer idCliente
) {
}
