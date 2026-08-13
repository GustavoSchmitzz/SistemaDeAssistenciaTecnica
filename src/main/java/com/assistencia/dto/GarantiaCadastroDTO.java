package com.assistencia.dto;


import jakarta.validation.constraints.Min;

public record GarantiaCadastroDTO(
        @Min(value = 7, message = "a garantia deve ter ao menos 7 dias.")
        int diasDeGarantia
) {
}
