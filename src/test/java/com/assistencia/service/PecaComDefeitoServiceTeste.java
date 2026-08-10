package com.assistencia.service;

import com.assistencia.entity.Cliente;
import com.assistencia.entity.PecaComDefeito;
import com.assistencia.repository.PecaComDefeitoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PecaComDefeitoServiceTeste {

    @Mock
    private PecaComDefeitoRepository pecaComDefeitoRepository;

    @InjectMocks
    private PecaComDefeitoService pecaComDefeitoService;

    private PecaComDefeito criaPecaValida() {
        PecaComDefeito peca = new PecaComDefeito();
        peca.setId(1);
        peca.setTipoPeca("PLACA");
        peca.setMarca("MARCA");
        peca.setModelo("MODELO");
        peca.setDescricao("DESCRICAO");
        Cliente cliente = new Cliente();
        cliente.setId(1);
        peca.setCliente(cliente);
        return peca;
    }

    @Test
    void testaSeBuscaPorIdComSucesso() {
        PecaComDefeito peca = criaPecaValida();
        when(pecaComDefeitoRepository.buscaOID(1)).thenReturn(peca);

        PecaComDefeito resultado = pecaComDefeitoService.buscaPorID(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        verify(pecaComDefeitoRepository, times(1)).buscaOID(1);
    }

    @Test
    void testaSeLancaExcecaoBuscaPorIdMenorOuIgualAZero() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaComDefeitoService.buscaPorID(0)
        );
        assertEquals("O Id nao pode ser menor ou igual a zero.", excecao.getMessage());
        verify(pecaComDefeitoRepository, never()).buscaOID(anyInt());
    }

    @Test
    void testaSeLancaExcecaoBuscaPorIdNaoEncontrado() {
        when(pecaComDefeitoRepository.buscaOID(1)).thenReturn(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaComDefeitoService.buscaPorID(1)
        );
        assertEquals("Peca com defeito nao encontrada no banco de dados.", excecao.getMessage());
    }

    @Test
    void testaSeAdicionaPecaComSucessoEConverteParaLowerCase() {
        PecaComDefeito peca = criaPecaValida();
        when(pecaComDefeitoRepository.criar(peca)).thenReturn(peca);

        PecaComDefeito resultado = pecaComDefeitoService.adicionaPeca(peca);

        assertEquals("placa", resultado.getTipoPeca());
        assertEquals("marca", resultado.getMarca());
        assertEquals("modelo", resultado.getModelo());
        assertEquals("descricao", resultado.getDescricao());
        verify(pecaComDefeitoRepository, times(1)).criar(peca);
    }

    @Test
    void testaSeLancaExcecaoAdicionaPecaNula() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaComDefeitoService.adicionaPeca(null)
        );
        assertEquals("Peca com defeito nao pode ser nulo.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAdicionaPecaComTipoVazio() {
        PecaComDefeito peca = criaPecaValida();
        peca.setTipoPeca("");

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaComDefeitoService.adicionaPeca(peca)
        );
        assertEquals("O Tipo de Peca nao pode ser nulo ou vazio.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAdicionaPecaComMarcaVazia() {
        PecaComDefeito peca = criaPecaValida();
        peca.setMarca("");

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaComDefeitoService.adicionaPeca(peca)
        );
        assertEquals("A marca nao pode ser nulo ou vazio.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAdicionaPecaComDescricaoVazia() {
        PecaComDefeito peca = criaPecaValida();
        peca.setDescricao("");

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaComDefeitoService.adicionaPeca(peca)
        );
        assertEquals("A Descricao nao pode ser nula ou vazia.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAdicionaPecaComModeloVazio() {
        PecaComDefeito peca = criaPecaValida();
        peca.setModelo("");

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaComDefeitoService.adicionaPeca(peca)
        );
        assertEquals("O modelo nao pode ser nula ou vazia.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAdicionaPecaComClienteInvalido() {
        PecaComDefeito peca = criaPecaValida();
        Cliente cliente = new Cliente();
        cliente.setId(0);
        peca.setCliente(cliente);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaComDefeitoService.adicionaPeca(peca)
        );
        assertEquals("O Id nao pode ser nulo ou menor que 1.", excecao.getMessage());
    }

    @Test
    void testaSeAtualizaPecaComSucessoEConverteParaLowerCase() {
        PecaComDefeito peca = criaPecaValida();
        when(pecaComDefeitoRepository.buscaOID(1)).thenReturn(peca);
        when(pecaComDefeitoRepository.atualiza(peca)).thenReturn(true);

        boolean resultado = pecaComDefeitoService.atualizaPecaComDefeito(peca);

        assertTrue(resultado);
        assertEquals("placa", peca.getTipoPeca());
        assertEquals("marca", peca.getMarca());
        assertEquals("modelo", peca.getModelo());
        assertEquals("descricao", peca.getDescricao());
        verify(pecaComDefeitoRepository, times(1)).atualiza(peca);
    }

    @Test
    void testaSeLancaExcecaoAtualizaPecaNula() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaComDefeitoService.atualizaPecaComDefeito(null)
        );
        assertEquals("Peca com defeito nao pode ser nulo.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAtualizaPecaSemId() {
        PecaComDefeito peca = criaPecaValida();
        peca.setId(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaComDefeitoService.atualizaPecaComDefeito(peca)
        );
        assertEquals("O Id nao pode ser nulo.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAtualizaPecaNaoEncontrada() {
        PecaComDefeito peca = criaPecaValida();
        when(pecaComDefeitoRepository.buscaOID(1)).thenReturn(null);

        RuntimeException excecao = assertThrows(
                RuntimeException.class, () -> pecaComDefeitoService.atualizaPecaComDefeito(peca)
        );
        assertEquals("O produto com defeito nao existe no banco de dados.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAtualizaPecaComTipoVazio() {
        PecaComDefeito peca = criaPecaValida();
        peca.setTipoPeca("");
        when(pecaComDefeitoRepository.buscaOID(1)).thenReturn(peca);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaComDefeitoService.atualizaPecaComDefeito(peca)
        );
        assertEquals("O Tipo de Peca nao pode ser nulo ou vazio.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAtualizaPecaComModeloVazio() {
        PecaComDefeito peca = criaPecaValida();
        peca.setModelo("");
        when(pecaComDefeitoRepository.buscaOID(1)).thenReturn(peca);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaComDefeitoService.atualizaPecaComDefeito(peca)
        );
        assertEquals("O Modelo nao pode ser nulo ou vazio.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAtualizaPecaComMarcaVazia() {
        PecaComDefeito peca = criaPecaValida();
        peca.setMarca("");
        when(pecaComDefeitoRepository.buscaOID(1)).thenReturn(peca);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaComDefeitoService.atualizaPecaComDefeito(peca)
        );
        assertEquals("A marca nao pode ser nula ou vazia.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAtualizaPecaComDescricaoVazia() {
        PecaComDefeito peca = criaPecaValida();
        peca.setDescricao("");
        when(pecaComDefeitoRepository.buscaOID(1)).thenReturn(peca);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaComDefeitoService.atualizaPecaComDefeito(peca)
        );
        assertEquals("A Descricao nao pode ser nula ou vazia.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAtualizaPecaComClienteInvalido() {
        PecaComDefeito peca = criaPecaValida();
        Cliente cliente = new Cliente();
        cliente.setId(0);
        peca.setCliente(cliente);
        when(pecaComDefeitoRepository.buscaOID(1)).thenReturn(peca);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaComDefeitoService.atualizaPecaComDefeito(peca)
        );
        assertEquals("O Id nao pode ser nulo ou menor que 1.", excecao.getMessage());
    }

    @Test
    void testaSeListaPecasComDefeitoComSucesso() {
        List<PecaComDefeito> pecasMock = List.of(new PecaComDefeito(), new PecaComDefeito());
        when(pecaComDefeitoRepository.buscaPecasComDefeitoDaPagina(10, 10)).thenReturn(pecasMock);

        List<PecaComDefeito> resultado = pecaComDefeitoService.listar(2, 10);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(pecaComDefeitoRepository, times(1)).buscaPecasComDefeitoDaPagina(10, 10);
    }

    @Test
    void testaSeLancaExcecaoListarComPaginaInvalida() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaComDefeitoService.listar(0, 10)
        );
        assertEquals("pagina nao pode ser igual ou menor a zero.", excecao.getMessage());
        verify(pecaComDefeitoRepository, never()).buscaPecasComDefeitoDaPagina(anyInt(), anyInt());
    }

    @Test
    void testaSeLancaExcecaoListarComLimiteInvalido() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaComDefeitoService.listar(1, 0)
        );
        assertEquals("limite nao pode ser igual ou menor a zero", excecao.getMessage());
        verify(pecaComDefeitoRepository, never()).buscaPecasComDefeitoDaPagina(anyInt(), anyInt());
    }
}