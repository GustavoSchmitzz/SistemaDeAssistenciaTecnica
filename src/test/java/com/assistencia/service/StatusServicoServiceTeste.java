package com.assistencia.service;

import com.assistencia.entity.StatusServico;
import com.assistencia.repository.StatusServicoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatusServicoServiceTeste {

    @Mock
    private StatusServicoRepository statusServicoRepository;

    @InjectMocks
    private StatusServicoService statusServicoService;

    @Test
    void testaSeBuscaPorIdComSucesso() {
        StatusServico status = new StatusServico();
        status.setId(1);
        status.setStatus("EM ANDAMENTO");

        when(statusServicoRepository.buscaOID(1)).thenReturn(status);

        StatusServico resultado = statusServicoService.buscaPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("EM ANDAMENTO", resultado.getStatus());
        verify(statusServicoRepository, times(1)).buscaOID(1);
    }

    @Test
    void testaSeLancaExcecaoBuscaPorIdMenorOuIgualAZero() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> statusServicoService.buscaPorId(0)
        );
        assertEquals("Id nao pode ser igual ou menor que zero.", excecao.getMessage());
        verify(statusServicoRepository, never()).buscaOID(anyInt());
    }

    @Test
    void testaSeLancaExcecaoBuscaPorIdNaoEncontrado() {
        when(statusServicoRepository.buscaOID(1)).thenReturn(null);

        RuntimeException excecao = assertThrows(
                RuntimeException.class, () -> statusServicoService.buscaPorId(1)
        );
        assertEquals("Status nao encontrado no banco de dados.", excecao.getMessage());
        verify(statusServicoRepository, times(1)).buscaOID(1);
    }

    @Test
    void testaSeListaStatusServicoComSucesso() {
        List<StatusServico> listaMock = List.of(new StatusServico(), new StatusServico());
        when(statusServicoRepository.buscaStatusServico()).thenReturn(listaMock);

        List<StatusServico> resultado = statusServicoService.listar();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(statusServicoRepository, times(1)).buscaStatusServico();
    }
}