package com.assistencia.dto;

import jakarta.validation.constraints.Positive;

public record PecaAdicionarAoEstoqueDTO(
        @Positive(message = "a quantidade nao pode ser menor que 1.")
        int estoque
) {}
