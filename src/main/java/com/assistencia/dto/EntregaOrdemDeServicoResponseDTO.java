package com.assistencia.dto;

import java.time.LocalDate;

public record EntregaOrdemDeServicoResponseDTO(
        int id,
        LocalDate dataEntrega
) {}
