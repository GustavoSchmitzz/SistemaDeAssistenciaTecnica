package com.assistencia.dto;


public record OrdemDeServicoAtualizaDTO(
        double valor,
        Integer idFuncionario,
        Integer idGarantia,
        Integer  idStatusServico,
        Integer idPagamento
) {
}
