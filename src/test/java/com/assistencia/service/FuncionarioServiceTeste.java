package com.assistencia.service;

import com.assistencia.entity.Funcionario;
import com.assistencia.repository.FuncionarioRepository;
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
import static org.mindrot.jbcrypt.BCrypt.gensalt;
import static org.mindrot.jbcrypt.BCrypt.hashpw;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FuncionarioServiceTeste {

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @InjectMocks
    private FuncionarioService funcionarioService;

    @Test
    void testeCadastraFuncionarioEVerificaSeAtributosForamPalaLowerCase() {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Gustavo");
        funcionario.setEmail("GusTavo@teste.com");
        funcionario.setEspecialidade("Notebooks");
        funcionario.setSenha("@I1abcde");
        funcionario.setTelefone("65999999999");

        when(funcionarioRepository.save(any(Funcionario.class))).thenReturn(funcionario);

        Funcionario retorno = funcionarioService.cadastraFuncionario(funcionario);
        assertNotNull(retorno);
        assertEquals("gustavo", retorno.getNome());
        assertEquals("gustavo@teste.com", retorno.getEmail());
        assertEquals("notebooks", retorno.getEspecialidade());
        assertEquals("65999999999", retorno.getTelefone());
        verify(funcionarioRepository, times(1)).save(any(Funcionario.class));
    }

    @Test
    void testaSeOHashEhAplicadoNaSenha() {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Gustavo");
        funcionario.setEmail("GusTavo@teste.com");
        funcionario.setEspecialidade("Notebooks");
        funcionario.setSenha("@I1abcde");
        String senhaOriginal = funcionario.getSenha();
        funcionario.setTelefone("65999999999");

        when(funcionarioRepository.save(any(Funcionario.class))).thenReturn(funcionario);

        Funcionario retorno = funcionarioService.cadastraFuncionario(funcionario);
        assertNotNull(retorno);
        assertNotEquals(senhaOriginal, retorno.getSenha());
        verify(funcionarioRepository, times(1)).save(any(Funcionario.class));
    }

    @Test
    void testaSeLoginEstaFuncionando() {
        String senhaOriginal = "@I1abcde";
        String hash = hashpw(senhaOriginal, gensalt());
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("gustavo");
        funcionario.setEmail("gustavo@teste.com");
        funcionario.setEspecialidade("notebooks");
        funcionario.setSenha(hash);
        funcionario.setTelefone("65999999999");

        when(funcionarioRepository.findByEmail(funcionario.getEmail())).thenReturn(funcionario);

        Funcionario logado = funcionarioService.loginFuncionario(funcionario.getEmail(), senhaOriginal);
        assertNotNull(logado);
        assertNotNull(logado.getTelefone());
        assertNotNull(logado.getNome());
        assertEquals(funcionario.getEmail(), logado.getEmail());
        verify(funcionarioRepository, times(1)).findByEmail(funcionario.getEmail());
    }

    @Test
    void testaSeLancaExcecaoCadastrarFuncionarioNulo() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> funcionarioService.cadastraFuncionario(null)
        );
        assertEquals("Tecnico nao pode ser nulo.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoCadastrarFuncionarioEmailInvalido() {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Gustavo");
        funcionario.setEmail("email-invalido");
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> funcionarioService.cadastraFuncionario(funcionario)
        );
        assertEquals("Email invalido.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoCadastrarFuncionarioSenhaFraca() {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Gustavo");
        funcionario.setEmail("gustavo@teste.com");
        funcionario.setSenha("123");
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> funcionarioService.cadastraFuncionario(funcionario)
        );
        assertEquals("Senha nao pode ser vazia e deve conter letras maiusculas, minusculas, numeros e caracteres especiais.", excecao.getMessage());
    }

    @Test
    void testaSeLancaExcecaoLoginEmailNulo() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class, () -> funcionarioService.loginFuncionario(null, "senha")
        );
        assertEquals("Email e senha precisam ser preenchido.", excecao.getMessage());
    }

    @Test
    void testaSeRetornaNuloQuandoSenhaIncorretaNoLogin() {
        Funcionario funcionario = new Funcionario();
        funcionario.setEmail("gustavo@teste.com");
        funcionario.setSenha(org.mindrot.jbcrypt.BCrypt.hashpw("Senha@123", org.mindrot.jbcrypt.BCrypt.gensalt()));

        when(funcionarioRepository.findByEmail("gustavo@teste.com")).thenReturn(funcionario);

        Funcionario logado = funcionarioService.loginFuncionario("gustavo@teste.com", "SenhaErrada@123");
        assertNull(logado);
    }

    @Test
    void testaSeDeletaFuncionarioComSucesso() {
        when(funcionarioRepository.existsById(1)).thenReturn(true);
        doNothing().when(funcionarioRepository).deleteById(1);

        boolean resultado = funcionarioService.deletaFuncionario(1);
        assertTrue(resultado);
        verify(funcionarioRepository, times(1)).existsById(1);
        verify(funcionarioRepository, times(1)).deleteById(1);
    }

    @Test
    void testaSeListaFuncionariosComSucesso() {
        int pagina = 2;
        int limite = 10;
        Pageable pageable = PageRequest.of(pagina - 1, limite);
        List<Funcionario> funcionariosMock = List.of(new Funcionario(), new Funcionario());
        Page<Funcionario> paginaMock = new PageImpl<>(funcionariosMock);

        when(funcionarioRepository.findAll(pageable)).thenReturn(paginaMock);

        List<Funcionario> resultado = funcionarioService.listar(pagina, limite);
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(funcionarioRepository, times(1)).findAll(pageable);
    }
}