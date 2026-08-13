package com.assistencia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record PecaAtualizarDTO(
        @PositiveOrZero(message = "o valor nao pode ser igual ou menor a zero.")
        double valor,
        @NotNull(message = "o id nao pode ser nulo.")
        Integer idFornecedor
) {}
