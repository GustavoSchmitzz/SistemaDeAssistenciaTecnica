package com.assistencia.service;

import com.assistencia.entity.OrdemPeca;
import com.assistencia.repository.OrdemPecaRepository;

import java.util.List;

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
    public List<OrdemPeca> listar(int pagina, int limite) {
        if (pagina <= 0) {
            throw new IllegalArgumentException("pagina nao pode ser igual ou menor a zero.");
        }
        if (limite <= 0) {
            throw new IllegalArgumentException("limite nao pode ser igual ou menor a zero");
        }
        int offset = (pagina - 1) * limite;

        return ordemPecaRepository.buscaOrdemPecaDaPagina(limite, offset);
    }
}
