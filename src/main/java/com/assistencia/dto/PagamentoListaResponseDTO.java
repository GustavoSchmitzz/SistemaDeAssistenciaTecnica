package com.assistencia.dto;

import java.util.List;

public record PagamentoListaResponseDTO(
        List<PagamentoResponseDTO> formasDepagamento
) {
}
