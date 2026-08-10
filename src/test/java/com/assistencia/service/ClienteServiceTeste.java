package com.assistencia.service;

import com.assistencia.entity.Cliente;
import com.assistencia.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTeste {
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
    @Test
    void testaSeNaoAceitaEmailInvalido() {
        Cliente cliente = new Cliente();
        cliente.setNome("gustavo schmitz");
        cliente.setEmail("12345678");
        cliente.setTelefone("65999999999");

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> {
                    clienteService.cadastrar(cliente);
                }
        );

        assertEquals("email nao pode ser nulo, vazio ou ter mais de 100 caracteres.",  excecao.getMessage());
    }
    @Test
    void testaSeNaoAceitaNomeCadastrarNulo() {
        Cliente cliente = new Cliente();
        cliente.setEmail("gustavo schmitz");
        cliente.setTelefone("65999999999");

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> {
                    clienteService.cadastrar(cliente);
                }
        );
        assertEquals("nome nao pode ser nulo, vazio ou ter mais de 100 caracteres.",  excecao.getMessage());
    }
    @Test
    void testaSeLancaExcecaoCadastrarClienteNulo() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> {
                    clienteService.cadastrar(null);
                }
        );
        assertEquals("cliente nao pode ser nulo.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoCadastrarComTelefoneInvalido() {
        Cliente cliente = new Cliente();
        cliente.setNome("Gustavo Schmitz");
        cliente.setEmail("gustavo@teste.com");
        cliente.setTelefone("123");

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> {
                    clienteService.cadastrar(cliente);
                }
        );
        assertEquals("telefone nao pode ser nulo, vazio ou ter mais de 11 caracteres.", excecao.getMessage());
    }
    @Test
    void testaSeLancaExcecaoBuscaPorIdMenorOuIgualAZero() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> {
                    clienteService.buscaPorId(0);
                }
        );
        assertEquals("id nao pode ser menor ou igual a zero.", excecao.getMessage());
        verify(clienteRepository, never()).buscarOID(anyInt());
    }

    @Test
    void testaSeLancaExcecaoBuscaPorIdNaoEncontrado() {
        when(clienteRepository.buscarOID(99)).thenReturn(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> {
                    clienteService.buscaPorId(99);
                }
        );
        assertEquals("cliente nao encontrado.", excecao.getMessage());
        verify(clienteRepository, times(1)).buscarOID(99);
    }
    @Test
    void testaSeLancaExcecaoRemoverComIdMenorOuIgualAZero() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> {
                    clienteService.remover(-1);
                }
        );
        assertEquals("id nao pode ser menor ou igual a zero.", excecao.getMessage());
        verify(clienteRepository, never()).deleta(anyInt());
    }
    @Test
    void testaSeLancaExcecaoAtualizarClienteNulo() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> {
                    clienteService.atualizar(null);
                }
        );
        assertEquals("cliente nao pode ser nulo.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAtualizarClienteComIdNulo() {
        Cliente cliente = new Cliente();

        cliente.setNome("Gustavo");

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> {
                    clienteService.atualizar(cliente);
                }
        );
        assertEquals("id nao pode ser nulo.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAtualizarClienteInexistente() {
        Cliente cliente = new Cliente();
        cliente.setId(99);

        when(clienteRepository.buscarOID(99)).thenReturn(null);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> {
                    clienteService.atualizar(cliente);
                }
        );
        assertEquals("O cliente nao existe no banco de dados.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAtualizarComNomeVazio() {
        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(1);

        Cliente clienteAtualizado = new Cliente();
        clienteAtualizado.setId(1);
        clienteAtualizado.setNome("");

        when(clienteRepository.buscarOID(1)).thenReturn(clienteExistente);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> {
                    clienteService.atualizar(clienteAtualizado);
                }
        );
        assertEquals("nome nao pode ser nulo.", excecao.getMessage());
    }
    @Test
    void testaSeListaClientesComSucesso() {

        int pagina = 2;
        int limite = 10;
        int offsetEsperado = 10; // (2 - 1) * 10 = 10

        List<Cliente> clientesMock = List.of(new Cliente(), new Cliente());
        when(clienteRepository.buscaClientesDaPagina(limite, offsetEsperado)).thenReturn(clientesMock);


        List<Cliente> resultado = clienteService.listar(pagina, limite);


        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(clienteRepository, times(1)).buscaClientesDaPagina(limite, offsetEsperado);
    }

    @Test
    void testaSeLancaExcecaoListarComPaginaInvalida() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> {
                    clienteService.listar(0, 10);
                }
        );
        assertEquals("pagina nao pode ser igual ou menor a zero.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoListarComLimiteInvalido() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> {
                    clienteService.listar(1, -5);
                }
        );
        assertEquals("limite nao pode ser igual ou menor a zero", excecao.getMessage());
    }
}
