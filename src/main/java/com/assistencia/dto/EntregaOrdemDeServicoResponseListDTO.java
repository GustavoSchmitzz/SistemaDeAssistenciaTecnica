package com.assistencia.dto;

import java.util.List;

public record EntregaOrdemDeServicoResponseListDTO(
        int pagina,
        int limite,
        List<EntregaOrdemDeServicoResponseDTO> resultado
) {
}
