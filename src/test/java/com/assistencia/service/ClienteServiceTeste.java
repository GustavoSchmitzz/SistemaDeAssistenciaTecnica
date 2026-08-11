package com.assistencia.service;

import com.assistencia.entity.Cliente;
import com.assistencia.repository.ClienteRepository;
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

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));

        Cliente retorno = clienteService.buscaPorId(1);
        assertNotNull(retorno);
        assertEquals(1, retorno.getId());
        assertEquals("gustavo schmitz", retorno.getNome());
        verify(clienteRepository, times(1)).findById(1);
    }

    @Test
    void deveCriarUmClienteERetornarNomeEEmailEmLowerCase() {
        Cliente cliente = new Cliente();
        cliente.setNome("gusTavo schmiTz");
        cliente.setEmail("GUStavo@teste.com");
        cliente.setTelefone("65999999999");

        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente retorno = clienteService.cadastrar(cliente);
        assertNotNull(retorno);
        assertEquals("gustavo schmitz", retorno.getNome());
        assertEquals("gustavo@teste.com", retorno.getEmail());
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void testaSeDeletaOCliente() {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setNome("gustavo schmitz");

        when(clienteRepository.existsById(1)).thenReturn(true);
        doNothing().when(clienteRepository).deleteById(1);

        boolean resultado = clienteService.remover(1);
        assertTrue(resultado);
        verify(clienteRepository, times(1)).existsById(1);
        verify(clienteRepository, times(1)).deleteById(1);
    }

    @Test
    void testaSeAtualizaCorretamente() {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setNome("gustavo schmitz");
        cliente.setEmail("gustavo@teste.com");
        cliente.setTelefone("65999999999");

        when(clienteRepository.save(cliente)).thenReturn(cliente);

        boolean resultado = clienteService.atualizar(cliente);
        assertTrue(resultado);
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void testaSeLancaExcecaoDeletarClienteInexistente() {
        int id = 87;
        when(clienteRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> clienteService.remover(id)
        );
        assertEquals("cliente nao encontrado.", excecao.getMessage());
        verify(clienteRepository, times(1)).existsById(id);
        verify(clienteRepository, never()).deleteById(anyInt());
    }

    @Test
    void testaSeNaoAceitaEmailInvalido() {
        Cliente cliente = new Cliente();
        cliente.setNome("gustavo schmitz");
        cliente.setEmail("12345678");
        cliente.setTelefone("65999999999");

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> clienteService.cadastrar(cliente)
        );
        assertEquals("email nao pode ser nulo, vazio ou ter mais de 100 caracteres.", excecao.getMessage());
    }

    @Test
    void testaSeNaoAceitaNomeCadastrarNulo() {
        Cliente cliente = new Cliente();
        cliente.setEmail("gustavo@teste.com");
        cliente.setTelefone("65999999999");

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> clienteService.cadastrar(cliente)
        );
        assertEquals("nome nao pode ser nulo, vazio ou ter mais de 100 caracteres.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoCadastrarClienteNulo() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> clienteService.cadastrar(null)
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
                IllegalArgumentException.class, () -> clienteService.cadastrar(cliente)
        );
        assertEquals("telefone nao pode ser nulo, vazio ou ter mais de 11 caracteres.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoBuscaPorIdMenorOuIgualAZero() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> clienteService.buscaPorId(0)
        );
        assertEquals("id nao pode ser menor ou igual a zero.", excecao.getMessage());
        verify(clienteRepository, never()).findById(anyInt());
    }

    @Test
    void testaSeLancaExcecaoBuscaPorIdNaoEncontrado() {
        when(clienteRepository.findById(99)).thenReturn(Optional.empty());

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> clienteService.buscaPorId(99)
        );
        assertEquals("cliente nao encontrado.", excecao.getMessage());
        verify(clienteRepository, times(1)).findById(99);
    }

    @Test
    void testaSeLancaExcecaoRemoverComIdMenorOuIgualAZero() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> clienteService.remover(-1)
        );
        assertEquals("id nao pode ser menor ou igual a zero.", excecao.getMessage());
        verify(clienteRepository, never()).existsById(anyInt());
    }

    @Test
    void testaSeLancaExcecaoAtualizarClienteNulo() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> clienteService.atualizar(null)
        );
        assertEquals("cliente nao pode ser nulo.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAtualizarClienteComIdNulo() {
        Cliente cliente = new Cliente();
        cliente.setNome("Gustavo");

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> clienteService.atualizar(cliente)
        );
        assertEquals("id nao pode ser nulo.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoAtualizarComNomeVazio() {
        Cliente clienteAtualizado = new Cliente();
        clienteAtualizado.setId(1);
        clienteAtualizado.setNome("");

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> clienteService.atualizar(clienteAtualizado)
        );
        assertEquals("nome nao pode ser nulo.", excecao.getMessage());
    }

    @Test
    void testaSeListaClientesComSucesso() {
        int pagina = 2;
        int limite = 10;
        Pageable pageable = PageRequest.of(pagina - 1, limite);
        List<Cliente> clientesMock = List.of(new Cliente(), new Cliente());
        Page<Cliente> paginaMock = new PageImpl<>(clientesMock);

        when(clienteRepository.findAll(pageable)).thenReturn(paginaMock);

        List<Cliente> resultado = clienteService.listar(pagina, limite);
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(clienteRepository, times(1)).findAll(pageable);
    }

    @Test
    void testaSeLancaExcecaoListarComPaginaInvalida() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> clienteService.listar(0, 10)
        );
        assertEquals("pagina nao pode ser igual ou menor a zero.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoListarComLimiteInvalido() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> clienteService.listar(1, -5)
        );
        assertEquals("limite nao pode ser igual ou menor a zero", excecao.getMessage());
    }
}