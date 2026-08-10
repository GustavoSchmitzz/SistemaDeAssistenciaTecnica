package com.assistencia.service;

import com.assistencia.entity.Pagamento;
import com.assistencia.repository.PagamentoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PagamentoServiceTeste {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @InjectMocks
    private PagamentoService pagamentoService;

    @Test
    void testaSeBuscaPorIdComSucesso() {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(1);
        pagamento.setFormaPagamento("PIX");

        when(pagamentoRepository.buscaOID(1)).thenReturn(pagamento);

        Pagamento resultado = pagamentoService.buscaPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("PIX", resultado.getFormaPagamento());
        verify(pagamentoRepository, times(1)).buscaOID(1);
    }

    @Test
    void testaSeLancaExcecaoBuscaPorIdMenorOuIgualAZero() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> pagamentoService.buscaPorId(0)
        );
        assertEquals("Id nao pode ser menor ou igual a zero.", excecao.getMessage());
        verify(pagamentoRepository, never()).buscaOID(anyInt());
    }

    @Test
    void testaSeLancaExcecaoBuscaPorIdNaoEncontrado() {
        when(pagamentoRepository.buscaOID(1)).thenReturn(null);

        RuntimeException excecao = assertThrows(
                RuntimeException.class, () -> pagamentoService.buscaPorId(1)
        );
        assertEquals("forma de pagamento nao encontrada.", excecao.getMessage());
        verify(pagamentoRepository, times(1)).buscaOID(1);
    }

    @Test
    void testaSeListaPagamentosComSucesso() {
        List<Pagamento> listaMock = List.of(new Pagamento(), new Pagamento());
        when(pagamentoRepository.buscaPagamentos()).thenReturn(listaMock);

        List<Pagamento> resultado = pagamentoService.listar();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(pagamentoRepository, times(1)).buscaPagamentos();
    }
}