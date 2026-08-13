package com.assistencia.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record PecaAtualizarDTO(
        @PositiveOrZero(message = "o valor nao pode ser igual ou menor a zero.")
        @Digits(integer = 6, fraction = 2, message = "o valor do servico deve ter no maximo 2 casas decimais.")
        double valor,
        @NotNull(message = "o id nao pode ser nulo.")
        Integer idFornecedor
) {}
