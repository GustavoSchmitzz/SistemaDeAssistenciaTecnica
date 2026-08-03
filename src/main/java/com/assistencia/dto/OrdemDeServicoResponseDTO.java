package com.assistencia.dto;

import java.time.LocalDate;

public record OrdemDeServicoResponseDTO(
        Integer id,
        double valor,
        LocalDate dataAbertura,
        Integer idFuncionario,
        Integer  idPecaComDefeito,
        Integer idGarantia,
        Integer  idStatusServico,
        Integer idPagamento
) {}
