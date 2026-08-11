package com.assistencia.service;

import com.assistencia.entity.EntregaOrdemDeServico;
import com.assistencia.entity.OrdemDeServico;
import com.assistencia.repository.EntregaOrdemDeServicoRepository;
import com.assistencia.repository.OrdemDeServicoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EntregaOrdemDeServicoService {

    private final OrdemDeServicoRepository ordemDeServicoRepository;
    private final EntregaOrdemDeServicoRepository entregaServicoRepository;

    public EntregaOrdemDeServicoService(OrdemDeServicoRepository ordemDeServicoRepository,
                                        EntregaOrdemDeServicoRepository entregaServicoRepository) {
        this.ordemDeServicoRepository = ordemDeServicoRepository;
        this.entregaServicoRepository = entregaServicoRepository;
    }

    /* Status OS  *
     * id = 1 significa em espera
     * id = 2 significa em andamento
     * id = 3 significa pronto para entrega
     * id = 4 significa entrega concluída
     */
    public EntregaOrdemDeServico entregaAOrdemDeServico(int idOS) {
        if (idOS <= 0) {
            throw new IllegalArgumentException("O id Da ordem de servi o nao pode ser negativo.");
        }

        OrdemDeServico os = ordemDeServicoRepository.findById(idOS).orElse(null);
        if (os == null) {
            throw new RuntimeException("Ordem de servico nao existente.");
        }
        if (os.getStatusServico().getId() == 4) {
            throw new IllegalStateException("A ordem de servico ja foi entrege.");
        }
        if (os.getStatusServico().getId() != 3) {
            throw new IllegalStateException("A ordem de serviço nao esta pronta para entrega.");
        }

        os.getStatusServico().setId(4);

        try {
            ordemDeServicoRepository.save(os);
        } catch (Exception e) {
            throw new RuntimeException("Nao foi possivel atualizar a ordem de servico.");
        }

        EntregaOrdemDeServico entregaServico = new EntregaOrdemDeServico();
        entregaServico.setDataEntrega(LocalDate.now());
        entregaServico.setOrdemDeServico(os);

        return entregaServicoRepository.save(entregaServico);
    }

    public boolean reverteAEntrega(int idEntrega) {
        if (idEntrega <= 0) {
            throw new IllegalArgumentException("O id nao pode ser negativo.");
        }

        EntregaOrdemDeServico entrega = entregaServicoRepository.findById(idEntrega).orElse(null);
        if (entrega == null) {
            throw new IllegalArgumentException("O registro de entrega não foi encontrado.");
        }

        int idOS = entrega.getOrdemDeServico().getId();
        OrdemDeServico os = ordemDeServicoRepository.findById(idOS).orElse(null);
        if (os == null) {
            throw new RuntimeException("Ordem de servico nao existente.");
        }
        if (os.getStatusServico().getId() != 4) {
            throw new IllegalStateException("A ordem de serviço nao esta entregue.");
        }

        os.getStatusServico().setId(3);

        try {
            ordemDeServicoRepository.save(os);
        } catch (Exception e) {
            throw new RuntimeException("Nao foi possivel reverter a ordem de servico.");
        }

        entregaServicoRepository.deleteById(idEntrega);
        return true;
    }

    public List<EntregaOrdemDeServico> listar(int pagina, int limite) {
        if (pagina <= 0) {
            throw new IllegalArgumentException("pagina nao pode ser igual ou menor a zero.");
        }
        if (limite <= 0) {
            throw new IllegalArgumentException("limite nao pode ser igual ou menor a zero");
        }

        Pageable pageable = PageRequest.of(pagina - 1, limite);
        return entregaServicoRepository.findAll(pageable).getContent();
    }
}