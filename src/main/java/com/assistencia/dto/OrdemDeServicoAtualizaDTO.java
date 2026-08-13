package com.assistencia.dto;


import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrdemDeServicoAtualizaDTO(
        @Positive(message = "o valor nao pode ser igual ou menor que zero.")
        @Digits(integer = 100000,fraction = 2, message = "o valor do servico deve ter no maximo 2 casas decimais.")
        double valor,

        @Positive(message = "o id do funcionario nao pode ser igual ou menor que zero.")
        @NotNull(message = "o id do funcionario nao pode ser nulo.")
        Integer idFuncionario,

        @Positive(message = "o id da garantia nao pode ser igual ou menor que zero.")
        @NotNull(message = "o id da garantia nao pode ser nulo.")
        Integer idGarantia,

        @Positive(message = "o id do status nao pode ser igual ou menor que zero.")
        @NotNull(message = "o id do status nao pode ser nulo.")
        Integer  idStatusServico,

        @Positive(message = "o id do pagamento nao pode ser igual ou menor que zero.")
        @NotNull(message = "o id do pagamento nao pode ser nulo.")
        Integer idPagamento
) {
}
