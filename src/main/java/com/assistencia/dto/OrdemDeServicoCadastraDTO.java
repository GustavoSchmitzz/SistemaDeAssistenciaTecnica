package com.assistencia.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrdemDeServicoCadastraDTO(
        @Positive(message = "o valor nao pode ser igual ou menor a zero.")
        double valor,

        @Positive(message = "o id do funcionario nao pode ser igual ou menor que zero.")
        @NotNull(message = "o id do funcionario nao pode ser nulo.")
        Integer idFuncionario,

        @Positive(message = "o id da peca nao pode ser igual ou menor que zero.")
        @NotNull(message = "o id da peca nao pode ser nulo.")
        Integer  idPecaComDefeito,

        @Positive(message = "o id da garantia nao pode ser igual ou menor que zero.")
        @NotNull(message = "o id da garantia nao pode ser nulo.")
        Integer idGarantia,

        @Positive(message = "o id do status nao pode ser igual ou menor que zero.")
        @NotNull(message = "o id do status nao pode ser nulo.")
        Integer  idStatusServico,

        @Positive(message = "o id do pagamento nao pode ser igual ou menor que zero.")
        @NotNull(message = "o id do pagamento nao pode ser nulo.")
        Integer idPagamento
) {}
