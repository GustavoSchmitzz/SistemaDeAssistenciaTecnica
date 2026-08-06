package com.assistencia.dto;

import java.util.List;

public record PecaComDefeitoListaResponseDTO(
        int pagina,
        int limite,
        List<PecaComDefeitoResponseDTO> resultado
) {
}
