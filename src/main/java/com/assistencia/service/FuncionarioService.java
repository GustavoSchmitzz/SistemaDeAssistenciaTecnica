package com.assistencia.service;

import com.assistencia.entity.Funcionario;
import com.assistencia.repository.FuncionarioRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    public FuncionarioService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    public Funcionario buscaPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id nao pode ser menor ou igual a zero.");
        }
        Funcionario funcionario = funcionarioRepository.findById(id).orElse(null);
        if (funcionario == null) {
            throw new IllegalArgumentException("Tecnico nao encontrado.");
        }
        return funcionario;
    }

    public Funcionario cadastraFuncionario(Funcionario funcionario) {
        funcionario.setNome(funcionario.getNome().trim().toLowerCase());
        funcionario.setEspecialidade(funcionario.getEspecialidade().trim().toLowerCase());
        funcionario.setTelefone(funcionario.getTelefone().trim().toLowerCase());
        funcionario.setEmail(funcionario.getEmail().trim().toLowerCase());

        // Gera o hash da senha e salva no banco de dados
        funcionario.setSenha(BCrypt.hashpw(funcionario.getSenha(), BCrypt.gensalt()));

        return funcionarioRepository.save(funcionario);
    }

    public boolean deletaFuncionario(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id nao pode ser menor ou igual a zero.");
        }
        funcionarioRepository.deleteById(id);
        return true;
    }

    public boolean atualizaFuncionario(Funcionario funcionario) {

        funcionario.setNome(funcionario.getNome().trim().toLowerCase());
        funcionario.setEspecialidade(funcionario.getEspecialidade().trim().toLowerCase());
        funcionario.setTelefone(funcionario.getTelefone().trim());

        funcionarioRepository.save(funcionario);
        return true;
    }

    public Funcionario loginFuncionario(String email, String senha) {
        Funcionario funcionario = funcionarioRepository.findByEmail(email);
        if (funcionario == null) {
            throw new IllegalArgumentException("Email ou Senha incorreta.");
        }

        boolean igual = BCrypt.checkpw(senha, funcionario.getSenha());
        if (!igual) {
            return null;
        }
        return funcionario;
    }

    public List<Funcionario> listar(int pagina, int limite) {
        Pageable pageable = PageRequest.of(pagina - 1, limite);
        return funcionarioRepository.findAll(pageable).getContent();
    }
}