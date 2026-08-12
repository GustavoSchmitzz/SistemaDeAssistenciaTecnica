package com.assistencia.dto;

public record OrdemDeServicoCadastraDTO(
        double valor,
        Integer idFuncionario,
        Integer  idPecaComDefeito,
        Integer idGarantia,
        Integer  idStatusServico,
        Integer idPagamento
) {}
