package com.assistencia.dto;

import java.util.List;

public record FuncionarioListaResponseDTO(
        int pagina,
        int limite,
        List<FuncionarioResponseDTO> resultado
) {
}
