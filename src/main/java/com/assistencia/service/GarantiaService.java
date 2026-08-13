package com.assistencia.service;

import com.assistencia.entity.Garantia;
import com.assistencia.repository.GarantiaRepository;
import com.assistencia.repository.OrdemDeServicoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GarantiaService {

    private final GarantiaRepository garantiaRepository;

    public GarantiaService(GarantiaRepository garantiaRepository) {
        this.garantiaRepository = garantiaRepository;
    }

    public Garantia adicionaGarantia(Garantia garantia) {
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
        garantiaRepository.deleteById(id);
        return true;
    }

    public List<Garantia> listar() {
        return garantiaRepository.findAll();
    }
}