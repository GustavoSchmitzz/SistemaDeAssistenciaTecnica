package com.assistencia.service;

import com.assistencia.entity.OrdemDeServico;
import com.assistencia.repository.OrdemDeServicoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class OrdemDeServicoService {

    private final OrdemDeServicoRepository ordemDeServicoRepository;

    public OrdemDeServicoService(OrdemDeServicoRepository ordemDeServicoRepository) {
        this.ordemDeServicoRepository = ordemDeServicoRepository;
    }

    public OrdemDeServico abrirOrdem(OrdemDeServico ordemDeServico) {
        ordemDeServico.setDataInicio(LocalDate.now());
        return ordemDeServicoRepository.save(ordemDeServico);
    }

    public OrdemDeServico buscaOId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id nao pode ser igual ou menor que zero");
        }
        OrdemDeServico os = ordemDeServicoRepository.findById(id).orElse(null);
        if (os == null) {
            throw new IllegalArgumentException("OrdemDeServico nao encontrada.");
        } else {
            return os;
        }
    }

    public boolean atualiza(OrdemDeServico ordemDeServico) {
        ordemDeServicoRepository.save(ordemDeServico);
        return true;
    }

    public List<OrdemDeServico> listar(int pagina, int limite) {
        Pageable pageable = PageRequest.of(pagina - 1, limite);
        return ordemDeServicoRepository.findAll(pageable).getContent();
    }
}