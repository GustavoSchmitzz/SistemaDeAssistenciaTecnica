package com.assistencia.dto;

import java.util.List;

public record PecaListaResponseDTO(
        int pagina,
        int limite,
        List<PecaResponseDTO> resultado
) {
}
