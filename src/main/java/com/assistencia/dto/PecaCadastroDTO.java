package com.assistencia.dto;

import jakarta.validation.constraints.*;

public record PecaCadastroDTO(
        @NotBlank(message = "o nome nao pode ser vazio.")
        @Size(max = 100,message = "o nome nao pode ter mais de 100 caracteres.")
        String nome,
        @Positive(message = "o valor nao pode ser menou ou igual a zero.")
        @Digits(integer = 6, fraction = 2, message = "o valor do servico deve ter no maximo 2 casas decimais.")
        double valor,
        @NotNull(message = "o id nao pode ser nulo.")
        Integer idFornecedor,
        @PositiveOrZero(message = "o valor nao pode ser menor que zero.")
        int estoque
) {
}
