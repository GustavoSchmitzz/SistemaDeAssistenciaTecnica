package com.assistencia.service;

import com.assistencia.entity.Funcionario;
import com.assistencia.repository.FuncionarioRepository;

public class FuncionarioService {
    private final FuncionarioRepository funcionarioRepository;
    public FuncionarioService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    public Funcionario buscaPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id nao pode ser menor ou igual a zero.");
        }
        Funcionario funcionario = funcionarioRepository.buscaOID(id);
        if (funcionario == null) {
            throw new IllegalArgumentException("Tecnico nao encontrado.");
        }
        return funcionario;
    }
    public Funcionario cadastraTecnico(Funcionario funcionario) {
        if (funcionario == null) {
            throw new IllegalArgumentException("Tecnico nao pode ser nulo.");
        }
        if (funcionario.getNome() == null || funcionario.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome nao pode ser vazio.");
        }
        if (funcionario.getEspecialidade() == null || funcionario.getEspecialidade().trim().isEmpty()) {
            throw new IllegalArgumentException("Especialidade nao pode ser vazio.");
        }
        if (funcionario.getTelefone() == null || !funcionario.getTelefone().trim().matches("^[0-9]{10,11}$")) {
            throw new IllegalArgumentException("Telefone nao pode ser vazio ou com mais de 11 digitos.");
        }

        funcionario.setNome(funcionario.getNome().trim().toLowerCase());
        funcionario.setEspecialidade(funcionario.getEspecialidade().trim().toLowerCase());

        return funcionarioRepository.cria(funcionario);
    }
    public boolean deletaTecnico(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id nao pode ser menor ou igual a zero.");
        }
        Funcionario funcionario = funcionarioRepository.buscaOID(id);
        if (funcionario == null) {
            throw new IllegalArgumentException("Tecnico nao encontrado.");
        }

        return funcionarioRepository.deleta(id);
    }
    public boolean atualizaTecnico(Funcionario funcionario) {
        if (funcionario == null) {
            throw new IllegalArgumentException("Tecnico nao pode ser nulo.");
        }
        if (funcionarioRepository.buscaOID(funcionario.getId()) == null) {
            throw new RuntimeException("Tecnico nao encontrado.");
        }
        if (funcionario.getNome() == null || funcionario.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome nao pode ser vazio.");
        }
        if (funcionario.getEspecialidade() == null || funcionario.getEspecialidade().trim().isEmpty()) {
            throw new IllegalArgumentException("Especialidade nao pode ser vazio.");
        }
        if (funcionario.getTelefone() == null || !funcionario.getTelefone().trim().matches("^[0-9]{10,11}$")) {
            throw new IllegalArgumentException("Telefone nao pode ser vazio ou com mais de 11 digitos.");
        }

        funcionario.setNome(funcionario.getNome().trim().toLowerCase());
        funcionario.setEspecialidade(funcionario.getEspecialidade().trim().toLowerCase());
        funcionario.setTelefone(funcionario.getTelefone().trim());

        return funcionarioRepository.atualiza(funcionario);
    }
}
