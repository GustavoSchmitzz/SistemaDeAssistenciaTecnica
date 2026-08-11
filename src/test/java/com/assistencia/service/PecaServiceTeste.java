package com.assistencia.service;

import com.assistencia.entity.Fornecedor;
import com.assistencia.entity.Peca;
import com.assistencia.repository.PecaRepository;
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
public class PecaServiceTeste {

    @Mock
    private PecaRepository pecaRepository;

    @InjectMocks
    private PecaService pecaService;

    private Peca criaPecaValida() {
        Peca peca = new Peca();
        peca.setId(1);
        peca.setNome("PLACA MAE");
        peca.setValor(500.0);
        peca.setEstoque(10);
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(1);
        peca.setFornecedor(fornecedor);
        return peca;
    }

    @Test
    void testaSeCriaPecaComSucessoEConverteParaLowerCase() {
        Peca peca = criaPecaValida();
        when(pecaRepository.save(peca)).thenReturn(peca);
        Peca resultado = pecaService.cria(peca);
        assertNotNull(resultado);
        assertEquals("placa mae", resultado.getNome());
        verify(pecaRepository, times(1)).save(peca);
    }

    @Test
    void testaSeLancaExcecaoCriaPecaNula() {
        NullPointerException excecao = assertThrows(
                NullPointerException.class, () -> pecaService.cria(null)
        );
        assertEquals("peca nao pode ser nula.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoCriaPecaComNomeVazio() {
        Peca peca = criaPecaValida();
        peca.setNome("");
        NullPointerException excecao = assertThrows(
                NullPointerException.class, () -> pecaService.cria(peca)
        );
        assertEquals("nome nao pode ser nulo ou vazio.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoCriaPecaSemFornecedor() {
        Peca peca = criaPecaValida();
        peca.setFornecedor(null);
        NullPointerException excecao = assertThrows(
                NullPointerException.class, () -> pecaService.cria(peca)
        );
        assertEquals("fornecedor nao pode ser nulo.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoCriaPecaComEstoqueNegativo() {
        Peca peca = criaPecaValida();
        peca.setEstoque(-1);
        NullPointerException excecao = assertThrows(
                NullPointerException.class, () -> pecaService.cria(peca)
        );
        assertEquals("estoque nao pode ser nulo nem negativo.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoCriaPecaComValorZeroOuNegativo() {
        Peca peca = criaPecaValida();
        peca.setValor(0);
        NullPointerException excecao = assertThrows(
                NullPointerException.class, () -> pecaService.cria(peca)
        );
        assertEquals("valor nao pode ser negativo ou menor que 0.", excecao.getMessage());
    }

    @Test
    void testaSeBuscaPorIdComSucesso() {
        Peca peca = criaPecaValida();
        when(pecaRepository.findById(1)).thenReturn(Optional.of(peca));
        Peca resultado = pecaService.buscaPorId(1);
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        verify(pecaRepository, times(1)).findById(1);
    }

    @Test
    void testaSeLancaExcecaoBuscaPorIdMenorOuIgualAZero() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaService.buscaPorId(0)
        );
        assertEquals("Id nao pode ser igual ou menor que zero", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoBuscaPorIdNaoEncontrado() {
        when(pecaRepository.findById(1)).thenReturn(Optional.empty());
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaService.buscaPorId(1)
        );
        assertEquals("Peca nao encontrada", excecao.getMessage());
    }

    @Test
    void testaSeDeletaPecaComSucesso() {
        Peca peca = criaPecaValida();
        peca.setEstoque(0);
        when(pecaRepository.findById(1)).thenReturn(Optional.of(peca));
        doNothing().when(pecaRepository).deleteById(1);
        boolean resultado = pecaService.deletarPeca(1);
        assertTrue(resultado);
        verify(pecaRepository, times(1)).deleteById(1);
    }

    @Test
    void testaSeLancaExcecaoDeletarPecaComIdInvalido() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaService.deletarPeca(0)
        );
        assertEquals("Id nao pode ser igual ou menor que zero", excecao.getMessage());
        verify(pecaRepository, never()).deleteById(anyInt());
    }

    @Test
    void testaSeLancaExcecaoDeletarPecaComEstoqueMaiorQueZero() {
        Peca peca = criaPecaValida();
        peca.setEstoque(5);
        when(pecaRepository.findById(1)).thenReturn(Optional.of(peca));
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaService.deletarPeca(1)
        );
        assertEquals("Nao e possivel deletar uma peca que tem no estoque", excecao.getMessage());
        verify(pecaRepository, never()).deleteById(anyInt());
    }

    @Test
    void testaSeAdicionaAoEstoqueComSucesso() {
        Peca peca = criaPecaValida();
        peca.setEstoque(5);
        when(pecaRepository.findById(1)).thenReturn(Optional.of(peca));
        when(pecaRepository.save(peca)).thenReturn(peca);
        Peca resultado = pecaService.adicionarAoEstoque(1, 10);
        assertNotNull(resultado);
        assertEquals(15, resultado.getEstoque());
        verify(pecaRepository, times(1)).save(peca);
    }

    @Test
    void testaSeLancaExcecaoAdicionaAoEstoqueComIdInvalido() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaService.adicionarAoEstoque(0, 10)
        );
        assertEquals("Id e quantidade nao pode ser igual ou menor que zero", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAdicionaAoEstoqueComQuantidadeInvalida() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaService.adicionarAoEstoque(1, 0)
        );
        assertEquals("Id e quantidade nao pode ser igual ou menor que zero", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAdicionaAoEstoquePecaNaoEncontrada() {
        when(pecaRepository.findById(1)).thenReturn(Optional.empty());
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaService.adicionarAoEstoque(1, 10)
        );
        assertEquals("Peca nao encontrada", excecao.getMessage());
    }

    @Test
    void testaSeListaPecasComSucesso() {
        int pagina = 2;
        int limite = 10;
        Pageable pageable = PageRequest.of(pagina - 1, limite);
        List<Peca> pecasMock = List.of(new Peca(), new Peca());
        Page<Peca> paginaMock = new PageImpl<>(pecasMock);

        when(pecaRepository.findAll(pageable)).thenReturn(paginaMock);

        List<Peca> resultado = pecaService.listar(pagina, limite);
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(pecaRepository, times(1)).findAll(pageable);
    }

    @Test
    void testaSeLancaExcecaoListarComPaginaInvalida() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaService.listar(0, 10)
        );
        assertEquals("pagina nao pode ser igual ou menor a zero.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoListarComLimiteInvalido() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pecaService.listar(1, 0)
        );
        assertEquals("limite nao pode ser igual ou menor a zero", excecao.getMessage());
    }
}