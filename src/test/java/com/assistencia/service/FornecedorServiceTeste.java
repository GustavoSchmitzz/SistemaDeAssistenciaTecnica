package com.assistencia.service;

import com.assistencia.entity.Fornecedor;
import com.assistencia.repository.FornecedorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FornecedorServiceTeste {

    @Mock
    private FornecedorRepository fornecedorRepository;

    @InjectMocks
    private FornecedorService fornecedorService;

    @Test
    void testaSeCadastraFornecedorComSucesso() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("NOME FORNECEDOR");
        fornecedor.setTelefone("65999999999");

        when(fornecedorRepository.cria(fornecedor)).thenReturn(fornecedor);

        Fornecedor resultado = fornecedorService.cadastrar(fornecedor);

        assertNotNull(resultado);
        assertEquals("nome fornecedor", resultado.getNome());
        assertEquals("65999999999", resultado.getTelefone());
        verify(fornecedorRepository, times(1)).cria(fornecedor);
    }

    @Test
    void testaSeLancaExcecaoCadastrarFornecedorNulo() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> fornecedorService.cadastrar(null)
        );
        assertEquals("cliente nao pode ser nulo.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoCadastrarFornecedorComNomeVazio() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("");
        fornecedor.setTelefone("65999999999");

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> fornecedorService.cadastrar(fornecedor)
        );
        assertEquals("nome nao pode ser nulo, vazio ou ter mais de 100 caracteres.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoCadastrarFornecedorComTelefoneInvalido() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Fornecedor");
        fornecedor.setTelefone("123");

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> fornecedorService.cadastrar(fornecedor)
        );
        assertEquals("telefone nao pode ser nulo, vazio ou ter mais de 11 caracteres.", excecao.getMessage());
    }

    @Test
    void testaSeBuscaPorIdComSucesso() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(1);

        when(fornecedorRepository.buscaOID(1)).thenReturn(fornecedor);

        Fornecedor resultado = fornecedorService.buscaPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        verify(fornecedorRepository, times(1)).buscaOID(1);
    }

    @Test
    void testaSeLancaExcecaoBuscaPorIdInvalido() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> fornecedorService.buscaPorId(0)
        );
        assertEquals("id nao pode ser menor ou igual a zero.", excecao.getMessage());
        verify(fornecedorRepository, never()).buscaOID(anyInt());
    }

    @Test
    void testaSeLancaExcecaoBuscaPorIdNaoEncontrado() {
        when(fornecedorRepository.buscaOID(1)).thenReturn(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> fornecedorService.buscaPorId(1)
        );
        assertEquals("cliente nao encontrado.", excecao.getMessage());
    }

    @Test
    void testaSeRemoveFornecedorComSucesso() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(1);

        when(fornecedorRepository.buscaOID(1)).thenReturn(fornecedor);
        when(fornecedorRepository.deleta(1)).thenReturn(true);

        boolean resultado = fornecedorService.remover(1);

        assertTrue(resultado);
        verify(fornecedorRepository, times(1)).deleta(1);
    }

    @Test
    void testaSeLancaExcecaoRemoverComIdInvalido() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> fornecedorService.remover(0)
        );
        assertEquals("id nao pode ser menor ou igual a zero.", excecao.getMessage());
        verify(fornecedorRepository, never()).deleta(anyInt());
    }

    @Test
    void testaSeLancaExcecaoRemoverFornecedorNaoEncontrado() {
        when(fornecedorRepository.buscaOID(1)).thenReturn(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> fornecedorService.remover(1)
        );
        assertEquals("cliente nao encontrado.", excecao.getMessage());
    }

    @Test
    void testaSeAtualizaFornecedorComSucesso() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(1);
        fornecedor.setNome("NOVO NOME");
        fornecedor.setTelefone("123");

        when(fornecedorRepository.atualiza(fornecedor)).thenReturn(true);

        boolean resultado = fornecedorService.atualizar(fornecedor);

        assertTrue(resultado);
        assertEquals("novo nome", fornecedor.getNome());
        verify(fornecedorRepository, times(1)).atualiza(fornecedor);
    }

    @Test
    void testaSeLancaExcecaoAtualizarFornecedorNulo() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> fornecedorService.atualizar(null)
        );
        assertEquals("fornecedor nao pode ser nulo.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAtualizarFornecedorComIdNulo() {
        Fornecedor fornecedor = new Fornecedor();

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> fornecedorService.atualizar(fornecedor)
        );
        assertEquals("id nao pode ser nulo.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAtualizarFornecedorComNomeVazio() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(1);
        fornecedor.setNome("");

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> fornecedorService.atualizar(fornecedor)
        );
        assertEquals("nome nao pode ser nulo.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAtualizarFornecedorComTelefoneInvalido() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(1);
        fornecedor.setNome("Fornecedor");
        fornecedor.setTelefone("65999999999");

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> fornecedorService.atualizar(fornecedor)
        );
        assertEquals("telefone nao pode ser nulo.", excecao.getMessage());
    }

    @Test
    void testaSeListaFornecedoresComSucesso() {
        List<Fornecedor> listaMock = List.of(new Fornecedor(), new Fornecedor());
        when(fornecedorRepository.buscaFornecedoresDaPagina(10, 10)).thenReturn(listaMock);

        List<Fornecedor> resultado = fornecedorService.listar(2, 10);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(fornecedorRepository, times(1)).buscaFornecedoresDaPagina(10, 10);
    }

    @Test
    void testaSeLancaExcecaoListarComPaginaInvalida() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> fornecedorService.listar(0, 10)
        );
        assertEquals("pagina nao pode ser igual ou menor a zero.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoListarComLimiteInvalido() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> fornecedorService.listar(1, 0)
        );
        assertEquals("limite nao pode ser igual ou menor a zero", excecao.getMessage());
    }
}