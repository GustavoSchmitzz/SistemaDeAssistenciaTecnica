package com.assistencia.dto;

import java.time.LocalDate;

public record OrdemDeServicoServiceCadastraDTO(
        double valor,
        Integer idFuncionario,
        Integer  idPecaComDefeito,
        Integer idGarantia,
        Integer  idStatusServico,
        Integer idPagamento
) {}
