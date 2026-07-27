package com.assistencia.service;

import com.assistencia.entity.OrdemDeServico;
import com.assistencia.repository.FornecedorRepository;

import java.math.BigDecimal;

public class OrdemDeServicoService {
    private final FornecedorRepository fornecedorRepository;
    public OrdemDeServicoService(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    public OrdemDeServico abrirOrdem(OrdemDeServico ordemDeServico) {
        if (ordemDeServico == null) {
            throw new IllegalArgumentException("ordemDeServico nao pode ser nulo.");
        }
        if (ordemDeServico.getDataInicio() == null) {
            throw new IllegalArgumentException("dataInicio nao pode ser nulo.");
        }
        if (ordemDeServico.getValorServico() < 0) {
            throw new IllegalArgumentException("valorServico nao pode ser menor que zero.");
        }
        double valorServico = ordemDeServico.getValorServico();
        if(BigDecimal.valueOf(valorServico).scale() > 2) {
            throw new IllegalArgumentException("O valor do servico nao deve ter mais de duas casas decimais.");
        }
        if(ordemDeServico.getTecnico() == null) {
            throw new IllegalArgumentException("Tecnico nao pode ser nulo.");
        }
        if (ordemDeServico.getPeca() == null) {
            throw new IllegalArgumentException("Peca nao pode ser nulo.");
        }
        if (ordemDeServico.getStatusServico() == null)  {
            throw new IllegalArgumentException("StatusServico nao pode ser nulo.");
        }

        return ordemDeServico;
    }

}
