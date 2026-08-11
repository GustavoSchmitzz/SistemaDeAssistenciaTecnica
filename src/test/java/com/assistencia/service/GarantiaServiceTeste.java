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
import java.util.Optional;

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

        when(garantiaRepository.save(garantia)).thenReturn(garantia);

        Garantia resultado = garantiaService.adicionaGarantia(garantia);
        assertNotNull(resultado);
        verify(garantiaRepository, times(1)).save(garantia);
    }

    @Test
    void testaSeLancaExcecaoAdicionaGarantiaNula() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> garantiaService.adicionaGarantia(null)
        );
        assertEquals("A garantia nao pode ser nula.", excecao.getMessage());
        verify(garantiaRepository, never()).save(any());
    }

    @Test
    void testaSeLancaExcecaoAdicionaGarantiaMenorQueNoventaDias() {
        Garantia garantia = new Garantia();
        garantia.setDiasDeGarantia(89);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> garantiaService.adicionaGarantia(garantia)
        );
        assertEquals("Os dias de garantia nao deve ser menor que 90.", excecao.getMessage());
        verify(garantiaRepository, never()).save(any());
    }

    @Test
    void testaSeBuscaPorIdComSucesso() {
        Garantia garantia = new Garantia();
        garantia.setId(1);

        when(garantiaRepository.findById(1)).thenReturn(Optional.of(garantia));

        Garantia resultado = garantiaService.buscaPorId(1);
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        verify(garantiaRepository, times(1)).findById(1);
    }

    @Test
    void testaSeLancaExcecaoBuscaPorIdInvalido() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> garantiaService.buscaPorId(0)
        );
        assertEquals("Id nao pode ser menor que 1.", excecao.getMessage());
        verify(garantiaRepository, never()).findById(anyInt());
    }

    @Test
    void testaSeLancaExcecaoBuscaPorIdNaoEncontrado() {
        when(garantiaRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException excecao = assertThrows(
                RuntimeException.class, () -> garantiaService.buscaPorId(1)
        );
        assertEquals("Garantia nao encontrada.", excecao.getMessage());
    }

    @Test
    void testaSeRemoverGarantiaComSucesso() {
        when(garantiaRepository.existsById(1)).thenReturn(true);
        doNothing().when(garantiaRepository).deleteById(1);

        boolean resultado = garantiaService.removerGarantia(1);
        assertTrue(resultado);
        verify(garantiaRepository, times(1)).existsById(1);
        verify(garantiaRepository, times(1)).deleteById(1);
    }

    @Test
    void testaSeLancaExcecaoRemoverGarantiaComIdInvalido() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> garantiaService.removerGarantia(0)
        );
        assertEquals("Id nao pode ser menor que 1.", excecao.getMessage());
        verify(garantiaRepository, never()).existsById(anyInt());
    }

    @Test
    void testaSeListaGarantiasComSucesso() {
        List<Garantia> listaMock = List.of(new Garantia(), new Garantia());

        when(garantiaRepository.findAll()).thenReturn(listaMock);

        List<Garantia> resultado = garantiaService.listar();
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(garantiaRepository, times(1)).findAll();
    }
}