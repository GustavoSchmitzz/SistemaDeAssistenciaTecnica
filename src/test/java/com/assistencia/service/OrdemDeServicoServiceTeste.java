package com.assistencia.service;

import com.assistencia.entity.*;
import com.assistencia.repository.OrdemDeServicoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrdemDeServicoServiceTeste {

    @Mock
    private OrdemDeServicoRepository ordemDeServicoRepository;

    @InjectMocks
    private OrdemDeServicoService ordemDeServicoService;

    private OrdemDeServico criaOrdemValida() {
        OrdemDeServico os = new OrdemDeServico();
        os.setValorServico(150.50);
        os.setFuncionario(new Funcionario());
        os.setPeca(new PecaComDefeito());
        os.setStatusServico(new StatusServico());
        os.setGarantia(new Garantia());
        return os;
    }

    @Test
    void testaSeAbreOrdemDeServicoComSucessoEDefineDataInicio() {
        OrdemDeServico os = criaOrdemValida();

        when(ordemDeServicoRepository.cria(os)).thenReturn(os);

        OrdemDeServico resultado = ordemDeServicoService.abrirOrdem(os);

        assertNotNull(resultado);
        assertNotNull(resultado.getDataInicio());
        assertEquals(LocalDate.now(), resultado.getDataInicio());
        verify(ordemDeServicoRepository, times(1)).cria(os);
    }

    @Test
    void testaSeLancaExcecaoAbrirOrdemNula() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> ordemDeServicoService.abrirOrdem(null)
        );
        assertEquals("ordemDeServico nao pode ser nulo.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAbrirOrdemComValorNegativo() {
        OrdemDeServico os = criaOrdemValida();
        os.setValorServico(-10.0);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> ordemDeServicoService.abrirOrdem(os)
        );
        assertEquals("valorServico nao pode ser menor que zero.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAbrirOrdemComValorDeMuitasCasasDecimais() {
        OrdemDeServico os = criaOrdemValida();
        os.setValorServico(150.555);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> ordemDeServicoService.abrirOrdem(os)
        );
        assertEquals("O valor do servico nao deve ter mais de duas casas decimais.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAbrirOrdemSemFuncionario() {
        OrdemDeServico os = criaOrdemValida();
        os.setFuncionario(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> ordemDeServicoService.abrirOrdem(os)
        );
        assertEquals("Tecnico nao pode ser nulo.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAbrirOrdemSemPeca() {
        OrdemDeServico os = criaOrdemValida();
        os.setPeca(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> ordemDeServicoService.abrirOrdem(os)
        );
        assertEquals("Peca nao pode ser nulo.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAbrirOrdemSemStatus() {
        OrdemDeServico os = criaOrdemValida();
        os.setStatusServico(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> ordemDeServicoService.abrirOrdem(os)
        );
        assertEquals("StatusServico nao pode ser nulo.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAbrirOrdemSemGarantia() {
        OrdemDeServico os = criaOrdemValida();
        os.setGarantia(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> ordemDeServicoService.abrirOrdem(os)
        );
        assertEquals("Garantia nao pode ser nulo.", excecao.getMessage());
    }

    @Test
    void testaSeBuscaOrdemPorIdComSucesso() {
        OrdemDeServico os = new OrdemDeServico();
        os.setId(1);

        when(ordemDeServicoRepository.buscaOID(1)).thenReturn(os);

        OrdemDeServico resultado = ordemDeServicoService.buscaOId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        verify(ordemDeServicoRepository, times(1)).buscaOID(1);
    }

    @Test
    void testaSeLancaExcecaoBuscaOrdemIdInvalido() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> ordemDeServicoService.buscaOId(0)
        );
        assertEquals("id nao pode ser igual ou menor que zero", excecao.getMessage());
        verify(ordemDeServicoRepository, never()).buscaOID(anyInt());
    }

    @Test
    void testaSeLancaExcecaoBuscaOrdemNaoEncontrada() {
        when(ordemDeServicoRepository.buscaOID(1)).thenReturn(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> ordemDeServicoService.buscaOId(1)
        );
        assertEquals("OrdemDeServico nao encontrada.", excecao.getMessage());
    }

    @Test
    void testaSeAtualizaOrdemComSucesso() {
        OrdemDeServico os = criaOrdemValida();

        when(ordemDeServicoRepository.atualiza(os)).thenReturn(true);

        boolean resultado = ordemDeServicoService.atualiza(os);

        assertTrue(resultado);
        verify(ordemDeServicoRepository, times(1)).atualiza(os);
    }

    @Test
    void testaSeLancaExcecaoAtualizarOrdemNula() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> ordemDeServicoService.atualiza(null)
        );
        assertEquals("ordemDeServico nao pode ser nulo.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAtualizarOrdemValorInvalido() {
        OrdemDeServico os = criaOrdemValida();
        os.setValorServico(-5.0);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> ordemDeServicoService.atualiza(os)
        );
        assertEquals("O valor do servico nao deve ter mais de duas casas decimais e menor que 0.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAtualizaOrdemSemFuncionario() {
        OrdemDeServico os = criaOrdemValida();
        os.setFuncionario(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> ordemDeServicoService.atualiza(os)
        );
        assertEquals("Tecnico nao pode ser nulo.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAtualizaOrdemSemPeca() {
        OrdemDeServico os = criaOrdemValida();
        os.setPeca(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> ordemDeServicoService.atualiza(os)
        );
        assertEquals("Peca nao pode ser nulo.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAtualizaOrdemSemStatus() {
        OrdemDeServico os = criaOrdemValida();
        os.setStatusServico(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> ordemDeServicoService.atualiza(os)
        );
        assertEquals("StatusServico nao pode ser nulo.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAtualizaOrdemSemGarantia() {
        OrdemDeServico os = criaOrdemValida();
        os.setGarantia(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> ordemDeServicoService.atualiza(os)
        );
        assertEquals("Garantia nao pode ser nulo.", excecao.getMessage());
    }

    @Test
    void testaSeListaOrdensDeServicoComSucesso() {
        List<OrdemDeServico> ordensMock = List.of(new OrdemDeServico(), new OrdemDeServico());
        when(ordemDeServicoRepository.buscaOrdemDeServicoDaPagina(10, 10)).thenReturn(ordensMock);

        List<OrdemDeServico> resultado = ordemDeServicoService.listar(2, 10);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(ordemDeServicoRepository, times(1)).buscaOrdemDeServicoDaPagina(10, 10);
    }

    @Test
    void testaSeLancaExcecaoListarComPaginaInvalida() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> ordemDeServicoService.listar(0, 10)
        );
        assertEquals("pagina nao pode ser igual ou menor a zero.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoListarComLimiteInvalido() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> ordemDeServicoService.listar(1, 0)
        );
        assertEquals("limite nao pode ser igual ou menor a zero", excecao.getMessage());
    }
}