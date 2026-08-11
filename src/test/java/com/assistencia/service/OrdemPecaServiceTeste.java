package com.assistencia.service;

import com.assistencia.entity.OrdemDeServico;
import com.assistencia.entity.OrdemPeca;
import com.assistencia.entity.Peca;
import com.assistencia.repository.OrdemPecaRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrdemPecaServiceTeste {

    @Mock
    private OrdemPecaRepository ordemPecaRepository;

    @InjectMocks
    private OrdemPecaService ordemPecaService;

    @Test
    void testaSeAbreOrdemPecaComSucesso() {
        OrdemPeca ordemPeca = new OrdemPeca();
        ordemPeca.setQuantidade(2);
        ordemPeca.setOrdemDeServico(new OrdemDeServico());
        Peca peca = new Peca();
        peca.setId(1);
        ordemPeca.setPeca(peca);

        when(ordemPecaRepository.save(ordemPeca)).thenReturn(ordemPeca);

        OrdemPeca resultado = ordemPecaService.abreOrdemPeca(ordemPeca);
        assertNotNull(resultado);
        verify(ordemPecaRepository, times(1)).save(ordemPeca);
    }

    @Test
    void testaSeLancaExcecaoAbreOrdemPecaNulo() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> ordemPecaService.abreOrdemPeca(null)
        );
        assertEquals("ordemPeca nao pode ser nulo.", excecao.getMessage());
        verify(ordemPecaRepository, never()).save(any());
    }

    @Test
    void testaSeLancaExcecaoAbreOrdemPecaComQuantidadeZero() {
        OrdemPeca ordemPeca = new OrdemPeca();
        ordemPeca.setQuantidade(0);
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> ordemPecaService.abreOrdemPeca(ordemPeca)
        );
        assertEquals("Quantidade nao pode ser menor ou igual a zero.", excecao.getMessage());
        verify(ordemPecaRepository, never()).save(any());
    }

    @Test
    void testaSeLancaExcecaoAbreOrdemPecaSemOrdemDeServico() {
        OrdemPeca ordemPeca = new OrdemPeca();
        ordemPeca.setQuantidade(1);
        ordemPeca.setOrdemDeServico(null);
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> ordemPecaService.abreOrdemPeca(ordemPeca)
        );
        assertEquals("OrdemDeServico nao pode ser nulo.", excecao.getMessage());
        verify(ordemPecaRepository, never()).save(any());
    }

    @Test
    void testaSeLancaExcecaoAbreOrdemPecaSemPeca() {
        OrdemPeca ordemPeca = new OrdemPeca();
        ordemPeca.setQuantidade(1);
        ordemPeca.setOrdemDeServico(new OrdemDeServico());
        Peca pecaSemId = new Peca();
        ordemPeca.setPeca(pecaSemId);
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> ordemPecaService.abreOrdemPeca(ordemPeca)
        );
        assertEquals("Peca nao pode ser nulo.", excecao.getMessage());
        verify(ordemPecaRepository, never()).save(any());
    }

    @Test
    void testaSeListaOrdemPecaComSucesso() {
        int pagina = 2;
        int limite = 10;
        Pageable pageable = PageRequest.of(pagina - 1, limite);
        List<OrdemPeca> ordensMock = List.of(new OrdemPeca(), new OrdemPeca());
        Page<OrdemPeca> paginaMock = new PageImpl<>(ordensMock);

        when(ordemPecaRepository.findAll(pageable)).thenReturn(paginaMock);

        List<OrdemPeca> resultado = ordemPecaService.listar(pagina, limite);
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(ordemPecaRepository, times(1)).findAll(pageable);
    }

    @Test
    void testaSeLancaExcecaoListarOrdemPecaComPaginaInvalida() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> ordemPecaService.listar(0, 10)
        );
        assertEquals("pagina nao pode ser igual ou menor a zero.", excecao.getMessage());
        verify(ordemPecaRepository, never()).findAll(any(Pageable.class));
    }

    @Test
    void testaSeLancaExcecaoListarOrdemPecaComLimiteInvalido() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> ordemPecaService.listar(1, 0)
        );
        assertEquals("limite nao pode ser igual ou menor a zero", excecao.getMessage());
        verify(ordemPecaRepository, never()).findAll(any(Pageable.class));
    }
}