package com.assistencia.dto;

import java.util.List;

public record ClienteListaResponseDTO(
        int pagina,
        int limiteDaPagina,
        List<ClienteResponseDTO> clientes
) {
}
