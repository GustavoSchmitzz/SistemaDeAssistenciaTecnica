package com.assistencia.service;

import com.assistencia.entity.Garantia;
import com.assistencia.entity.OrdemDeServico;
import com.assistencia.repository.GarantiaRepository;
import com.assistencia.repository.OrdemDeServicoRepository;

public class GarantiaService {
    private final GarantiaRepository garantiaRepository;
    private final OrdemDeServicoRepository ordemDeServicoRepository;

    public GarantiaService(GarantiaRepository garantiaRepository,
                           OrdemDeServicoRepository ordemDeServicoRepository) {
        this.garantiaRepository = garantiaRepository;
        this.ordemDeServicoRepository = ordemDeServicoRepository;
    }

    public Garantia adicionaGarantia(Garantia garantia) {
        if (garantia == null) {
            throw new IllegalArgumentException("A garantia nao pode ser nula.");
        }
        if (garantia.getDiasDeGarantia() < 90) {
            throw new IllegalArgumentException("Os dias de garantia nao deve ser menor que 90.");
        }
         return garantiaRepository.cria(garantia);
    }
    public Garantia buscaPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id nao pode ser menor que 1.");
        }
        Garantia garantia = garantiaRepository.buscaOID(id);
        if(garantia == null) {
            throw new RuntimeException("Garantia nao encontrada.");
        }

        return garantia;
    }
    public boolean removerGarantia(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id nao pode ser menor que 1.");
        }
        Garantia garantia = garantiaRepository.buscaOID(id);
        if(garantia == null) {
            throw new RuntimeException("Garantia nao encontrada.");
        }

        return garantiaRepository.deleta(id);
    }
}
