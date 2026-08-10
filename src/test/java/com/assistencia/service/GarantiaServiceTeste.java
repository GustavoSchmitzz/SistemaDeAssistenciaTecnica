package com.assistencia.service;

import com.assistencia.entity.Garantia;
import com.assistencia.repository.GarantiaRepository;
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
public class GarantiaServiceTeste {

    @Mock
    private GarantiaRepository garantiaRepository;

    @Mock
    private OrdemDeServicoRepository ordemDeServicoRepository;

    @InjectMocks
    private GarantiaService garantiaService;

    @Test
    void testaSeAdicionaGarantiaComSucesso() {
        Garantia garantia = new Garantia();
        garantia.setDiasDeGarantia(90);

        when(garantiaRepository.cria(garantia)).thenReturn(garantia);

        Garantia resultado = garantiaService.adicionaGarantia(garantia);

        assertNotNull(resultado);
        verify(garantiaRepository, times(1)).cria(garantia);
    }

    @Test
    void testaSeLancaExcecaoAdicionaGarantiaNula() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> garantiaService.adicionaGarantia(null)
        );
        assertEquals("A garantia nao pode ser nula.", excecao.getMessage());
        verify(garantiaRepository, never()).cria(any());
    }

    @Test
    void testaSeLancaExcecaoAdicionaGarantiaMenorQueNoventaDias() {
        Garantia garantia = new Garantia();
        garantia.setDiasDeGarantia(89);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> garantiaService.adicionaGarantia(garantia)
        );
        assertEquals("Os dias de garantia nao deve ser menor que 90.", excecao.getMessage());
        verify(garantiaRepository, never()).cria(any());
    }

    @Test
    void testaSeBuscaPorIdComSucesso() {
        Garantia garantia = new Garantia();
        garantia.setId(1);

        when(garantiaRepository.buscaOID(1)).thenReturn(garantia);

        Garantia resultado = garantiaService.buscaPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        verify(garantiaRepository, times(1)).buscaOID(1);
    }

    @Test
    void testaSeLancaExcecaoBuscaPorIdInvalido() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> garantiaService.buscaPorId(0)
        );
        assertEquals("Id nao pode ser menor que 1.", excecao.getMessage());
        verify(garantiaRepository, never()).buscaOID(anyInt());
    }

    @Test
    void testaSeLancaExcecaoBuscaPorIdNaoEncontrado() {
        when(garantiaRepository.buscaOID(1)).thenReturn(null);

        RuntimeException excecao = assertThrows(
                RuntimeException.class, () -> garantiaService.buscaPorId(1)
        );
        assertEquals("Garantia nao encontrada.", excecao.getMessage());
    }

    @Test
    void testaSeRemoverGarantiaComSucesso() {
        Garantia garantia = new Garantia();
        garantia.setId(1);

        when(garantiaRepository.deleta(1)).thenReturn(true);

        boolean resultado = garantiaService.removerGarantia(1);

        assertTrue(resultado);
        verify(garantiaRepository, times(1)).deleta(1);
    }

    @Test
    void testaSeLancaExcecaoRemoverGarantiaComIdInvalido() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> garantiaService.removerGarantia(0)
        );
        assertEquals("Id nao pode ser menor que 1.", excecao.getMessage());
        verify(garantiaRepository, never()).deleta(anyInt());
    }

    @Test
    void testaSeListaGarantiasComSucesso() {
        List<Garantia> listaMock = List.of(new Garantia(), new Garantia());
        when(garantiaRepository.buscaGarantias()).thenReturn(listaMock);

        List<Garantia> resultado = garantiaService.listar();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(garantiaRepository, times(1)).buscaGarantias();
    }
}