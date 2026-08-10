package com.assistencia.service;

import com.assistencia.entity.EntregaOrdemDeServico;
import com.assistencia.entity.OrdemDeServico;
import com.assistencia.entity.StatusServico;
import com.assistencia.repository.EntregaOrdemDeServicoRepository;
import com.assistencia.repository.OrdemDeServicoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EntregaOrdemDeServicoServiceTeste {

    @Mock
    private OrdemDeServicoRepository ordemDeServicoRepository;

    @Mock
    private EntregaOrdemDeServicoRepository entregaServicoRepository;

    @InjectMocks
    private EntregaOrdemDeServicoService entregaOrdemDeServicoService;

    @Test
    void testaSeEntregaAOrdemDeServicoComSucesso() {
        OrdemDeServico os = new OrdemDeServico();
        os.setId(1);
        StatusServico status = new StatusServico();
        status.setId(3);
        os.setStatusServico(status);

        EntregaOrdemDeServico entregaCriada = new EntregaOrdemDeServico();
        entregaCriada.setId(1);

        when(ordemDeServicoRepository.buscaOID(1)).thenReturn(os);
        when(ordemDeServicoRepository.atualiza(os)).thenReturn(true);
        when(entregaServicoRepository.cria(any(EntregaOrdemDeServico.class))).thenReturn(entregaCriada);

        EntregaOrdemDeServico resultado = entregaOrdemDeServicoService.entregaAOrdemDeServico(1);

        assertNotNull(resultado);
        assertEquals(4, os.getStatusServico().getId());
        verify(ordemDeServicoRepository, times(1)).buscaOID(1);
        verify(ordemDeServicoRepository, times(1)).atualiza(os);
        verify(entregaServicoRepository, times(1)).cria(any(EntregaOrdemDeServico.class));
    }

    @Test
    void testaSeLancaExcecaoEntregaAOrdemDeServicoComIdInvalido() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> entregaOrdemDeServicoService.entregaAOrdemDeServico(0)
        );
        assertEquals("O id Da ordem de serviço nao pode ser negativo.", excecao.getMessage());
        verify(ordemDeServicoRepository, never()).buscaOID(anyInt());
    }

    @Test
    void testaSeLancaExcecaoEntregaAOrdemDeServicoNaoExistente() {
        when(ordemDeServicoRepository.buscaOID(1)).thenReturn(null);

        RuntimeException excecao = assertThrows(
                RuntimeException.class, () -> entregaOrdemDeServicoService.entregaAOrdemDeServico(1)
        );
        assertEquals("Ordem de servico nao existente.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoEntregaAOrdemDeServicoJaEntregue() {
        OrdemDeServico os = new OrdemDeServico();
        os.setId(1);
        StatusServico status = new StatusServico();
        status.setId(4);
        os.setStatusServico(status);

        when(ordemDeServicoRepository.buscaOID(1)).thenReturn(os);

        IllegalStateException excecao = assertThrows(
                IllegalStateException.class, () -> entregaOrdemDeServicoService.entregaAOrdemDeServico(1)
        );
        assertEquals("A ordem de servico ja foi entrege.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoEntregaAOrdemDeServicoNaoPronta() {
        OrdemDeServico os = new OrdemDeServico();
        os.setId(1);
        StatusServico status = new StatusServico();
        status.setId(2);
        os.setStatusServico(status);

        when(ordemDeServicoRepository.buscaOID(1)).thenReturn(os);

        IllegalStateException excecao = assertThrows(
                IllegalStateException.class, () -> entregaOrdemDeServicoService.entregaAOrdemDeServico(1)
        );
        assertEquals("A ordem de serviço nao esta pronta para entrega.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoFalhaAoAtualizarEntregaAOrdemDeServico() {
        OrdemDeServico os = new OrdemDeServico();
        os.setId(1);
        StatusServico status = new StatusServico();
        status.setId(3);
        os.setStatusServico(status);

        when(ordemDeServicoRepository.buscaOID(1)).thenReturn(os);
        when(ordemDeServicoRepository.atualiza(os)).thenReturn(false);

        RuntimeException excecao = assertThrows(
                RuntimeException.class, () -> entregaOrdemDeServicoService.entregaAOrdemDeServico(1)
        );
        assertEquals("Nao foi possivel atualizar a ordem de servico.", excecao.getMessage());
    }

    @Test
    void testaSeReverteAEntregaComSucesso() {
        EntregaOrdemDeServico entrega = new EntregaOrdemDeServico();
        entrega.setId(1);
        OrdemDeServico os = new OrdemDeServico();
        os.setId(2);
        StatusServico status = new StatusServico();
        status.setId(4);
        os.setStatusServico(status);
        entrega.setOrdemDeServico(os);

        when(entregaServicoRepository.buscaOID(1)).thenReturn(entrega);
        when(ordemDeServicoRepository.buscaOID(2)).thenReturn(os);
        when(ordemDeServicoRepository.atualiza(os)).thenReturn(true);
        when(entregaServicoRepository.deleta(1)).thenReturn(true);

        boolean resultado = entregaOrdemDeServicoService.reverteAEntrega(1);

        assertTrue(resultado);
        assertEquals(3, os.getStatusServico().getId());
        verify(ordemDeServicoRepository, times(1)).atualiza(os);
        verify(entregaServicoRepository, times(1)).deleta(1);
    }

    @Test
    void testaSeLancaExcecaoReverteAEntregaComIdInvalido() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> entregaOrdemDeServicoService.reverteAEntrega(0)
        );
        assertEquals("O id nao pode ser negativo.", excecao.getMessage());
        verify(entregaServicoRepository, never()).buscaOID(anyInt());
    }

    @Test
    void testaSeLancaExcecaoReverteAEntregaNaoEncontrada() {
        when(entregaServicoRepository.buscaOID(1)).thenReturn(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> entregaOrdemDeServicoService.reverteAEntrega(1)
        );
        assertEquals("O registro de entrega não foi encontrado.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoReverteAEntregaOSNaoExistente() {
        EntregaOrdemDeServico entrega = new EntregaOrdemDeServico();
        entrega.setId(1);
        OrdemDeServico os = new OrdemDeServico();
        os.setId(2);
        entrega.setOrdemDeServico(os);

        when(entregaServicoRepository.buscaOID(1)).thenReturn(entrega);
        when(ordemDeServicoRepository.buscaOID(2)).thenReturn(null);

        RuntimeException excecao = assertThrows(
                RuntimeException.class, () -> entregaOrdemDeServicoService.reverteAEntrega(1)
        );
        assertEquals("Ordem de servico nao existente.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoReverteAEntregaOSNaoEntregue() {
        EntregaOrdemDeServico entrega = new EntregaOrdemDeServico();
        entrega.setId(1);
        OrdemDeServico os = new OrdemDeServico();
        os.setId(2);
        StatusServico status = new StatusServico();
        status.setId(3);
        os.setStatusServico(status);
        entrega.setOrdemDeServico(os);

        when(entregaServicoRepository.buscaOID(1)).thenReturn(entrega);
        when(ordemDeServicoRepository.buscaOID(2)).thenReturn(os);

        IllegalStateException excecao = assertThrows(
                IllegalStateException.class, () -> entregaOrdemDeServicoService.reverteAEntrega(1)
        );
        assertEquals("A ordem de serviço nao esta entregue.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoFalhaAoAtualizarReverteAEntrega() {
        EntregaOrdemDeServico entrega = new EntregaOrdemDeServico();
        entrega.setId(1);
        OrdemDeServico os = new OrdemDeServico();
        os.setId(2);
        StatusServico status = new StatusServico();
        status.setId(4);
        os.setStatusServico(status);
        entrega.setOrdemDeServico(os);

        when(entregaServicoRepository.buscaOID(1)).thenReturn(entrega);
        when(ordemDeServicoRepository.buscaOID(2)).thenReturn(os);
        when(ordemDeServicoRepository.atualiza(os)).thenReturn(false);

        RuntimeException excecao = assertThrows(
                RuntimeException.class, () -> entregaOrdemDeServicoService.reverteAEntrega(1)
        );
        assertEquals("Nao foi possivel reverter a ordem de servico.", excecao.getMessage());
    }

    @Test
    void testaSeListaEntregaOrdemDeServicoComSucesso() {
        List<EntregaOrdemDeServico> listaMock = List.of(new EntregaOrdemDeServico(), new EntregaOrdemDeServico());
        when(entregaServicoRepository.buscaEntregaOrdemDeServicoDaPagina(10, 10)).thenReturn(listaMock);

        List<EntregaOrdemDeServico> resultado = entregaOrdemDeServicoService.listar(2, 10);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(entregaServicoRepository, times(1)).buscaEntregaOrdemDeServicoDaPagina(10, 10);
    }

    @Test
    void testaSeLancaExcecaoListarComPaginaInvalida() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> entregaOrdemDeServicoService.listar(0, 10)
        );
        assertEquals("pagina nao pode ser igual ou menor a zero.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoListarComLimiteInvalido() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> entregaOrdemDeServicoService.listar(1, 0)
        );
        assertEquals("limite nao pode ser igual ou menor a zero", excecao.getMessage());
    }
}