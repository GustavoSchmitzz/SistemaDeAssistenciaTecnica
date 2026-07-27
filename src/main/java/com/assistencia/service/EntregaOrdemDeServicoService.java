package com.assistencia.service;

import com.assistencia.entity.EntregaOrdemDeServico;
import com.assistencia.entity.OrdemDeServico;
import com.assistencia.repository.EntregaOrdemDeServicoRepository;
import com.assistencia.repository.OrdemDeServicoRepository;

import java.time.LocalDate;


public class EntregaOrdemDeServicoService {
    private OrdemDeServicoRepository ordemDeServicoRepository;
    private EntregaOrdemDeServicoRepository entregaServicoRepository;

    public EntregaOrdemDeServicoService(OrdemDeServicoRepository ordemDeServicoRepository,
                                        EntregaOrdemDeServicoRepository entregaServicoRepository) {
        this.ordemDeServicoRepository = ordemDeServicoRepository;
        this.entregaServicoRepository = entregaServicoRepository;
    }
    /* Status OS
     *
     * id = 1 significa em espera
     * id = 2 significs em andamento
     * id = 3 significa pronto para entrega
     * id = 4 significa entrega concluída
     */
    public EntregaOrdemDeServico entregaAOrdemDeServico(int idOS) {
        if (idOS <= 0) {
            throw new IllegalArgumentException("O id Da ordem de serviço nao pode ser negativo.");
        }
        OrdemDeServico os = ordemDeServicoRepository.buscaOID(idOS);
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

        boolean atualizou = ordemDeServicoRepository.atualiza(os);
        if (!atualizou) {
            throw new RuntimeException("Nao foi possivel atualizar a ordem de servico.");
        }

        EntregaOrdemDeServico entregaServico = new EntregaOrdemDeServico();
        entregaServico.setDataEntrega(LocalDate.now());
        entregaServico.setOrdemDeServico(os);

        return entregaServicoRepository.cria(entregaServico);
    }

}
