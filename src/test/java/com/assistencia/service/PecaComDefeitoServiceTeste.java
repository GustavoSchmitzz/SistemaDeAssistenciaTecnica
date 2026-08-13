package com.assistencia.service;

import com.assistencia.entity.Cliente;
import com.assistencia.entity.PecaComDefeito;
import com.assistencia.repository.PecaComDefeitoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

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
        peca.setProblema("DESCRICAO");
        Cliente cliente = new Cliente();
        cliente.setId(1);
        peca.setCliente(cliente);
        return peca;
    }

    @Test
    void testaSeBuscaPorIdComSucesso() {
        PecaComDefeito peca = criaPecaValida();
        when(pecaComDefeitoRepository.findById(1)).thenReturn(Optional.of(peca));
        PecaComDefeito resultado = pecaComDefeitoService.buscaPorID(1);
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        verify(pecaComDefeitoRepository, times(1)).findById(1);
    }

    @Test
    void testaSeLancaExcecaoBuscaPorIdMenorOuIgualAZero() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaComDefeitoService.buscaPorID(0)
        );
        assertEquals("O Id nao pode ser menor ou igual a zero.", excecao.getMessage());
        verify(pecaComDefeitoRepository, never()).findById(anyInt());
    }

    @Test
    void testaSeLancaExcecaoBuscaPorIdNaoEncontrado() {
        when(pecaComDefeitoRepository.findById(1)).thenReturn(Optional.empty());
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaComDefeitoService.buscaPorID(1)
        );
        assertEquals("Peca com defeito nao encontrada no banco de dados.", excecao.getMessage());
    }

    @Test
    void testaSeAdicionaPecaComSucessoEConverteParaLowerCase() {
        PecaComDefeito peca = criaPecaValida();
        when(pecaComDefeitoRepository.save(peca)).thenReturn(peca);
        PecaComDefeito resultado = pecaComDefeitoService.adicionaPeca(peca);
        assertEquals("placa", resultado.getTipoPeca());
        assertEquals("marca", resultado.getMarca());
        assertEquals("modelo", resultado.getModelo());
        assertEquals("descricao", resultado.getProblema());
        verify(pecaComDefeitoRepository, times(1)).save(peca);
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
        peca.setProblema("");
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
        when(pecaComDefeitoRepository.save(peca)).thenReturn(peca);
        boolean resultado = pecaComDefeitoService.atualizaPecaComDefeito(peca);
        assertTrue(resultado);
        assertEquals("placa", peca.getTipoPeca());
        assertEquals("marca", peca.getMarca());
        assertEquals("modelo", peca.getModelo());
        assertEquals("descricao", peca.getProblema());
        verify(pecaComDefeitoRepository, times(1)).save(peca);
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
    void testaSeLancaExcecaoAtualizaPecaComTipoVazio() {
        PecaComDefeito peca = criaPecaValida();
        peca.setTipoPeca("");
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaComDefeitoService.atualizaPecaComDefeito(peca)
        );
        assertEquals("O Tipo de Peca nao pode ser nulo ou vazio.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAtualizaPecaComModeloVazio() {
        PecaComDefeito peca = criaPecaValida();
        peca.setModelo("");
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaComDefeitoService.atualizaPecaComDefeito(peca)
        );
        assertEquals("O Modelo nao pode ser nulo ou vazio.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAtualizaPecaComMarcaVazia() {
        PecaComDefeito peca = criaPecaValida();
        peca.setMarca("");
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaComDefeitoService.atualizaPecaComDefeito(peca)
        );
        assertEquals("A marca nao pode ser nula ou vazia.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAtualizaPecaComDescricaoVazia() {
        PecaComDefeito peca = criaPecaValida();
        peca.setProblema("");
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
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaComDefeitoService.atualizaPecaComDefeito(peca)
        );
        assertEquals("O Id nao pode ser nulo ou menor que 1.", excecao.getMessage());
    }

    @Test
    void testaSeListaPecasComDefeitoComSucesso() {
        int pagina = 2;
        int limite = 10;
        Pageable pageable = PageRequest.of(pagina - 1, limite);
        List<PecaComDefeito> pecasMock = List.of(new PecaComDefeito(), new PecaComDefeito());
        Page<PecaComDefeito> paginaMock = new PageImpl<>(pecasMock);

        when(pecaComDefeitoRepository.findAll(pageable)).thenReturn(paginaMock);

        List<PecaComDefeito> resultado = pecaComDefeitoService.listar(pagina, limite);
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(pecaComDefeitoRepository, times(1)).findAll(pageable);
    }
}