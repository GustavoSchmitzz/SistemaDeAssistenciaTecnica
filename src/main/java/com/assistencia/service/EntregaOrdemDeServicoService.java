package com.assistencia.service;

import com.assistencia.entity.EntregaOrdemDeServico;
import com.assistencia.entity.OrdemDeServico;
import com.assistencia.repository.EntregaOrdemDeServicoRepository;
import com.assistencia.repository.OrdemDeServicoRepository;

import java.time.LocalDate;
import java.util.List;


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
    public boolean reverteAEntrega(int idEntrega) {
        if (idEntrega <= 0) {
            throw new IllegalArgumentException("O id nao pode ser negativo.");
        }
        EntregaOrdemDeServico entrega = entregaServicoRepository.buscaOID(idEntrega);
        if (entrega == null) {
            throw new IllegalArgumentException("O registro de entrega não foi encontrado.");
        }

        int idOS = entrega.getOrdemDeServico().getId();
        OrdemDeServico os = ordemDeServicoRepository.buscaOID(idOS);

        if (os == null) {
            throw new RuntimeException("Ordem de servico nao existente.");
        }
        if (os.getStatusServico().getId() != 4) {
            throw new IllegalStateException("A ordem de serviço nao esta entregue.");
        }

        os.getStatusServico().setId(3);

        boolean atualizou = ordemDeServicoRepository.atualiza(os);
        if (!atualizou) {
            throw new RuntimeException("Nao foi possivel reverter a ordem de servico.");
        }

        return entregaServicoRepository.deleta(idEntrega);
    }
    public List<EntregaOrdemDeServico> listar(int pagina, int limite) {
        if (pagina <= 0) {
            throw new IllegalArgumentException("pagina nao pode ser igual ou menor a zero.");
        }
        if (limite <= 0) {
            throw new IllegalArgumentException("limite nao pode ser igual ou menor a zero");
        }
        int offset = (pagina - 1) * limite;

        return entregaServicoRepository.buscaEntregaOrdemDeServicoDaPagina(limite, offset);
    }
}
