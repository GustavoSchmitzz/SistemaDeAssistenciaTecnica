package com.assistencia.dto;

import java.util.List;

public record GarantiaListaResponseDTO (
        List<GarantiaResponseDTO> garantias
) {
}
