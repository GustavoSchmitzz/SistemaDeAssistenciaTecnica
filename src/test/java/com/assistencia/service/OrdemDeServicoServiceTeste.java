package com.assistencia.service;

import com.assistencia.entity.*;
import com.assistencia.repository.OrdemDeServicoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrdemDeServicoServiceTeste {

    @Mock
    private OrdemDeServicoRepository ordemDeServicoRepository;

    @InjectMocks
    private OrdemDeServicoService ordemDeServicoService;

    private OrdemDeServico criaOrdemValida() {
        OrdemDeServico os = new OrdemDeServico();
        os.setValorServico(150.50);
        os.setFuncionario(new Funcionario());
        os.setPeca(new PecaComDefeito());
        os.setStatusServico(new StatusServico());
        os.setGarantia(new Garantia());
        return os;
    }

    @Test
    void testaSeAbreOrdemDeServicoComSucessoEDefineDataInicio() {
        OrdemDeServico os = criaOrdemValida();
        when(ordemDeServicoRepository.save(os)).thenReturn(os);
        OrdemDeServico resultado = ordemDeServicoService.abrirOrdem(os);
        assertNotNull(resultado);
        assertNotNull(resultado.getDataInicio());
        assertEquals(LocalDate.now(), resultado.getDataInicio());
        verify(ordemDeServicoRepository, times(1)).save(os);
    }

    @Test
    void testaSeBuscaOrdemPorIdComSucesso() {
        OrdemDeServico os = new OrdemDeServico();
        os.setId(1);
        when(ordemDeServicoRepository.findById(1)).thenReturn(Optional.of(os));
        OrdemDeServico resultado = ordemDeServicoService.buscaOId(1);
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        verify(ordemDeServicoRepository, times(1)).findById(1);
    }

    @Test
    void testaSeLancaExcecaoBuscaOrdemIdInvalido() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> ordemDeServicoService.buscaOId(0)
        );
        assertEquals("id nao pode ser igual ou menor que zero", excecao.getMessage());
        verify(ordemDeServicoRepository, never()).findById(anyInt());
    }

    @Test
    void testaSeLancaExcecaoBuscaOrdemNaoEncontrada() {
        when(ordemDeServicoRepository.findById(1)).thenReturn(Optional.empty());
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> ordemDeServicoService.buscaOId(1)
        );
        assertEquals("OrdemDeServico nao encontrada.", excecao.getMessage());
    }

    @Test
    void testaSeAtualizaOrdemComSucesso() {
        OrdemDeServico os = criaOrdemValida();
        when(ordemDeServicoRepository.save(os)).thenReturn(os);
        boolean resultado = ordemDeServicoService.atualiza(os);
        assertTrue(resultado);
        verify(ordemDeServicoRepository, times(1)).save(os);
    }

    @Test
    void testaSeListaOrdensDeServicoComSucesso() {
        int pagina = 2;
        int limite = 10;
        Pageable pageable = PageRequest.of(pagina - 1, limite);
        List<OrdemDeServico> ordensMock = List.of(new OrdemDeServico(), new OrdemDeServico());
        Page<OrdemDeServico> paginaMock = new PageImpl<>(ordensMock);

        when(ordemDeServicoRepository.findAll(pageable)).thenReturn(paginaMock);

        List<OrdemDeServico> resultado = ordemDeServicoService.listar(pagina, limite);
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(ordemDeServicoRepository, times(1)).findAll(pageable);
    }

}