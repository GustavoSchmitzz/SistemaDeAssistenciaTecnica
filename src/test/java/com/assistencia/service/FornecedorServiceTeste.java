package com.assistencia.service;

import com.assistencia.entity.Fornecedor;
import com.assistencia.repository.FornecedorRepository;
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

        when(fornecedorRepository.save(fornecedor)).thenReturn(fornecedor);

        Fornecedor resultado = fornecedorService.cadastrar(fornecedor);
        assertNotNull(resultado);
        assertEquals("nome fornecedor", resultado.getNome());
        assertEquals("65999999999", resultado.getTelefone());
        verify(fornecedorRepository, times(1)).save(fornecedor);
    }

    @Test
    void testaSeBuscaPorIdComSucesso() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(1);

        when(fornecedorRepository.findById(1)).thenReturn(Optional.of(fornecedor));

        Fornecedor resultado = fornecedorService.buscaPorId(1);
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        verify(fornecedorRepository, times(1)).findById(1);
    }

    @Test
    void testaSeLancaExcecaoBuscaPorIdInvalido() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> fornecedorService.buscaPorId(0)
        );
        assertEquals("id nao pode ser menor ou igual a zero.", excecao.getMessage());
        verify(fornecedorRepository, never()).findById(anyInt());
    }

    @Test
    void testaSeLancaExcecaoBuscaPorIdNaoEncontrado() {
        when(fornecedorRepository.findById(1)).thenReturn(Optional.empty());

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> fornecedorService.buscaPorId(1)
        );
        assertEquals("cliente nao encontrado.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoRemoverComIdInvalido() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> fornecedorService.remover(0)
        );
        assertEquals("id nao pode ser menor ou igual a zero.", excecao.getMessage());
        verify(fornecedorRepository, never()).existsById(anyInt());
    }

    @Test
    void testaSeAtualizaFornecedorComSucesso() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(1);
        fornecedor.setNome("NOVO NOME");
        fornecedor.setTelefone("65999999999");

        when(fornecedorRepository.save(fornecedor)).thenReturn(fornecedor);

        boolean resultado = fornecedorService.atualizar(fornecedor);
        assertTrue(resultado);
        assertEquals("novo nome", fornecedor.getNome());
        verify(fornecedorRepository, times(1)).save(fornecedor);
    }

    @Test
    void testaSeListaFornecedoresComSucesso() {
        int pagina = 2;
        int limite = 10;
        Pageable pageable = PageRequest.of(pagina - 1, limite);
        List<Fornecedor> listaMock = List.of(new Fornecedor(), new Fornecedor());
        Page<Fornecedor> paginaMock = new PageImpl<>(listaMock);

        when(fornecedorRepository.findAll(pageable)).thenReturn(paginaMock);

        List<Fornecedor> resultado = fornecedorService.listar(pagina, limite);
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(fornecedorRepository, times(1)).findAll(pageable);
    }
}