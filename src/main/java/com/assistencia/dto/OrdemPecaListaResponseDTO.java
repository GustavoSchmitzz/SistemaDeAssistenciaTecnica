package com.assistencia.dto;

import java.util.List;

public record OrdemPecaListaResponseDTO(
        int pagina,
        int limite,
        List<OrdemPecaResponseDTO> resultado
) {
}
