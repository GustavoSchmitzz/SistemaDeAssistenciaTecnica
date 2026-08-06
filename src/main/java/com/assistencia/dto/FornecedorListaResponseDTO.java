package com.assistencia.dto;

import java.util.List;

public record FornecedorListaResponseDTO(
        int pagina,
        int limite,
        List<FornecedorResponseDTO> resultado
) {
}
