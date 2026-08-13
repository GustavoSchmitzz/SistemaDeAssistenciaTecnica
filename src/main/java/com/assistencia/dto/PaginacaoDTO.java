package com.assistencia.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record PaginacaoDTO(
        @Min(value = 1, message = "a pagina deve ser no minimo 1.")
        Integer pagina,

        @Min(value = 1, message = "o limite deve ser no minimo 1.")
        @Max(value = 100, message = "o limite maximo por pagina deve ser 100.")
        Integer limite
) {
    public PaginacaoDTO {
        if (pagina == null) pagina = 1;
        if (limite == null) limite = 20;
    }
}