package com.assistencia.dto;

import java.util.List;

public record OrdemDeServicoListaResponseDTO(
        int pagina,
        int limite,
        List<OrdemDeServicoResponseDTO> resultado
) {
}
