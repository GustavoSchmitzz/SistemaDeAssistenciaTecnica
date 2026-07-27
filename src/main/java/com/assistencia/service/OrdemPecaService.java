package com.assistencia.service;

import com.assistencia.entity.OrdemPeca;
import com.assistencia.repository.OrdemPecaRepository;

public class OrdemPecaService {
    private final OrdemPecaRepository ordemPecaRepository;
    public OrdemPecaService(OrdemPecaRepository ordemPecaRepository) {
        this.ordemPecaRepository = ordemPecaRepository;
    }

    public OrdemPeca abreOrdemPeca(OrdemPeca ordemPeca) {
        if (ordemPeca == null) {
            throw new IllegalArgumentException("ordemPeca nao pode ser nulo.");
        }
        if (ordemPeca.getQuantidade() <= 0) {
            throw new IllegalArgumentException("Quantidade nao pode ser menor ou igual a zero.");
        }
        if (ordemPeca.getOrdemDeServico() == null) {
            throw new IllegalArgumentException("OrdemDeServico nao pode ser nulo.");
        }
        if (ordemPeca.getPeca().getId() == null) {
            throw new IllegalArgumentException("Peca nao pode ser nulo.");
        }

         return ordemPecaRepository.cria(ordemPeca);
    }
}
