package com.assistencia.service;

import com.assistencia.entity.Cliente;
import com.assistencia.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClenteServiceTeste {
    @Mock
    private ClienteRepository clienteRepository;
    @InjectMocks
    private ClienteService clienteService;

    @Test
    void deveRetornarUmClienteSeIdExistir() {

        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setNome("gustavo schmitz");
        cliente.setEmail("gustavo@teste.com");
        cliente.setTelefone("65999999999");

        when(clienteRepository.buscarOID(1)).thenReturn(cliente);

        Cliente retorno = clienteService.buscaPorId(1);

        assertNotNull(retorno);
        assertEquals(1, retorno.getId());
        assertEquals("gustavo schmitz", retorno.getNome());

        verify(clienteRepository, times(1)).buscarOID(1);
    }
    @Test
    void deveCriarUmClienteERetornarNomeEEmailEmLowerCase() {
        Cliente cliente = new Cliente();
        cliente.setNome("gusTavo schmiTz");
        cliente.setEmail("GUStavo@teste.com");
        cliente.setTelefone("65999999999");

        when(clienteRepository.cria(cliente)).thenReturn(cliente);

        Cliente retorno = clienteService.cadastrar(cliente);

        assertNotNull(cliente);
        assertEquals("gustavo schmitz", retorno.getNome());
        assertEquals("gustavo@teste.com", retorno.getEmail());
        verify(clienteRepository, times(1)).cria(cliente);
    }
    @Test
    void testaSeDeletaOCliente() {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setNome("gustavo schmitz");

        when(clienteRepository.buscarOID(1)).thenReturn(cliente);
        when(clienteRepository.deleta(1)).thenReturn(true);

        boolean resultado = clienteService.remover(1);

        assertTrue(resultado);
        verify(clienteRepository, times(1)).buscarOID(1);
        verify(clienteRepository, times(1)).deleta(1);
    }
    @Test
    void testaSeAtualizaCorretamente() {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setNome("gustavo schmitz");
        cliente.setEmail("gustavo@teste.com");
        cliente.setTelefone("65999999999");

        when(clienteRepository.buscarOID(1)).thenReturn(cliente);
        when(clienteRepository.atualiza(cliente)).thenReturn(true);

        boolean resultado = clienteService.atualizar(cliente);

        assertTrue(resultado);
        verify(clienteRepository, times(1)).buscarOID(1);
        verify(clienteRepository, times(1)).atualiza(cliente);
    }
    @Test
    void testaSeLancaExcecaoDeletarClienteInexistente() {
        int id = 87;

        when(clienteRepository.buscarOID(id)).thenReturn(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> {
            clienteService.remover(id);
        });

        assertEquals("cliente nao encontrado.", excecao.getMessage());

        verify(clienteRepository, times(1)).buscarOID(id);
        verify(clienteRepository, never()).deleta(anyInt());
    }
}
