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
        if (ordemDeServico == null) {
            throw new IllegalArgumentException("ordemDeServico nao pode ser nulo.");
        }
        if (ordemDeServico.getValorServico() < 0) {
            throw new IllegalArgumentException("valorServico nao pode ser menor que zero.");
        }
        double valorServico = ordemDeServico.getValorServico();
        if (BigDecimal.valueOf(valorServico).scale() > 2) {
            throw new IllegalArgumentException("O valor do servico nao deve ter mais de duas casas decimais.");
        }
        if (ordemDeServico.getFuncionario() == null) {
            throw new IllegalArgumentException("Tecnico nao pode ser nulo.");
        }
        if (ordemDeServico.getPeca() == null) {
            throw new IllegalArgumentException("Peca nao pode ser nulo.");
        }
        if (ordemDeServico.getStatusServico() == null) {
            throw new IllegalArgumentException("StatusServico nao pode ser nulo.");
        }
        if (ordemDeServico.getGarantia() == null) {
            throw new IllegalArgumentException("Garantia nao pode ser nulo.");
        }

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
        if (ordemDeServico == null) {
            throw new IllegalArgumentException("ordemDeServico nao pode ser nulo.");
        }
        double valorServico = ordemDeServico.getValorServico();
        if (BigDecimal.valueOf(valorServico).scale() > 2 || valorServico < 0) {
            throw new IllegalArgumentException("O valor do servico nao deve ter mais de duas casas decimais e menor que 0.");
        }
        if (ordemDeServico.getFuncionario() == null) {
            throw new IllegalArgumentException("Tecnico nao pode ser nulo.");
        }
        if (ordemDeServico.getPeca() == null) {
            throw new IllegalArgumentException("Peca nao pode ser nulo.");
        }
        if (ordemDeServico.getStatusServico() == null) {
            throw new IllegalArgumentException("StatusServico nao pode ser nulo.");
        }
        if (ordemDeServico.getGarantia() == null) {
            throw new IllegalArgumentException("Garantia nao pode ser nulo.");
        }

        ordemDeServicoRepository.save(ordemDeServico);
        return true;
    }

    public List<OrdemDeServico> listar(int pagina, int limite) {
        if (pagina <= 0) {
            throw new IllegalArgumentException("pagina nao pode ser igual ou menor a zero.");
        }
        if (limite <= 0) {
            throw new IllegalArgumentException("limite nao pode ser igual ou menor a zero");
        }

        Pageable pageable = PageRequest.of(pagina - 1, limite);
        return ordemDeServicoRepository.findAll(pageable).getContent();
    }
}