package com.assistencia.service;

import com.assistencia.entity.StatusServico;
import com.assistencia.repository.StatusServicoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusServicoService {

    private final StatusServicoRepository statusServicoRepository;

    public StatusServicoService(StatusServicoRepository statusServicoRepository) {
        this.statusServicoRepository = statusServicoRepository;
    }

    public StatusServico buscaPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id nao pode ser igual ou menor que zero.");
        }
        StatusServico status = statusServicoRepository.findById(id).orElse(null);
        if (status == null) {
            throw new RuntimeException("Status nao encontrado no banco de dados.");
        }
        return status;
    }

    public List<StatusServico> listar() {
        return statusServicoRepository.findAll();
    }
}