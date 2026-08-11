package com.assistencia.service;

import com.assistencia.entity.Garantia;
import com.assistencia.repository.GarantiaRepository;
import com.assistencia.repository.OrdemDeServicoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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

        return garantiaRepository.save(garantia);
    }

    public Garantia buscaPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id nao pode ser menor que 1.");
        }
        Garantia garantia = garantiaRepository.findById(id).orElse(null);
        if (garantia == null) {
            throw new RuntimeException("Garantia nao encontrada.");
        }
        return garantia;
    }

    public boolean removerGarantia(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id nao pode ser menor que 1.");
        }
        if (!garantiaRepository.existsById(id)) {
            return false;
        }
        garantiaRepository.deleteById(id);
        return true;
    }

    public List<Garantia> listar() {
        return garantiaRepository.findAll();
    }
}